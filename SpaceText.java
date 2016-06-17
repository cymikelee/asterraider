// SpaceText class for AsterRaider
// by Mike Lee

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

public class SpaceText extends Star {
	// Fields
	private String text;
	private int life;

	// Constructors
	public SpaceText(String s, int x, int y, int z, int m) {
		super(x,y,z,m);
		text = s;
		life = -1;
	}

	public SpaceText(String s, int x, int y, int z, Color c) {
		super(x,y,z,1);
		text = s;
		color = c;
		life = -1;
	}

	public SpaceText(String s, int x, int y, int z, int m, int l) {
		super(x,y,z,m);
		text = s;
		life = l;
	}

	public SpaceText(String s, int x, int y, int z, Color c, int l) {
		super(x,y,z,1);
		text = s;
		color = c;
		life = l;
	}
	
	// Methods
	public boolean isAlive() {
		return (life > 0 || life == -1);
	}

	public void kill() {
		life = 0;
	}

	public void move(int dx, int dy) {
		x+=dx;
		y+=dy;
	}

	public void dim(int f) {
		color = new Color(color.getRed()/f,color.getGreen()/f,color.getBlue()/f);
	}

	public void update() {
		if(life > 0)
			life--;
	}

	public void drawMe(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		g.drawString(text,x,y);
		g.setColor(c);
	}

	public void changeText(String s) {
		text = s;
	}

	public static void addFadingMessage(String message, Vector vector) {
	// adds to bottom-of-screen message queue
		SpaceText stx;
		if(!vector.isEmpty()) {
			for(int i=0; i<vector.size(); i++) {
				stx = (SpaceText)vector.elementAt(i);
				stx.dim(2);
				stx.move(0,-14);
			}
		}
		vector.addElement(new SpaceText(message,10,AsterRaider.GAME_HEIGHT-26,10,191,AsterRaider.SLEEP_TIME*6));
	}

	public static void addFadingMessages(String[] messages, Vector vector) {
	// adds to bottom-of-screen message queue
		SpaceText stx;
		if(!vector.isEmpty()) {
			for(int i=0; i<vector.size(); i++) {
				stx = (SpaceText)vector.elementAt(i);
				stx.dim(2);
				stx.move(0,-14*messages.length);
			}
		}
		for(int i=0; i<messages.length; i++)
			vector.addElement(new SpaceText(messages[i],10,AsterRaider.GAME_HEIGHT-12-14*(messages.length-i),10,191,AsterRaider.SLEEP_TIME*20));
	}
	
}
