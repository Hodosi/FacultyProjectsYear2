import socket
import threading
import random
import time

HEADER = 64
PORT = 5050
FORMAT = "utf-8"
DISCONNECT_MESSAGE = "exit"

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

        print("Active connections: " + str(threading.active_count() - 1))


def handle_client(conn, addr):
    print("A new Client is connected: " + str(addr))

    n = conn.recv(HEADER).decode(FORMAT)
    n = int(n)

    matrix = [[0 for i in range(n)] for j in range(n)]
    print(matrix)

    cpy_n = n
    while cpy_n:
        x = random.randint(0, n - 1)
        y = random.randint(0, n - 1)
        if matrix[x][y] == 1:
            continue

        matrix[x][y] = 1
        cpy_n -= 1

    print(matrix)

    print("Game start for client: " + str(addr) + " with n = " + str(n))
    conn.send("Start Game".encode(FORMAT))

    nr_mistakes = 0
    nr_non_bombed = n
    while True:
        x = conn.recv(HEADER).decode(FORMAT)
        x = int(x)
        print("x: " + str(x))

        y = conn.recv(HEADER).decode(FORMAT)
        y = int(y)
        print("y: " + str(y))

        msg = ""
        if matrix[x][y] == 1:
            msg += "YES"
            nr_non_bombed -= 1
            matrix[x][y] = 0
        else:
            msg += "NO"
            nr_mistakes += 1

        msg += " " + str(nr_mistakes) + " " + str(nr_non_bombed)
        conn.send(msg.encode(FORMAT))
        time.sleep(1)

        if nr_non_bombed == 0:
            conn.send("You Win".encode(FORMAT))
        elif nr_mistakes >= 5:
            conn.send("You Lose".encode(FORMAT))
        else:
            conn.send("Continue".encode(FORMAT))

        if nr_non_bombed == 0 or nr_mistakes >= 5:
            print("A new Client is disconnected: " + str(addr))
            conn.close()
            print ("Connection closed for client: " + str(addr))
            break


start()
