import socket
import struct
import threading
import re

HEADER = 64
PORT = 5050
FORMAT = "utf-8"
DISCONNECT_MESSAGE = "exit"

# IP = socket.gethostbyname(socket.gethostname())
IP = "127.0.0.1"
ADDRESS = (IP, PORT)
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(ADDRESS)

def start():
    server.listen(1)
    print("Server is starting...")
    print("ServerSocket: " + str(ADDRESS))

    while True:
        conn, addr = server.accept()

        print ("Assigning a new thread for this Client")

        thread = threading.Thread(target=handle_client, args=(conn, addr))
        thread.start()

        # print("[ACTIVE CONNECTIONS] " + str(threading.active_count() - 1))


def handle_client(conn, addr):
    print("A new Client is connected: " + str(addr))

    while True:
        msg_length = conn.recv(HEADER).decode(FORMAT)
        msg_length = int(msg_length)
        print("Client message length: " + str(addr) + ": " + str(msg_length))

        msg = ""
        if msg_length:
           msg = conn.recv(msg_length).decode(FORMAT)

        if msg == DISCONNECT_MESSAGE:
            print("A new Client is disconnected: " + str(addr))
            conn.close()
            print ("Connection closed for client: " + str(addr))
            break

        conn.send("Message received".encode(FORMAT))
        print("Response of client: " + str(addr) + ": " + msg)

start()
