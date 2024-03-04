package Talk23V_Opgave_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        try {
            String sentence;
            String modifiedSentence;

            Socket clientSocket = new Socket("localhost", 6789);


            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);

            System.out.println("Indtast en besked:");

            sentence = inFromUser.readLine();
            outToServer.println(sentence);

            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

