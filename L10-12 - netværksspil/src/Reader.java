import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// En trådklasse til at læse og behandle indkommende beskeder fra serveren
public class Reader extends Thread {
    private BufferedReader reader; // Reader til at læse beskeder fra serveren
    private GUI gui; // Reference til GUI'en for at kunne opdatere interfacet

    // Konstruktør der opretter en læser for en given socketforbindelse og en GUI-reference
    public Reader(Socket socket, GUI gui) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.gui = gui;
    }

    // Metode der eksekveres når tråden startes
    @Override
    public void run() {
        try {
            // Lytter på beskeder fra serveren
            while (true) {
                String receivedMessage = reader.readLine();
                if (receivedMessage == null) {
                    break;
                }
                System.out.println("Received: " + receivedMessage);

                // Opdeler beskeden i dele
                String[] deler = receivedMessage.split(" ");

                // Ekstraherer information fra beskeden
                String handling = deler[0];
                String playerName = deler[1];
                int posX = Integer.parseInt(deler[2]);
                int posY = Integer.parseInt(deler[3]);
                String retning = deler[4];
                int points = Integer.parseInt(deler[5]);

                // Udskriver informationen til konsollen
                System.out.println("Spiller: " + playerName + " handling: " + handling);
                System.out.println("Position: (" + posX + ", " + posY + ") Retning: " + retning);

                // Udfører handling baseret på beskeden
                Player player = gui.getPlayer(playerName, posX, posY, retning);
                if (player.point != points) {
                    gui.playerPoints(player, points);
                }
                if (handling.equals("MOVE")) {
                    switch (retning) {
                        case "up":
                            gui.playerMoved(0, -1, "up", player);
                            break;
                        case "down":
                            gui.playerMoved(0, +1, "down", player);
                            break;
                        case "left":
                            gui.playerMoved(-1, 0, "left", player);
                            break;
                        case "right":
                            gui.playerMoved(+1, 0, "right", player);
                            break;
                        case "ud":
                            gui.playerMoved(0, 0, "ud", player);
                            gui.leave(playerName);
                            break;
                        case "ind":
                            gui.playerMoved(0, 0, "Ind", player);
                            break;
                        default:
                            break;
                    }
                } else if (handling.equals("PISTOL")) {
                    gui.playerShoot(retning, player);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}