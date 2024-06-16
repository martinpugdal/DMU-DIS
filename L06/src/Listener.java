import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Listener extends Thread {

    public static void main(String[] args) {
        Listener listener = new Listener();
        listener.start();
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(8888);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received broadcast message: " + receivedMessage);

                InetAddress senderAddress = packet.getAddress();
                int senderPort = packet.getPort();

                String responseMessage = "Hello, I am here!";
                byte[] responseBuffer = responseMessage.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length, senderAddress, senderPort);
                socket.send(responsePacket);
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
