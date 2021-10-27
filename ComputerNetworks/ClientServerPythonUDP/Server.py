import socket

BUFFER_SIZE = 1024
FORMAT = "utf-8"
DISCONNECT_MESSAGE = "exit"

# IP = socket.gethostbyname(socket.gethostname())
IP = "127.0.0.1"
PORT = 20001
ADDRESS = (IP, PORT)
server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server.bind(ADDRESS)


def start():
    print("Server is starting...")
    print("ServerSocket: " + str(ADDRESS))

    while True:
        bytes_address_pair = server.recvfrom(BUFFER_SIZE)
        client_message = bytes_address_pair[0]
        client_address = bytes_address_pair[1]

        print("Client address: " + str(client_address))
        print("Client message: " + client_message.decode().strip())

        server_response = "Message received"
        server_response += b' ' * (BUFFER_SIZE - len(server_response))
        server.sendto(server_response, client_address)


start()
