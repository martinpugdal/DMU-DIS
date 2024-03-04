package Talk23V_Opgave_4B.Talk23V_Opgave_4.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader extends Thread {
    private BufferedReader reader;

    public Reader(Socket socket) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String receivedMessage = reader.readLine();

                if (receivedMessage.equalsIgnoreCase("exit")) {
                    reader.close();
                }

                System.out.println("Received: " + receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}