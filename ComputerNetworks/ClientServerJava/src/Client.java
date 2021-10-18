import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        final String DISCONNECT_MESSAGE = "exit";

        try{
            final int PORT = 5050;
            //final InetAddress IP = InetAddress.getByName("localhost");
            final InetAddress IP = InetAddress.getByName("127.0.0.1");

            Socket client = new Socket(IP, PORT);

            Scanner scanner = new Scanner(System.in);
            DataInputStream dis = new DataInputStream(client.getInputStream());
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());

            while (true){
                String msg = scanner.nextLine();
                String msg_length = String.valueOf(msg.length());

                dos.writeUTF(msg_length);
                if(!msg_length.equals("0")){
                    dos.writeUTF(msg);
                }

                if(msg.equals(DISCONNECT_MESSAGE)){
                    client.close();
                    System.out.println("Client Closed");
                    break;
                }

                System.out.println("Response of server: " + dis.readUTF());
            }

            scanner.close();
            dis.close();
            dos.close();
        }
        catch (Exception e){
            System.out.println("Client Error: " + e);
        }
    }
}
