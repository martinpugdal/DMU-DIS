package Talk23V_Opgave_2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class TCPClient {

	public static void main(String[] args) throws Exception, IOException {
		
		String sentence;
		String receivedSentence;
		Socket clientSocket = new Socket("localhost", 6789);
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

		while (true) {
			System.out.println("Skriv en sætning");

			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + '\n');
			receivedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + receivedSentence);
		}
	}
}