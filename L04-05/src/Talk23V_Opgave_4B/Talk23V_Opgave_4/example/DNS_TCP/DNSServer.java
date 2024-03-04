package Talk23V_Opgave_4B.Talk23V_Opgave_4.example.DNS_TCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class DNSServer {

    public static void main(String[] args) throws Exception {

        IpLookUp ipLookUp = new IpLookUp();

        ServerSocket DNSSocket = new ServerSocket(8889);
        System.out.println("Serveren venter på klienter...");


        while (true) {
            Socket connectionSocket = DNSSocket.accept();
            System.out.println("Klient forbundet til Server");

            Thread clientThread = new Thread(new DNSTread(connectionSocket, ipLookUp));
            clientThread.start();
        }
    }
}
