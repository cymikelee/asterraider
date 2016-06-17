// LagCounter class for AsterRaider
// by Mike Lee

import java.awt.Graphics;

public class LagCounter {
	// Fields
	private int lagAverage;
	private int lagSum;
	private float lagTotalAverage;
	private int lagTotal;
	private SpaceText lagText;

	// Constructor
	public LagCounter() {
		lagAverage = 0;
		lagSum = 0;
		lagTotalAverage = 0;
		lagTotal = 0;
		lagText = new SpaceText("Average Lag Time: --",AsterRaider.GAME_WIDTH-216,20,10,255);
	}

	// Methods
	public void reset() {
		lagAverage = 0;
		lagSum = 0;
		lagTotalAverage = 0;
		lagTotal = 0;
	}

	public void add(long time) {
		lagSum += (int)time;
	}

	public void update() {
		lagAverage = lagSum/30;
		lagSum = 0;
		lagTotal += lagAverage;
		lagTotalAverage = (float)lagTotal/AsterRaider.secondsCounter;
	}

	public void display(Graphics g) {
		lagText.changeText("Average Lag Time: "+lagAverage+", "+lagTotalAverage);
		lagText.drawMe(g);
	}

}

