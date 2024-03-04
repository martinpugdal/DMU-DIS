package Talk23V_Opgave_4B.Talk23V_Opgave_4.example.DNS_UDP;

import java.net.*;


public class DNSServer {

    public static void main(String[] args) {
        IpLookUp ipLookUp = new IpLookUp();

        try {
            DatagramSocket dnsSocket = new DatagramSocket(8889);
            System.out.println("Serveren venter på klienter...");

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                dnsSocket.receive(receivePacket);

                String clientRequest = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Klient forespørgsel: " + clientRequest);

                String ipResponse = ipLookUp.ipLookUp(clientRequest);
                InetAddress clientAddress = InetAddress.getByName(new String(receiveData));
                byte[] sendData = ipResponse.getBytes();
                int port = receivePacket.getPort();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, port);

                dnsSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
