package Talk23V_Opgave_4B.Talk23V_Opgave_4;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Writer extends Thread {
    private final NameServiceClient nameServiceClient;
    private final Socket socket;
    private DataOutputStream writer;

    public Writer(Socket socket, NameServiceClient nameServiceClient) throws IOException {
        this.socket = socket;
        this.nameServiceClient = nameServiceClient;
        this.writer = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            // first thing to do is to register this user with the server
            // send the name of the user to the server
            if (nameServiceClient == null) {
                // its a client that is not using the name service
                System.out.print("Enter your name: ");
                String name = scanner.nextLine();
                writer.writeBytes(name + '\n');
            }

            while (true) {
                System.out.print("Enter message (type 'exit' to end): ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    writer.close();
                    socket.close();
                    return;
                }

                writer.writeBytes(message + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}