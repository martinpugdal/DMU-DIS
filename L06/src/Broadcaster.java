import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Broadcaster {

    public static void main(String[] args) {
        Broadcaster broadcaster = new Broadcaster();
        broadcaster.udsendsBroadcast("Hello, is anyone there?");
    }

    public void udsendsBroadcast(String message) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            socket.setSoTimeout(2000);

            byte[] buffer = message.getBytes();
            InetAddress IPAddress = InetAddress.getByName("255.255.255.255");

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, IPAddress, 8888);

            boolean receivedResponse = false;

            for (int i = 0; i < 3 && !receivedResponse; i++) {
                socket.send(packet);
                try {
                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    socket.receive(receivePacket);
                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Received response: " + response);
                    receivedResponse = true;
                } catch (SocketTimeoutException e) {
                    System.out.println("No response, retrying... (" + (i + 1) + "/3)");
                }
            }

            if (!receivedResponse) {
                System.out.println("No response received after 3 attempts.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}

