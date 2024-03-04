package Talk23V_Opgave_4B.Talk23V_Opgave_4.example;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {

	public static void main(String[] args) {
		try {
			String domainName = "10.10.131.113";
			String ip;

//			UDP
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			DatagramSocket UDPSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(domainName != null ? domainName : "localhost");
			byte[] receiveData = new byte[InetAddress.getLocalHost().getHostAddress().length()];
			String sentence = inFromUser.readLine();
			byte[] sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, 8889);
			UDPSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			UDPSocket.receive(receivePacket);

			ip = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + ip);

			InetAddress serverIp = InetAddress.getByName(ip);
			System.out.println("FROM SERVER:" + serverIp);
			UDPSocket.close();

			//tcp
			Socket clientSocket = new Socket(serverIp, 8889);
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