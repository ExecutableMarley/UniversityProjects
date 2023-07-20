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
    def __init__(self, address:str, port:int, name:str) -> None:
        self.address :str = address
        self.port    :int = port
        self.name    :str = name
        
    def getAddress(self) -> str:
        return self.address
    
    def getPort(self) -> int:
        return self.port
    
    def getName(self) -> str:
        return self.name
    
    def __eq__(self, __value: 'clientStruct') -> bool:
        return __value != None and self.name == __value.name #and self.address == __value.address

class UDPServer:
    def __init__(self, host = socket.gethostname(), port = 0) -> None:
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sock.bind((host, port)) #socket.gethostname()
        self.host = self.sock.getsockname()[0]
        self.port = self.sock.getsockname()[1]
        self.clients = []
        self.stop = False
        self.thread : threading.Thread = None
    
    def start(self) -> bool:
        if self.isRunning():
            return False
        self.thread = threading.Thread(target=self.listen, daemon=True)
        self.thread.start()
    
    def stop(self) -> bool:
        if not self.isRunning():
            return False
        self.stop = True
        self.thread.join()
        return True
    
    def isRunning(self) -> bool:
        return self.thread != None
    
    def getHost(self) -> str:
        return self.host
    
    def getPort(self) -> int:
        return self.port
    
    def getClient(self, name : str) -> clientStruct:
        for client in self.clients:
            if client.getName() == name:
                return client
        return None
    
    def removeClient(self, name : str) -> bool:
        client = self.getClient(name)
        if client == None:
            return False
        self.clients.remove(client)
        return True
    
    def listen(self):
        if self.thread == None or self.thread != threading.current_thread():
            return
        
        while not self.stop:
            try:
                rawData, addrTupple  = self.sock.recvfrom(1024)
                
                message: Message = pickle.loads(rawData)
                
                #print("Message received: " + message.getContent())
                
                if OPCode.probe == message.getOpcode():
                    self.sock.sendto(pickle.dumps(Message(OPCode.probe, '', '', '')), addrTupple)
                    print("Probe received from " + addrTupple[0] + " " + str(addrTupple[1]))
                
                elif OPCode.sign_in == message.getOpcode():
                    # Check if the client is already in the list
                    if self.getClient(message.getSourceName) == None:
                        self.clients.append(clientStruct(address=addrTupple[0], port=addrTupple[1], name=message.getSourceName()))
                        # Send a message to all clients that a new client has joined
                        self.broadcastMessage(Message(OPCode.broadCastMessage, "Server", "", message.getSourceName() + " has joined the chat"))
                        print("Client " + message.getSourceName() + " has joined the chat")
                    else:
                        print("Client " + message.getSourceName() + " is already registered")
                    
                elif OPCode.sign_out == message.getOpcode():
                    # Remove the client from the list
                    if self.removeClient(message.getSourceName()):
                        # Send a message to all clients that a client has left
                        self.broadcastMessage(Message(OPCode.broadCastMessage, "Server", "", message.getSourceName() + " has left the chat"))
                        print("Client " + message.getSourceName() + " has left the chat")
                    else: 
                        print("Client " + message.getSourceName() + " is not registered")
                    
                elif OPCode.sendMessage == message.getOpcode():
                    if self.getClient(message.sourceName) == None:
                        print("Dropped Message from: [" + message.getSourceName() + "] (User is not registered)")
                        continue
                    client: clientStruct = self.getClient(message.getDestName())
                    if client == None:
                        print("Dropped Message " + message.sourceName + " -> " + message.destName +" (End user is not registered)")
                        continue
                    #Deliver Message
                    self.sendMessage(message, client)
                    print(message.sourceName + " -> " + message.destName + ": " + message.content)
                    
                elif OPCode.broadCastMessage == message.getOpcode():
                    if self.getClient(message.sourceName) == None:
                        print("Dropped Message from: [" + message.getSourceName() + "] (User is not registered)")
                        continue
                    #Deliver Message
                    self.broadcastMessage(message)
                    print(message.sourceName + " -> " + "@all" + ": " + message.content)
                
                elif OPCode.getClientList == message.getOpcode():
                    self.sendClientList(self.getClient(message.getSourceName()))
                    print("Client " + message.getSourceName() + " requested the client list")
                
                else:
                    print("Message not recognized " + message)
                    pass
                
            except socket.timeout:
                pass
            except ConnectionResetError as e:
                print(e)
            time.sleep(0.01)
    
    def sendMessage(self, message : Message, client : clientStruct) -> bool:
        serializedData = pickle.dumps(message)
        try:
            self.sock.sendto(serializedData, (client.getAddress(), client.getPort()))
            return True
        except:
            return False
    
    def broadcastMessage(self, message : Message) -> bool:
        for client in self.clients:
            self.sendMessage(message, client)
        return True
    
    def sendClientList(self, client : clientStruct) -> bool:
        if client == None:
            return False
        message = Message(OPCode.getClientList, "Server", client.getName(), "")
        message.content = "Client List: "
        for tmp in self.clients:
            message.content += "[" + tmp.getName() + "] "
        return self.sendMessage(message, client)
    
    
    

if __name__ == "__main__":
    current_dir = os.path.dirname(os.path.abspath(sys.argv[0]))
    parent_dir = os.path.abspath(os.path.join(current_dir, '../'))
    sys.path.append(parent_dir)
    
    #Optional ip address and port
    server:UDPServer = UDPServer("192.168.178.101")
    server.start()
    
    print("Server started on Address & Port: " + server.getHost() + " " + str(server.getPort()))
    
    while True:
        time.sleep(0.1)
    server.stop()