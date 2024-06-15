import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// En trådklasse til at håndtere kommunikation med en klient
public class GameServerThread extends Thread {
    private Socket connectionSocket;
    private DataOutputStream writer;

    // Konstruktør der opretter en tråd for en given socketforbindelse
    public GameServerThread(Socket connectionSocket)throws IOException {
        // Initialiserer forbindelsesvariabler
        this.connectionSocket = connectionSocket;
        this.writer = new DataOutputStream(connectionSocket.getOutputStream());
    }

    // Metode der eksekveres når tråden startes
    @Override
    public void run() {
        try {
            // Opretter en læser til at modtage beskeder fra klienten
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            System.out.println("Spiller forbundet til Server");

            // Lytter på beskeder fra klienten
            while (true) {
                String clientMsg = inFromClient.readLine();
                System.out.println(clientMsg);

                // Opdeler beskeden i dele
                String[] deler = clientMsg.split(" ");
                String handling = deler[0];

                // Sender beskeden videre til alle klienter
                GameServer.broadcastMessage(clientMsg);

                // Håndterer LEAVE handlingen, hvis modtaget
                if(deler[0].equals("LEAVE")){
                    GameServer.leave(this);
                    connectionSocket.close();
                }

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Metode til at sende en besked til klienten
    public void sendMessage(String message) throws IOException {
        writer.writeBytes(message + '\n');
    }
}
