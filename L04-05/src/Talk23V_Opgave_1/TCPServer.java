package Talk23V_Opgave_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        try {
            String clientSentence;
            String capitalizedSentence;

            ServerSocket welcomeSocket = new ServerSocket(6789);
            System.out.println("Serveren venter på klient");

            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream(), true);

            System.out.println("Klient forbundet til Server");

            clientSentence = inFromClient.readLine();
            System.out.println("Received from client: " + clientSentence);

            capitalizedSentence = clientSentence.toUpperCase();
            outToClient.println(capitalizedSentence);

            connectionSocket.close();
            welcomeSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

