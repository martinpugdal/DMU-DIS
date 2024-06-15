import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

// GUI-klassen er ansvarlig for at oprette og styre brugergrænsefladen
public class GUI extends Application {

	// Størrelser af felter og scene
	public static final int size = 20;
	public static final int scene_height = size * 20 + 100;
	public static final int scene_width = size * 20 + 200;

	// Billeder til visning af forskellige elementer i spillet
	public static Image image_floor;
	public static Image image_wall;
	public static Image hero_right, hero_left, hero_up, hero_down;
	public static Image pistol_fire_down, pistol_fire_up, pistol_fire_left, pistol_fire_right, pistol_horizontal, pistol_vertical, pistol_wall_down, pistol_wall_up, pistol_wall_left, pistol_wall_right;

	// Spillerobjekter
	public static Player me;
	public static List<Player> players = new ArrayList<Player>();

	// Labyrinten
	private Label[][] fields;
	private TextArea scoreList;
	private String[] board = {    // 20x20
			"wwwwwwwwwwwwwwwwwwww",
			"w        ww        w",
			"w w  w  www w  w  ww",
			"w w  w   ww w  w  ww",
			"w  w               w",
			"w w w w w w w  w  ww",
			"w w     www w  w  ww",
			"w w     w w w  w  ww",
			"w   w w  w  w  w   w",
			"w     w  w  w  w   w",
			"w ww ww        w  ww",
			"w  w w    w    w  ww",
			"w        ww w  w  ww",
			"w         w w  w  ww",
			"w        w     w  ww",
			"w  w              ww",
			"w  w www  w w  ww ww",
			"w w      ww w     ww",
			"w   w   ww  w      w",
			"wwwwwwwwwwwwwwwwwwww"
	};

	// -------------------------------------------
	// | Maze: (0,0)              | Score: (1,0) |
	// |-----------------------------------------|
	// | boardGrid (0,1)          | scorelist    |
	// |                          | (1,1)        |
	// -------------------------------------------

	// Forbindelsesvariabler
	private DataOutputStream outToServer;
	private Socket clientSocket;

	@Override
	public void start(Stage primaryStage) {
		try {
			// Opretter forbindelse til serveren
			clientSocket = new Socket("localhost", 6799);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());

			// Starter en tråd til at læse fra serveren
			Reader readerThread = new Reader(clientSocket, this);
			readerThread.start();

			// Vis dialog for at registrere spilleren
			showRegisterGameDialog();

			// Opsætning af GUI-elementer
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			// Opretter et label til at vise "Maze" teksten
			Text mazeLabel = new Text("Maze:");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			// Opretter et label til at vise "Score" teksten
			Text scoreLabel = new Text("Score:");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			// Opretter en tekstområde til at vise scores
			scoreList = new TextArea();

			// Opretter en grid til at vise labyrinten
			GridPane boardGrid = new GridPane();

			// Indlæser billeder til brug i GUI'en
			image_wall = new Image(getClass().getResourceAsStream(".Image/wall4.png"), size, size, false, false);
			image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"), size, size, false, false);
			hero_right = new Image(getClass().getResourceAsStream("Image/heroRight.png"), size, size, false, false);
			hero_left = new Image(getClass().getResourceAsStream("Image/heroLeft.png"), size, size, false, false);
			hero_up = new Image(getClass().getResourceAsStream("Image/heroUp.png"), size, size, false, false);
			hero_down = new Image(getClass().getResourceAsStream("Image/heroDown.png"), size, size, false, false);

			pistol_fire_down = new Image(getClass().getResourceAsStream("Image/fireDown.png"), size, size, false, false);
			pistol_fire_left = new Image(getClass().getResourceAsStream("Image/fireLeft.png"), size, size, false, false);
			pistol_fire_right = new Image(getClass().getResourceAsStream("Image/fireRight.png"), size, size, false, false);
			pistol_fire_up = new Image(getClass().getResourceAsStream("Image/fireUp.png"), size, size, false, false);

			pistol_vertical = new Image(getClass().getResourceAsStream("Image/fireVertical.png"), size, size, false, false);
			pistol_horizontal = new Image(getClass().getResourceAsStream("Image/fireHorizontal.png"), size, size, false, false);

			pistol_wall_down = new Image(getClass().getResourceAsStream("Image/fireWallSouth.png"), size, size, false, false);
			pistol_wall_left = new Image(getClass().getResourceAsStream("Image/fireWallWest.png"), size, size, false, false);
			pistol_wall_right = new Image(getClass().getResourceAsStream("Image/fireWallEast.png"), size, size, false, false);
			pistol_wall_up = new Image(getClass().getResourceAsStream("Image/fireWallNorth.png"), size, size, false, false);

			// Opretter felter i labyrinten baseret på layout
			fields = new Label[20][20];
			for (int j = 0; j < 20; j++) {
				for (int i = 0; i < 20; i++) {
					switch (board[j].charAt(i)) {
						case 'w':
							fields[i][j] = new Label("", new ImageView(image_wall));
							break;
						case ' ':
							fields[i][j] = new Label("", new ImageView(image_floor));
							break;
						default:
							throw new Exception("Illegal field value: " + board[j].charAt(i));
					}
					boardGrid.add(fields[i][j], i, j);
				}
			}
			scoreList.setEditable(false);

			// Tilføjer en event handler til stop knappen
			primaryStage.setOnCloseRequest(event -> {
				try {
					leaveHandler();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});

			// Tilføjer GUI-elementer til grid'en
			grid.add(mazeLabel, 0, 0);
			grid.add(scoreLabel, 1, 0);
			grid.add(boardGrid, 0, 1);
			grid.add(scoreList, 1, 1);

			// Opretter scenen og viser den
			Scene scene = new Scene(grid, scene_width, scene_height);
			primaryStage.setScene(scene);
			primaryStage.show();

			// Tilføjer en event filter til at lytte efter tastaturinput
			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				switch (event.getCode()) {
					case UP, W:
						outToServerHandler("MOVE", "up");
						break;
					case DOWN, S:
						outToServerHandler("MOVE", "down");
						break;
					case LEFT, A:
						outToServerHandler("MOVE", "left");
						break;
					case RIGHT, D:
						outToServerHandler("MOVE", "right");
						break;
					case ENTER, SPACE:
						outToServerHandler("PISTOL", me.direction);
						break;
					default:
						break;
				}
			});
			// Opdaterer scoren
			scoreList.setText(getScoreList());

			primaryStage.setTitle("Maze multiplayer game");
			primaryStage.getIcons().add(image_floor);
			primaryStage.setOnCloseRequest(e -> {
				System.out.println("Shutting down for game...");
				try {
					leaveHandler();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				Platform.exit();
				System.exit(0);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showRegisterGameDialog() {
		Dialog usernameDialog = new Dialog();
		GridPane gridDialog = new GridPane();
		gridDialog.setHgap(10);
		gridDialog.setVgap(10);
		gridDialog.setAlignment(javafx.geometry.Pos.CENTER);

		TextField usernameField = new TextField();
		usernameField.setPromptText("Username");
		gridDialog.add(usernameField, 0, 0);
		Button okButton = new Button("OK");
		okButton.setDefaultButton(true);
		okButton.setPrefSize(40, 20);
		gridDialog.add(okButton, 0, 1);

		Stage preStage = new Stage();
		Scene usernameScene = new Scene(gridDialog, 300, 300);
		preStage.setScene(usernameScene);

		okButton.setOnAction(e -> {
			// send request for username registration
			String name = usernameField.getText();
			if (name.isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid username");
				alert.setContentText("Please enter a valid username");
				alert.showAndWait();
			} else if (players.stream().anyMatch(p -> p.name.equals(name))) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid username");
				alert.setContentText("Username already taken");
				alert.showAndWait();
			} else {
				createPlayer(name);
				usernameDialog.close();
				preStage.close();
			}
		});

		preStage.setOnCloseRequest(e -> {
			System.out.println("Did not enter a username");
			Platform.exit();
			System.exit(0);
		});

		preStage.setTitle("Insert your player name");
		preStage.initModality(Modality.APPLICATION_MODAL);

		Platform.runLater(preStage::showAndWait);
	}

	// Metode til håndtering af "Leave" knappen
	private void leaveHandler() throws IOException {
		outToServerHandler("LEAVE", "ud");
		Platform.exit();
	}

	// Metode til at fjerne en spiller fra spillet
	public void leave(String name) {
		Player deadPlayer = null;
		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			if (p.name.equals(name)) {
				deadPlayer = p;
			}
		}
		System.out.println("Player " + deadPlayer.name + " has left the game");
		players.remove(deadPlayer);
	}

	// Metode til at oprette en spiller
	public Player getPlayer(String playName, int X, int Y, String direcktion) {
		Player player = null;

		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			if (p.name.equals(playName)) {
				player = p;
			}
		}
		if (player == null) {
			player = new Player(playName, X, Y, direcktion);
			players.add(player);
		}
		return player;
	}

	// Metode til at opdatere en spillers point
	public void playerPoints(Player p, int point) {
		p.point = point;
	}

	// Metode til at flytte en spiller og opdatere GUI'en
	public void playerMoved(int delta_x, int delta_y, String direction, Player player) {
		Platform.runLater(() -> {
			// Check if player is in the list
			if (player.lastRespawn > 0 && System.currentTimeMillis() - player.lastRespawn < 5000) {
				// Player is dead and respawning
				System.out.println("Player " + player.name + " is respawning");
				return;
			}
			player.direction = direction;
			int x = player.getXpos(),
					y = player.getYpos();

			// Håndterer kollision med vægge
			if (board[y + delta_y].charAt(x + delta_x) == 'w') {
				player.addPoints(-1);
			} else {
				// Håndterer kollision med andre spillere
				Player p = getPlayerAt(x + delta_x, y + delta_y);
				if (p != null) {
					player.addPoints(10);
					p.addPoints(-10);
				} else {
					// Flytter spilleren hvis der ikke er kollision
					player.addPoints(1);

					fields[x][y].setGraphic(new ImageView(image_floor));
					x += delta_x;
					y += delta_y;

					// Opdaterer spillerens position og retning
					if (direction.equals("right")) {
						fields[x][y].setGraphic(new ImageView(hero_right));
					}
					;
					if (direction.equals("left")) {
						fields[x][y].setGraphic(new ImageView(hero_left));
					}
					;
					if (direction.equals("up")) {
						fields[x][y].setGraphic(new ImageView(hero_up));
					}
					;
					if (direction.equals("down")) {
						fields[x][y].setGraphic(new ImageView(hero_down));
					}
					;
					if (direction.equals("ud")) {
						fields[x][y].setGraphic(new ImageView(image_floor));
					}
					;
					if (direction.equals("ind")) {
						fields[x][y].setGraphic(new ImageView(hero_up));
					}
					;

					// Opdaterer spillerens position
					player.setXpos(x);
					player.setYpos(y);
				}
			}
			// Opdaterer scoren
			scoreList.setText(getScoreList());
		});
	}

	public void playerShoot(String direction, Player player) {
		Platform.runLater(() -> {
			player.direction = direction;
			int x = player.getXpos(), y = player.getYpos();
			List<Label> pistolLine = new ArrayList<>();

			if (System.currentTimeMillis() - player.lastShot < 1000*5) {
				return;
			}
			player.lastShot = System.currentTimeMillis();

			switch (direction) {
				case "left": {
					// check for wall next to the player direction
					// if wall exists, shoot cancelled
					if (board[y].charAt(x-1) == 'w') {
						break;
					}
					// go to left to make the pistol line until the fields[x][y] is a wall
					hitByPistol(x-1, y, player);
					fields[x-1][y].setGraphic(new ImageView(pistol_fire_left));
					pistolLine.add(fields[x-1][y]);
					x--;
					while (board[y].charAt(x-1) != 'w') {
						x--;
						hitByPistol(x, y, player);
						fields[x][y].setGraphic(new ImageView(pistol_horizontal));
						pistolLine.add(fields[x][y]);
					}
					// hitting the wall
					fields[x][y].setGraphic(new ImageView(pistol_wall_left));
					break;
				}
				case "right": {
					// check for wall next to the player direction
					// if wall exists, shoot cancelled
					if (board[y].charAt(x+1) == 'w') {
						break;
					}
					// go to right to make the pistol line until the fields[x][y] is a wall
					hitByPistol(x+1, y, player);
					fields[x+1][y].setGraphic(new ImageView(pistol_fire_right));
					pistolLine.add(fields[x+1][y]);
					x++;
					while (board[y].charAt(x+1) != 'w') {
						x++;
						hitByPistol(x, y, player);
						fields[x][y].setGraphic(new ImageView(pistol_horizontal));
						pistolLine.add(fields[x][y]);
					}
					// hitting the wall
					fields[x][y].setGraphic(new ImageView(pistol_wall_right));
					break;
				}
				case "up": {
					// check for wall next to the player direction
					// if wall exists, shoot cancelled
					if (board[y-1].charAt(x) == 'w') {
						break;
					}
					// go up to make the pistol line until the fields[x][y] is a wall
					hitByPistol(x, y-1, player);
					fields[x][y-1].setGraphic(new ImageView(pistol_fire_up));
					pistolLine.add(fields[x][y-1]);
					y--;
					while (board[y-1].charAt(x) != 'w') {
						y--;
						hitByPistol(x, y, player);
						fields[x][y].setGraphic(new ImageView(pistol_vertical));
						pistolLine.add(fields[x][y]);
					}
					// hitting the wall
					fields[x][y].setGraphic(new ImageView(pistol_wall_up));
					break;
				}
				case "down": {
					// check for wall next to the player direction
					// if wall exists, shoot cancelled
					if (board[y+1].charAt(x) == 'w') {
						break;
					}
					// go down to make the pistol line until the fields[x][y] is a wall
					hitByPistol(x, y+1, player);
					fields[x][y+1].setGraphic(new ImageView(pistol_fire_down));
					pistolLine.add(fields[x][y+1]);
					while (board[y+1].charAt(x) != 'w') {
						y++;
						hitByPistol(x, y, player);
						fields[x][y].setGraphic(new ImageView(pistol_vertical));
						pistolLine.add(fields[x][y]);
					}
					// hitting the wall
					hitByPistol(x, y, player);
					fields[x][y].setGraphic(new ImageView(pistol_wall_down));
					break;
				}
			}
			new Thread(() -> {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Platform.runLater(() -> {
						for (Label l : pistolLine) {
							l.setGraphic(new ImageView(image_floor));
						}
					});
				}
			}).start();
		});
	}

	// Metode til at håndtere en pistolkollision
	private void hitByPistol(int x, int y, Player byPlayer) {
		Player p = getPlayerAt(x, y);
		if (p != null) {
			p.kill(byPlayer);
		}
	}

	// Metode til at opdatere scorelisten
	public String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Player p : players) {
			String cooldownRespawn = "";
			cooldownRespawn = (System.currentTimeMillis() - p.lastRespawn < 5000) ? " (Respawning)" : "";
			b.append(p + (p == me ? " <-- You" : "") + cooldownRespawn + "\n\r");
		}
		return b.toString();
	}

	// Metode til at finde en spiller baseret på position
	public Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos() == x && p.getYpos() == y) {
				// Check if player is respawning
				if (System.currentTimeMillis() - p.lastRespawn < 5000) {
					return null;
				}
				return p;
			}
		}
		return null;
	}

	// Metode til at sende en besked til serveren
	public void outToServerHandler(String handling, String direction) {
		try {
			outToServer.writeBytes(handling + " " + me.name + " " + me.xpos + " " + me.ypos + " " + direction + " " + me.point + "\n"); // \n HUSK NEWLINE
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	// Metode til at oprette en spiller
	public void createPlayer(String name) {
		Random random = new Random();
		int x = random.nextInt(20); // Generate random x coordinate between 0 and 19
		int y = random.nextInt(20); // Generate random y coordinate between 0 and 19

		// Ensure the generated position is not a wall
		while (board[y].charAt(x) == 'w') {
			x = random.nextInt(20);
			y = random.nextInt(20);
		}

		me = new Player(name, x, y, "up");
		players.add(me);
		fields[x][y].setGraphic(new ImageView(hero_up));

		outToServerHandler("MOVE", "ind");
	}
}