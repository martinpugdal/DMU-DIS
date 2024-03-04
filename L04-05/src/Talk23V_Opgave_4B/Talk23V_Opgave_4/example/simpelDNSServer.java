package Talk23V_Opgave_4B.Talk23V_Opgave_4.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class simpelDNSServer {
    public static void main(String[] args) throws Exception {

        String clientRequst;
        String ipResons;
        ServerSocket DNSSocket = new ServerSocket(9876);
        System.out.println("Serveren venter på klient");
        Socket connectionSocket = DNSSocket.accept();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        System.out.println("Klient forbundet til Server");

        clientRequst = inFromClient.readLine();
        System.out.println(clientRequst);
        ipResons = ipOpslag(clientRequst);
        outToClient.writeBytes(ipResons);

    }
    private static String ipOpslag(String domainName){
        switch (domainName.toLowerCase()) {
            case "mark":
                return "93.184.216.34";
            case "nicolaj":
                return "172.217.23.46";
            case "marlene":
                return "localhost";
            default:
                return "Kender ikke domain navnet";

        }
    }
}
