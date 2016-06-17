// HighScore class
// by Mike Lee

public class HighScore {
	// Fields
	private String player;
	private int score;
	private int level;

	// Constructors
	public HighScore() {
		player = new String("Anonymous");
		score = 0;
		level = 0;
	}

	public HighScore(String p, int s) {
		this(p,s,0);
	}

	public HighScore(String p, int s, int l) {
		player = p;
		score = s;
		level = l;
	}

	public HighScore(String hs) {
		player = new String();
		int i = 0;
		do {
			player+=hs.charAt(i);
			i++;
		} while(hs.charAt(i) != '\t');
		i++;
		String temp = new String();
		do {
			temp+=hs.charAt(i);
			i++;
		} while(hs.charAt(i) != '\t');
		score = Integer.parseInt(temp);
		i++;
		temp = new String();
		while(i < hs.length()) {
			temp+=hs.charAt(i);
			i++;
		}
		level = Integer.parseInt(temp);
	}

	public HighScore(HighScore hs) {
		player = hs.player;
		score = hs.score;
		level = hs.level;
	}

	// Methods
	public void setPlayer(String p) {
		player = p;
	}

	public void setScore(int s) {
		score = s;
	}

	public void setLevel(int l) {
		level = l;
	}

	public String getPlayer() {
		return player;
	}

	public int getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	public String toString() {
		return (player+'\t'+Integer.toString(score)+'\t'+Integer.toString(level));
	}

}

