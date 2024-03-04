package Talk23V_Opgave_4B.Talk23V_Opgave_4;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader extends Thread {

    private final Socket socket;
    private final NameServiceClient nameServiceClient;
    private BufferedReader reader;

    public Reader(Socket socket, NameServiceClient nameServiceClient) throws IOException {
        this.socket = socket;
        this.nameServiceClient = nameServiceClient;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String username = null;
            if (nameServiceClient != null) {
                username = reader.readLine();
                if (nameServiceClient.userExists(username)) {
                    socket.getOutputStream().write("Username already exists".getBytes());
                    // close this connection and remove the user from the nameService
                    socket.close();
                    nameServiceClient.removeUser(username);
                } else {
                    nameServiceClient.addUser(username, socket);
                    System.out.println("User " + username + " added");
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                    writer.writeBytes("User " + username + " added\n");
                    writer.flush();
                }
            }

            while (true) {
                String receivedMessage;
                try {
                    receivedMessage = reader.readLine();
                } catch (IOException e) {
                    continue;
                }

                if (receivedMessage == null || receivedMessage.equalsIgnoreCase("exit")) {
                    reader.close();
                    if (username != null) {
                        nameServiceClient.removeUser(socket.getInetAddress().getHostAddress());
                    }
                } else if (receivedMessage.equalsIgnoreCase("getusers")) {
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                    writer.writeBytes("Connected users:"+nameServiceClient.getUsers().toString() + '\n');
                    writer.flush();
                    continue;
                }

                if (nameServiceClient != null) {
                    System.out.println(nameServiceClient.getUser(socket) + ": " + receivedMessage);
                    for (Socket s : nameServiceClient.getSockets()) {
                        if (s.isClosed()) {
                            nameServiceClient.removeUser(s.getInetAddress().getHostAddress());
                            continue;
                        }
                        if (s != socket) {
                            DataOutputStream writer = new DataOutputStream(s.getOutputStream());
                            writer.writeBytes(nameServiceClient.getUser(socket) + ": " + receivedMessage + '\n');
                            writer.flush();
                        }
                    }
                } else {
                    System.out.println("Received: " + receivedMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}