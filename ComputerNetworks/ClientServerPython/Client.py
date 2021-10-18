import socket

HEADER = 64
PORT = 5050
FORMAT = "utf-8"
DISCONNECT_MESSAGE = "exit"

# SERVER = socket.gethostbyname(socket.gethostname())
SERVER = "127.0.0.1"
ADDR = (SERVER, PORT)
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(ADDR)

def start():
    while(True):
        msg = raw_input()

        msg = msg.encode(FORMAT)
        msg_length = str(len(msg)).encode(FORMAT)
        msg_length += b' ' * (HEADER - len(msg_length))

        client.send(msg_length)
        if msg_length:
            client.send(msg)

        if msg == DISCONNECT_MESSAGE:
            client.close()
            print("Client Closed")
            break

        print("Response of server: " + client.recv(2048).decode(FORMAT))


start()
