package Talk23V_Opgave_4B.Talk23V_Opgave_4.example;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Writer extends Thread {
    private DataOutputStream writer;

    public Writer(Socket socket) throws IOException {
        this.writer = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter message): ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    writer.close();
                }

                writer.writeBytes(message + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}