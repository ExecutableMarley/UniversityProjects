import socket
import time
import pickle
import threading

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

class Client:
    def __init__(self, name: str, serverAddr:str, serverPort:int) -> None:
        self.name:      str = name
        self.serverAddr:str = serverAddr
        self.serverPort:int = serverPort
        
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sock.bind(('', 0))
        
        self.messageHandler = None
        
        self.thread = threading.Thread(target=self.listen, daemon=True)
        self.thread.start()

    
    def send(self, message: Message) -> None:
        self.sock.sendto(pickle.dumps(message), (self.serverAddr, self.serverPort))
    
    def signIn(self) -> None:
        self.send(Message(OPCode.sign_in, self.name, 'Server', 'Sign in Request'))
    
    def signOut(self) -> None:
        self.send(Message(OPCode.sign_out, self.name, 'Server', 'Sign out Request'))
    
    def sendMessage(self, destName:str, content:str) -> None:
        self.send(Message(OPCode.sendMessage, self.name, destName, content))
    
    def broadCastMessage(self, content:str) -> None:
        self.send(Message(OPCode.broadCastMessage, self.name, '', content))
    
    def getClientList(self) -> None:
        self.send(Message(OPCode.getClientList, self.name, 'Server', 'Client List Request'))
    
    def __del__(self) -> None:
        self.signOut()
        self.sock.close()
    
    def listen(self) -> None:
        if self.thread == None or self.thread != threading.current_thread():
            return
        
        while True:
            try:
                data, addrTuple = self.sock.recvfrom(1024)
                message: Message = pickle.loads(data)
                
                if self.messageHandler != None:
                    self.messageHandler(message)
                
            except ConnectionResetError as e:
                print(e)
            
            time.sleep(0.01)
    
    def setMessageHandler(self, messageHandler) -> None:
        self.messageHandler = messageHandler
    
    def probeServer(ip:str, port:int) -> bool:
        tmpSock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        tmpSock.bind(('', 0))
        tmpSock.settimeout(10)
        
        try:
            tmpSock.sendto(pickle.dumps(Message(OPCode.probe, 'TmpClient', 'Server', 'Server Probe')), (ip, port))
            data, addrTuple = tmpSock.recvfrom(1024)
            message = pickle.loads(data)
            #Todo: Check if ip and port are the same
            return message.getOpcode() == OPCode.probe
        except socket.timeout:
            return False
        except:
            return False
        
import re

def parse_console_input(console_input):
    pattern = r'@(\w+)'  # Matches the @ symbol followed by word characters (\w+)
    matches = re.findall(pattern, console_input)

    # Separate the operations from the rest of the text
    operations = [match for match in matches]
    message_text = re.sub(pattern, '', console_input).strip()

    return operations, message_text    
    
def performOperations(client: Client, operations: List[str], message: str) -> None:
    if len(operations) == 0:
        print('Warning: No operation specified.')
        return
    
    for operation in operations:
        if operation == 'signin':
            client.signIn()
        elif operation == 'signout':
            client.signOut()
        elif operation == 'all':
            client.broadCastMessage(message)
        elif operation == 'getclients':
            client.getClientList()
        else:
            client.sendMessage(operation, message)
        
if __name__ == '__main__':
    print('Starting new client...')
    
    print('Enter client name: ', end='')
    name = input()
    
    print('Enter Server IP and Port (<ip> <port>): ', end='')
    while True:
        try:
            ip, port = input().split()
            if not Client.probeServer(ip, int(port)):
                raise Exception()
            break
        except:
            print('Invalid server address.\n Enter Server IP and Port (<ip> <port>): ', end='')
    
    client = Client(name, ip, int(port))
    
    client.setMessageHandler(lambda message: print( message.getSourceName() + ": " + message.getContent()))
    client.signIn()
    
    print("Connected to Server!.")
    
    while True:
        
        command:str = input()
        
        #print(command)
        operations, message = parse_console_input(command)
        performOperations(client, operations, message)
        
        time.sleep(1)