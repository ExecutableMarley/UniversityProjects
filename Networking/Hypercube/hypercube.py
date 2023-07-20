from bitmap import BitMap
from typing import List

# The primary advantage of Hypercubes lies in their scalability and efficient 
# routing capabilities in large-scale systems


# 32 Bit should be enough, Adjust if needed
maxBits: int = 32

def decimalToBinary(ip_val:int, bit_map: BitMap, pos:int = 0):
    if ip_val >= 1:
    # recursive function call
        decimalToBinary(ip_val // 2, bit_map, pos + 1)
    
    # printing remainder from each function call
    curBit = int(ip_val % 2)
    if curBit:
        bit_map.set(pos)
    return bit_map

class Node:
    def __init__(self, bit_map: BitMap) -> None:
        self.bit_map: BitMap = bit_map
        self.neighbours: list['Node'] = []
    
    # We mainly try to reduce the number of incorrect bits
    # Consider the following example: 1111 -> 1100
    # First 2 Bits are already correct, third is flipped so we take the third neighbor
    # This makes the routing simple while also guaranteeing an optimal path
    def route(self, destination: BitMap) -> BitMap:
        for i in range(self.bit_map.size()):
            if self.bit_map.test(i) != destination.test(i):
                print("[" + self.bit_map.tostring() + "] -> [" + self.neighbours[i].bit_map.tostring() + "]")
                return self.neighbours[i].route(destination)
        # All bits correct -> Reached destination
        print("["+self.bit_map.tostring() + "] Reached destination")
        return self.bit_map

class Hypercube:
    def __init__(self, dimensions: int) -> None:
        if dimensions < 0 or dimensions > maxBits:
            raise ValueError("Dimensions must be between 0 and 32")
        
        self.dimensions = dimensions
        self.size = 2**dimensions
        self.nodes: list[Node] = []
        
        #Creates all the nodes
        for i in range(self.size):
            self.nodes.append(Node(decimalToBinary(i, BitMap(self.dimensions))))
        #Link neighbours        
        for node in self.nodes:
            #print("Node: " + node.bit_map.tostring())
            tmpBitMap = BitMap.fromstring(node.bit_map.tostring())
            for i in range(self.dimensions):
                tmpBitMap.flip(i)
                #print("    Neighbour: " + tmpBitMap.tostring())
                node.neighbours.append(self.getNodeByBitmap(tmpBitMap))
                tmpBitMap.flip(i)
    
    def getDistance(self, source: BitMap, destination: BitMap) -> int:
        sum: int = 0
        for i in range(self.dimensions):
            if source.test(i) != destination.test(i):
                sum += 1
        return sum

    def getNodeByBitmap(self, bitmap: BitMap) -> Node:
        for node in self.nodes:
            if node.bit_map.tostring() == bitmap.tostring():
                return node
        print("Node not found")
        return None
    
    def getNodeByIndex(self, index: int) -> Node:
        return self.nodes[index]
    
    def sendMessageByBitmap(self, source: BitMap, destination: BitMap):
        self.getNodeByBitmap(source).route(destination)
    
    def sendMessageByIndex(self, startNodeIndex:int, destinationNodeIndex: int,string:str = ""):
        self.nodes[startNodeIndex].route(self.nodes[destinationNodeIndex].bit_map)

if __name__ == "__main__":
    bm = decimalToBinary(10, BitMap(10))
    print(bm)
    
    hypercube: Hypercube = Hypercube(3)
    
    hypercube.sendMessageByIndex(0, 7)
    
    hypercube2: Hypercube = Hypercube(4)
    
    startNode = BitMap.fromstring("0010")
    destNode  = BitMap.fromstring("1101")
    
    hypercube2.sendMessageByBitmap(startNode, destNode)