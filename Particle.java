// Particle class for AsterRaider
// by Mike Lee

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class Particle extends SpaceObject {
	// Fields
	private int size;
	private int life; // shouldn't exceed 180
	private boolean fading;
	private float red;
	private float grn;
	private float blu;
	private float fadeStepR;
	private float fadeStepG;
	private float fadeStepB;

	// Constructor
	public Particle(double x, double y, int z, double vx, double vy, int lf, Color c, SpaceObjectGroup sog) {
		this(x,y,z,vx,vy,lf,c,false,sog);
	}

	public Particle(double x, double y, int z, double vx, double vy, int lf, Color c, boolean f, SpaceObjectGroup sog) {
		super(x,y,vx,vy,sog);
		size = z;
		life = lf;
		red = (float)c.getRed()/255;
		grn = (float)c.getGreen()/255;
		blu = (float)c.getBlue()/255;
		fading = f;
		if(fading) {
			fadeStepR = red/(float)life;
			fadeStepG = grn/(float)life;
			fadeStepB = blu/(float)life;
		}
	}

	// Methods
	public int getLife() {
		return life;
	}

	public boolean isAlive() {
		return life > 0;
	}

	public void update() {
		move(xVel,yVel);
		if(fading) {
			red -= fadeStepR;
			grn -= fadeStepG;
			blu -= fadeStepB;
		}
		life--;
		if(life < 0)
			kill();
	}

	public void drawMe(Graphics g, ImageObserver observer) {
		Color c = g.getColor();
		g.setColor(new Color(Math.max(0,red),Math.max(0,grn),Math.max(0,blu)));
		g.fillRect(getIntX()-size/2,getIntY()-size/2,size,size);
		g.setColor(c);
	}

}

