package Talk23V_Opgave_4B.Talk23V_Opgave_4;

import java.net.ServerSocket;
import java.net.Socket;

public class UDPServer {

    public static void main(String[] args) {
        try {
            NameServiceClient nameServiceClient = new NameServiceClient();
            ServerSocket serverSocket = new ServerSocket(6789);
            System.out.println("Serveren venter på klient");

            while (true) {
                // check if a client is trying to connect with a username
                Socket connectionSocket = serverSocket.accept();
                if (connectionSocket.isConnected()) {
                    /*System.out.println("Talk23V Server connected to a client");
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    String username = inFromClient.readLine();
                    nameServiceClient.addUser(username);
                    System.out.println("User " + "username" + " connected to the server");*/

                    Reader readerThread = new Reader(connectionSocket, nameServiceClient);
                    Writer writerThread = new Writer(connectionSocket, nameServiceClient);

                    readerThread.start();
                    writerThread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}