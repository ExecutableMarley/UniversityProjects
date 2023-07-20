import socket
import time
import pickle
import threading
import sys
import os

from typing import List
from enum import Enum

class OPCode(Enum):
    probe = 0
    sign_in = 1
    sign_out = 2
    sendMessage = 3
    broadCastMessage = 4
    getClientList = 5

class Message:
    def __init__(self, opcode : OPCode, sourceName : str, destName : str, content : str) -> None:
        self.opcode     = opcode
        self.sourceName = sourceName
        self.destName   = destName
        self.content    = content
    
    def getOpcode(self) -> int:
        return self.opcode
    
    def getSourceName(self) -> str:
        return self.sourceName
    
    def getDestName(self) -> str:
        return self.destName
    
    def getContent(self) -> str:
        return self.content
    
    def __str__(self) -> str:
        return "OPCode: " + str(self.opcode) + " Source: " + self.sourceName + " Dest: " + self.destName + " Content: " + self.content
    
class clientStruct:
    def __init__(self, address:str, port:int, name:str, sock: socket) -> None:
        self.address :str    = address
        self.port    :int    = port
        self.name    :str    = name
        self.sock    :socket = sock
        
    def getAddress(self) -> str:
        return self.address
    
    def getPort(self) -> int:
        return self.port
    
    def getName(self) -> str:
        return self.name
    
    def __eq__(self, __value: 'clientStruct') -> bool:
        return __value != None and self.name == __value.name #and self.address == __value.address
    
class TCPServer:
    def __init__(self, host = '', port = 0) -> None:
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.bind((host, port))
        self.host = self.sock.getsockname()[0]
        self.port = self.sock.getsockname()[1]
        self.clientList : List[clientStruct] = []
        self.stop = False
        self.thread: threading.Thread
    
    def start(self):
        self.sock.listen(30)
        self.thread = threading.Thread(target=self.listen)
        self.thread.start()
    
    def stop(self):
        pass
    
    def isRunning(self) -> bool:
        return self.thread.is_alive()
    
    def getHost(self) -> str:
        return self.host
    
    def getPort(self) -> int:
        return self.port
    
    def getClient(self, name: str) -> clientStruct:
        for client in self.clientList:
            if client.getName() == name:
                return client
        return None
    
    def removeClient(self, name : str) -> bool:
        client = self.getClient(name)
        if client == None:
            return False
        self.clients.remove(client)
        return True
    
    def handleClient(self, client: clientStruct):
        while not self.stop:
            try:
                rawData = client.sock.recv(1024)
                message: Message = pickle.loads(rawData)
            
                if message.getOpcode() == OPCode.probe:
                    #Not required at all here
                    pass
                
                elif message.getOpcode() == OPCode.sign_in:
                    if client.name == '':
                        client.name = message.getSourceName()
                        self.broadcastMessage(Message(OPCode.probe, "Server", "", "Client " + client.name + " has joined the chat"))
                        print("Client " + client.name + " has joined the chat")
                    else:
                        print("Client " + client.name + " is registered")
                        
                elif message.getOpcode() == OPCode.sign_out:
                    if client.name != '':
                        print("Client " + client.name + " has left the chat")
                        client.name = ''
                        self.broadcastMessage(Message(OPCode.probe, "Server", "", "Client " + client.name + " has left the chat"))
                    else:
                        print("Client " + client.name + " is not registered")

                elif message.getOpcode() == OPCode.sendMessage:
                    if client.name == '':
                        print("Dropped Message from: [" + message.getSourceName() + "] (User is not registered)")
                        continue
                    if message.getDestName() == '':
                        print("Dropped Message from: [" + message.getSourceName() + "] (No destination specified)")
                        continue
                    destClient: clientStruct = self.getClient(message.getDestName())
                    if destClient == None:
                        print("Dropped Message " + message.sourceName + " -> " + message.destName +" (End user is not registered)")
                        continue
                    self.sendMessage(message, destClient)
                    print(message.sourceName + " -> " + message.destName + ": " + message.content)
                
                elif message.getOpcode() == OPCode.broadCastMessage:
                    if client.name == '':
                        print("Dropped Message from: [" + message.getSourceName() + "] (User is not registered)")
                        continue
                    self.broadcastMessage(message)
                    print(message.sourceName + " -> @all: " + message.content)
                    
                elif OPCode.getClientList == message.getOpcode():
                    if client.name == '':
                        print("Dropped Client List Request from: [" + message.getSourceName() + "] (User is not registered)")
                        continue
                    self.sendClientList(client)
                    print("Client " + message.getSourceName() + " requested the client list")
                
            except (ConnectionAbortedError, ConnectionResetError) as e:
                if client.name != '':
                    print("Client " + client.name + " has left the chat")
                #Remove client from list
                self.clientList.remove(client)
                #End thread
                return
            except Exception as e:
                print(e)
            
            time.sleep(0.15)
    
    def listen(self):
        while not self.stop:
            try:
                newSock, addrTuple = self.sock.accept()
                newClient = clientStruct(addrTuple[0], addrTuple[1], "", newSock)
                self.clientList.append(newClient)
                print("New client connected: " + newClient.getAddress() + ":" + str(newClient.getPort()))
                threading.Thread(target=self.handleClient, args=(newClient,)).start()
            except Exception as e:
                print(e)
            time.sleep(0.15)
            
    def sendMessage(self, message: Message, client: clientStruct):
        serialzedMessage = pickle.dumps(message)
        try :
            client.sock.send(serialzedMessage)
            return True
        except Exception as e:
            return False
    
    def broadcastMessage(self, message: Message):
        for client in self.clientList:
            if client.name != '': # and client.name != message.getSourceName():
                self.sendMessage(message, client)
        return True
        
    def sendClientList(self, client: clientStruct):
        if client == None:
            return False
        message = Message(OPCode.getClientList, "Server", client.getName(), "")
        message.content = "Client List: "
        for tmp in self.clientList:
            message.content += "[" + tmp.getName() + "] "
        return self.sendMessage(message, client)
    
if __name__ == "__main__":
    current_dir = os.path.dirname(os.path.abspath(sys.argv[0]))
    parent_dir = os.path.abspath(os.path.join(current_dir, '../'))
    sys.path.append(parent_dir)
    
    #Optional ip address and port
    server:TCPServer = TCPServer("192.168.178.101")
    server.start()
    
    print("Server started on Address & Port: " + server.getHost() + " " + str(server.getPort()))
    
    while True:
        time.sleep(0.1)
    server.stop()