import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        final int PORT = 5050;
        final InetAddress IP = InetAddress.getByName("localhost");
        final SocketAddress socketAddress = new InetSocketAddress(IP, PORT);

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(socketAddress);

        System.out.println("Server is starting...");
        System.out.println("ServerSocket: " + serverSocket);

        while (true) {
            Socket socket = null;

            try {
                socket = serverSocket.accept();

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                System.out.println("Assigning a new thread for this Client");

                Thread t = new ClientHandler(socket, dis, dos);
                t.start();

            } catch (Exception e) {
                socket.close();
                serverSocket.close();
                System.out.println("Connection error:" + e);
            }
        }
    }
}


class ClientHandler extends Thread{
    final String DISCONNECT_MESSAGE = "exit";
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket socket;

    public ClientHandler (Socket s, DataInputStream dis, DataOutputStream dos){
        this.socket = s;
        this.dis = dis;
        this.dos = dos;
    }

    public void run(){
        System.out.println("A new Client is connected: " + socket);

        while (true) {
            try{
                String msg_length_string = dis.readUTF();
                if (!msg_length_string.equals("")) {
                    int msg_length = Integer.parseInt(msg_length_string);
                    System.out.println("Client message length: " + socket + ": " + String.valueOf(msg_length));

                    String msg = dis.readUTF();
                    if (msg.equals(DISCONNECT_MESSAGE)) {
                        System.out.println("A new Client is disconnected: " + socket);
                        this.socket.close();
                        System.out.println("Connection closed");
                        break;
                    }

                    dos.writeUTF("Message received");
                    System.out.println("Response of client: " + socket + ": " + msg);
                }
            }
            catch (Exception e){
                System.out.println("Client Handler Exception: " + e);
            }
        }

        try{
            this.dis.close();
            this.dos.close();
        }
        catch (IOException e){
            System.out.println("Closing error: " + e);
        }
    }
}
