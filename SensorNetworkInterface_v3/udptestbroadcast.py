# Simulates broadcasted ZigBee module data on UDP port 5000
# Data format: [timestamp] [ZigBee address] [channel name] [value]

import sys
import socket
import time
import random

# The ZigBee module device MAC addresses
macs = [ 
  '[00:13:a2:00:40:32:de:87]!', #Kitchen
  #'[00:13:a2:00:40:62:a9:52]!', #Bathroom
  #'[00:13:a2:00:40:62:a9:4f]!', #Bedroom
  '[00:13:a2:00:40:62:a9:4d]!', #Dining Area
  '[00:13:a2:00:40:62:a9:4e]!'  #Living Room
]

# Our main function called at program start
def main(args):

    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.bind(('', 0))
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

    while True:
        try:
            for mac in macs:
                handle_sample(mac, sock)
            time.sleep(5)
        except socket.timeout:
            pass

def tx_sample(sock, msg):
    sock.sendto(msg, ('<broadcast>',5000))
    print msg
    
# Method called when a sample is received from the network
def handle_sample(node_addr, sock):
    mtime = time.time()
    
    for pin in [ 'AD0', 'AD1', 'AD2', 'AD3', 'SUPPLY' ]:
        supply = random.uniform(0, 1.215)
    
        if random.randint(0, 1) == 1:
            tx_sample(sock, "%d %s %s %3.3f" % (mtime, node_addr, pin, supply,))

    for pin in [ 'DIO0', 'DIO1', 'DIO2', 'DIO3', 'DIO4', 'DIO5', 'DIO6', 'DIO7', 'DIO10', 'DIO11', 'DIO12' ]:
        supply = random.randint(0, 1)
    
        if random.randint(0, 1) == 1:
            tx_sample(sock, "%d %s %s %d" % (mtime, node_addr, pin, supply,))

# Check to see if we were started from the
# command prompt, and if so, call the main
# function.
if __name__ == '__main__':
    rc = main(sys.argv)
    sys.exit(rc)

