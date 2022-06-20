'''
        @Author: Marley Arns
        @github: https://github.com/ExecutableMarley
    This is neither pretty nor efficient, but it works. :)
'''

from ctypes.wintypes import BYTE
from dataclasses import dataclass
import socket
import math

def access_bit(data, num):
    base = int(num // 8)
    shift = int(num % 8)
    return (data[base] >> shift) & 0x1

def makeBitArray(data):
    return [access_bit(data,i) for i in range(len(data)*8)]

def getIntFromBytes(bytes):
    return int.from_bytes(bytes, byteorder='big', signed=False)

#Overflows if we add more than 8 bits
class BitHelper:
    def __init__(self):
        self.bits = []
    def append(self, *bits : int):
        for bit in bits:
            self.bits.append(int(bit))
    def getByte(self) -> BYTE:
        byte : BYTE = 0
        for i in range(len(self.bits)):
            byte +=  int(math.pow(2, i - 1)) * int(self.bits[-i])
        return byte

@dataclass
class DnsRecord:
    ID = 666          #0x00 Transaction Identifier 
    QR : bool    = 0  #0x04 Query: False, Response True
    OPCODE : int = 0  #0x04 Standard Query: 0
    AA : bool    = 0  #0x04 Authoritative?
    TC : bool    = 0  #0x04 Truncated: False, True
    RD : bool    = 1  #0x04 Recursion Desired: False, True
    RA : bool    = 0  #0x05 Recursion Available: False, True
    Z1 : bool    = 0  #0x05 ?
    Z2 : bool    = 0  #0x05 ?
    Z3 : bool    = 0  #0x05 ?
    RCODE : int  = 0  #0x05 
    
    QDCOUNT : int = 0 #Number of questions
    ANCOUNT : int = 0 #Number of answers
    NSCOUNT : int = 0 #Number of authority records
    ARCOUNT : int = 0 #Number of additional records
    
    QNAME : str = "google.com" # Name of the host to lookup
    
    QTYPE : str = "A" #AAAA is 28. A is 1
    
    QClass : int = 1
    
    TTL : int = 0
    
    #Encodes the DNS packet
    def __bytes__(self):
        data = bytearray()
        data += self.ID.to_bytes(2, 'big')
        
        bits = BitHelper()
        bits.append(self.QR, 0,0,0,0, self.AA, self.TC, self.RD)
        data += BYTE(bits.getByte())
        
        bits = BitHelper()
        bits.append(self.RA, self.Z1, self.Z2, self.Z3, 0,0,0,0)
        data += BYTE(bits.getByte())
        
        # Counts (One Question)
        data += b"\x00\x01\x00\x00\x00\x00\x00\x00"
        
        ##QNAME
        nameParts = self.QNAME.split('.')
        for name in nameParts:
            data += len(name).to_bytes(1, 'big')
            data += name.encode('utf-8')
        data += b'\x00'
        
        #QType
        #AAAA is 28. A is 1
        if self.QTYPE == "A":
            data += b"\x00\x01"
        else:
            data += b"\x00\x1C"
        
        #QClass. 1 is IN
        data += b"\x00\x01"
        
        return data

    #Decodes the DNS packet
    def decode(self, bytes):
        if bytes is None or len(bytes) < 12:
            print("Invalid DNS packet")
            return
        
        ID = int.from_bytes(bytes[0:2], 'big')
        
        #Parse Header
        HeaderBits = makeBitArray(bytes[2:4])
        self.QR = HeaderBits[0]
        self.OpCode = getIntFromBytes(HeaderBits[1:5])
        self.AA = HeaderBits[5]
        self.TC = HeaderBits[6]
        self.RD = HeaderBits[7]
        self.RA = HeaderBits[8]
        #Z Not needed
        RCODE = getIntFromBytes(HeaderBits[12:16])
        
        #Counts
        self.QDCOUNT = getIntFromBytes(bytes[4  : 6])
        self.ANCOUNT = getIntFromBytes(bytes[6  : 8])
        self.NSCOUNT = getIntFromBytes(bytes[8  : 10])
        self.ARCOUNT = getIntFromBytes(bytes[10 : 12])
        
        #Sometimes server returns a packet with only the header
        if not self.QDCOUNT and not self.ANCOUNT and not self.NSCOUNT and not self.ARCOUNT:
            return
        
        byteCursor = 12
        
        #QNAME
        nameParts = []
        end = 0
        while bytes[byteCursor] != 0:
            end = bytes[byteCursor]
            nameParts.append(bytes[byteCursor + 1: byteCursor + end + 1].decode('utf-8'))
            byteCursor += end + 1
        self.QNAME = '.'.join(nameParts)
        
        byteCursor += 1
        
        # Type
        iType = int.from_bytes(bytes[byteCursor : byteCursor + 2], signed=False, byteorder="big")
        self.QTYPE = 'A' if iType == 1 else 'AAAA'
        byteCursor += 2
        
        #Class
        self.QClass = int.from_bytes(bytes[byteCursor : byteCursor + 2], signed=False, byteorder="big")
        byteCursor += 2
        
        byteCursor += 4
        
        for i in range(self.ANCOUNT):
            # TTL
            self.TTL = int.from_bytes(bytes[byteCursor + 2 : byteCursor + 6], signed=False, byteorder="big")
            byteCursor += 6
        
            #RDLENGTH (Length of Ipv4 or Ipv6)
            RDLENGTH = int.from_bytes(bytes[byteCursor : byteCursor + 2], signed=False, byteorder="big")
            byteCursor += 2
            
            address_list = []
            address = ""
            if self.QTYPE == 'A':
                for n in bytes[byteCursor:byteCursor + RDLENGTH]:
                    address_list.append(str(n))
                address = '.'.join(address_list)
            else:
                idx = 0
                for _ in range(int(RDLENGTH / 2)):
                    address_list.append(bytes[byteCursor + idx:byteCursor + idx + 2].hex())
                    idx += 2
                address = ':'.join(address_list)
            #print("Testing: " + address)
            
            #Check if server supports iterative
            if self.RA:
                #Check if server is a dns server
                if checkIfDnsServer(address):
                    result = recursiveDnsLookup(self.QNAME, self.QTYPE, address)
                    if result is not None:
                        return result
                else:
                    return address
            else:
                return address
            
            
            #if not self.RA or not checkIfDnsServer(address):
            byteCursor += RDLENGTH * 2
        #print(socket.inet_ntop(socket.AF_INET6, bytes[-16:]))
        #return socket.inet_ntoa(bytes[-4:])
#Returns true if server is a dns server
def checkIfDnsServer(address):
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(1)
        s.connect((address, 53))
        s.close()
        return True
    except:
        return False

def sendUDP(host, port, data : bytearray):
    #print("Data: ", bytes(data))
    
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.settimeout(3)
    sock.sendto(data, (host, port))
    #sock.sendto(b'\xaa\xaa\x01\x00\x00\x01\x00\x00\x00\x00\x00\x00\x06github\x03com\x00\x00\x01\x00\x01', ("1.1.1.1", 53))
    try :
        data, addr = sock.recvfrom(4096)
        #print("Data Received: ", data, addr)
        return data
    except socket.timeout:
        print("Timeout")
        return None
    finally:
        sock.close()

#Both Functions will handle recursive and iterative dns lookups
#We have to do it this way since some servers ignore the rd flag 
#And return a recursive request even if we requested an iterative 

def recursiveDnsLookup(host, requestType = "A" , dnsServer = "1.1.1.1", port=53):
    dnsRecord = DnsRecord()
    dnsRecord.QNAME = host
    dnsRecord.RD = 1
    dnsRecord.QTYPE = requestType
    
    result = sendUDP(dnsServer, port, dnsRecord.__bytes__())
    
    dnsRecord : DnsRecord = DnsRecord()
    return dnsRecord.decode(result)

def iterativeDnsLookup(host, requestType = "A", dnsServer = "1.1.1.1", port=53):
    dnsRecord = DnsRecord()
    dnsRecord.QNAME = host
    dnsRecord.RD = 0
    dnsRecord.QTYPE = requestType
    
    result = sendUDP(dnsServer, port, dnsRecord.__bytes__())
    
    dnsRecord : DnsRecord = DnsRecord()
    return dnsRecord.decode(result)
    

# Domain Name | Request Type | DNS Server | Port

print(recursiveDnsLookup("google.com", "A", "9.9.9.9", 53))

print(iterativeDnsLookup("google.com", "A", "38.132.106.139", 53))

print(recursiveDnsLookup("google.com", "AAAA", "38.132.106.139", 53))

print(iterativeDnsLookup("google.com", "AAAA", "38.132.106.139", 53))

#Common Dns Servers:
#(Please note that some dns server wont support iterative lookups while some will simply return a recursive request)
# Google Public DNS	8.8.8.8
# Cloudflare	    1.1.1.1
# OpenDNS	        208.67.222.222
# CyberGhost	    38.132.106.139
# Quad9	            9.9.9.9
