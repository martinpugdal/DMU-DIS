package Talk23V_Opgave_4B.Talk23V_Opgave_4.example;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            System.out.println("Serveren venter på klient");

            while (true) {
                Socket connectionSocket = serverSocket.accept();
                System.out.println("Talk23V Server connected to a client");

                Reader readerThread = new Reader(connectionSocket);
                Writer writerThread = new Writer(connectionSocket);

                readerThread.start();
                writerThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}