package Talk23V_Opgave_4;

import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TCPClient {

	public static void main(String[] args) {
		try {
			Socket clientSocket = new Socket("localhost", 6789);

			Reader readerThread = new Reader(clientSocket, null);
			Writer writerThread = new Writer(clientSocket, null);

			readerThread.start();
			writerThread.start();

			readerThread.join();
			writerThread.join();

			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}