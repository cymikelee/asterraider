// Level class for AsterRaider

public class Level {
	// Fields
	private int levelNumber;
	private int initRocks;
	public static double MAX_VEL = 10;

	// Constructor
	public Level(int n) {
		levelNumber = n;
		initRocks = n+1;
		MAX_VEL = 6+n/3;
	}

	// Methods
	public int getNumber() {
		return levelNumber;
	}

	public int numInitRocks() {
		return initRocks;
	}

}
