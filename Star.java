// Star class for AsterRaider
// by Mike Lee

import java.awt.Color;
import java.awt.Graphics;

public class Star {
	// Fields
	protected int x;
	protected int y;
	protected int size;
	protected Color color;

	// Constructor
	public Star(int x, int y, int z, float m) {
		this.x = x;
		this.y = y;
		size = z;
		color = new Color(m,m,m);
	}

	public Star(int x, int y, int z, int m) {
		this.x = x;
		this.y = y;
		size = z;
		color = new Color(m,m,m);
	}

	// Methods
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return size;
	}

	public Color getMagnitude() {
		return color;
	}

	public void drawMe(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		g.fillRect(x,y,size,size);
		g.setColor(c);
	}

	public static Star[] generateStarfield(int numStars) {
		Star[] starfield = new Star[numStars];
		int randx, randy, randz;
		float randc;
		for(int i=0; i<starfield.length; i++) {
			randx = (int)(Math.random()*(AsterRaider.GAME_WIDTH-2))+1;
			randy = (int)(Math.random()*(AsterRaider.GAME_HEIGHT-2))+1;
			randz = (int)(Math.random()+1.25);
			randc = (float)Math.random();
			starfield[i] = new Star(randx,randy,randz,randc);
		}
		return starfield;
	}

}

