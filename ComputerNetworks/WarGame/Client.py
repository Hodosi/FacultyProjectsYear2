import socket

HEADER = 64
PORT = 5050
FORMAT = "utf-8"
DISCONNECT_MESSAGE = "exit"

SERVER = "127.0.0.1"
ADDR = (SERVER, PORT)
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(ADDR)


def start():
    print ("Give n: ")
    n = raw_input()
    n = n.encode(FORMAT)
    n += b' ' * (HEADER - len(n))
    client.send(n)
    print("Response of server: " + client.recv(2048).decode(FORMAT))

    while True:

        print ("Give x: ")
        x = raw_input()
        x = x.encode(FORMAT)
        x += b' ' * (HEADER - len(x))
        client.send(x)

        print("Give y: ")
        y = raw_input()
        y = y.encode(FORMAT)
        y += b' ' * (HEADER - len(y))
        client.send(y)

        print("Response of server: " + client.recv(2048).decode(FORMAT))

        msg = client.recv(2048).decode(FORMAT)
        if msg == "Continue":
            continue
        else:
            print(msg)
            client.close()
            print("Client Closed")
            break


start()
