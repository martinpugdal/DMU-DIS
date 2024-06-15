// En klasse til at repræsentere en spiller i spillet
public class Player {
	String name; // Navnet på spilleren
	int xpos; // X-koordinatet for spilleren
	int ypos; // Y-koordinatet for spilleren
	int point; // Point for spilleren
	String direction; // Spillerens retning

	long lastShot = 0; // Tidspunkt for sidste skud
	long lastRespawn = 0; // Tidspunkt for sidste genoplivning

	// Konstruktør til at oprette en spiller med navn, position og retning
	public Player(String name, int xpos, int ypos, String direction) {
		this.name = name;
		this.xpos = xpos;
		this.ypos = ypos;
		this.direction = direction;
		this.point = 0; // Startpoint for spilleren
	}

	// Getter for spillerens x-koordinat
	public int getXpos() {
		return xpos;
	}

	// Setter for spillerens x-koordinat
	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	// Getter for spillerens y-koordinat
	public int getYpos() {
		return ypos;
	}

	// Setter for spillerens y-koordinat
	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	// Getter for spillerens retning
	public String getDirection() {
		return direction;
	}

	// Setter for spillerens retning
	public void setDirection(String direction) {
		this.direction = direction;
	}

	// Metode til at tilføje point til spilleren
	public void addPoints(int p) {
		point+=p;
	}

	// Metode til at dræbe spilleren
	public void kill(Player killedBy) {
		point -= 10;
		killedBy.addPoints(10);
		lastRespawn = System.currentTimeMillis();
	}

	// Metode til at konvertere spilleren til en streng
	public String toString() {
		return name+":   "+point;
	}
}