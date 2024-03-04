package Talk23V_Opgave_3;

import java.net.Socket;

public class TCPClient {

	public static void main(String[] args) {
		try {
			Socket clientSocket = new Socket("localhost", 6789);

			Reader readerThread = new Reader(clientSocket);
			Writer writerThread = new Writer(clientSocket);

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