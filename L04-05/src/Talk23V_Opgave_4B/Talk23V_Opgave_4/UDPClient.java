package Talk23V_Opgave_4B.Talk23V_Opgave_4;

import java.net.DatagramSocket;
import java.net.Socket;

public class UDPClient {

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