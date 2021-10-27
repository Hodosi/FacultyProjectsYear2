import socket

BUFFER_SIZE = 1024
FORMAT = "utf-8"
DISCONNECT_MESSAGE = "exit"
START_MESSAGE = "start"

# IP = socket.gethostbyname(socket.gethostname())
IP = "127.0.0.1"
PORT = 20001
ADDRESS = (IP, PORT)
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)


def handleMessage(requirement):
    print(requirement)

    client_message = raw_input().encode(FORMAT)
    client_message += b' ' * (BUFFER_SIZE - len(client_message))
    client.sendto(client_message, ADDRESS)

    bytes_address_pair = client.recvfrom(BUFFER_SIZE)
    server_message = bytes_address_pair[0]
    server_address = bytes_address_pair[1]

    print("Server address: " + str(server_address))
    print("Server message: " + server_message.decode().strip())

    if client_message.strip() == DISCONNECT_MESSAGE:
        client.close()
        print("Client Closed")
        return False

    return True


def start():
    while True:
        if not handleMessage("Enter a message: "):
            break

        if not handleMessage("Enter other message: "):
            break


start()
