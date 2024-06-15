import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// En serverklasse til at styre spilforbindelser og kommunikation
public class GameServer {
    // En liste til at holde styr på klienttråde
    private static List<GameServerThread> clientThreads = new ArrayList<>();
    // Main-metode til at starte serveren
    public static void main(String[] args) throws Exception {
        // Opretter en server socket
        ServerSocket welcomeSocket = new ServerSocket(6799);

        // Lytter på forbindelser
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            // Opretter en klienttråd for hver forbindelse og tilføjer den til listen
            GameServerThread clientThread = new GameServerThread(connectionSocket);
            clientThreads.add(clientThread);
            clientThread.start();
        }
    }

    // Metode til at sende en besked til alle klienttråde
    public static synchronized void broadcastMessage(String message) {
        for (GameServerThread clientThread : clientThreads) {
            try {
                clientThread.sendMessage(message);
            } catch (IOException e) {
                System.out.println("Fejl ved udsendelse af besked til klient: " + e.getMessage());
            }
        }
    }

    // Metode til at fjerne en klienttråd fra listen
    public static void leave(GameServerThread GT){
        clientThreads.remove(GT);
    }
}