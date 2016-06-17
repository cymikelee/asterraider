// SpaceObject class
// by Mike Lee

import java.awt.Graphics;
import java.awt.image.ImageObserver;

public abstract class SpaceObject {
	// Fields
	protected double x;
	protected double y;
	protected double xVel;
	protected double yVel;
	protected SpaceObjectGroup group;

	// Constructors
	public SpaceObject(SpaceObjectGroup sog) {
		this(0,0,0,0,sog);
	}

	public SpaceObject(double x, double y, SpaceObjectGroup sog) {
		this(x,y,0,0,sog);
	}

	public SpaceObject(double x, double y, double xv, double yv, SpaceObjectGroup sog) {
		group = sog;
		this.x = x;
		this.y = y;
		xVel = xv;
		yVel = yv;
	}

	// Methods
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getIntX() {
		return (int)x;
	}

	public int getIntY() {
		return (int)y;
	}

	public void move(double xIncr, double yIncr) {
		x += xIncr;
		y += yIncr;
		if(x < 0)
			x += AsterRaider.GAME_WIDTH;
		if(x > AsterRaider.GAME_WIDTH)
			x -= AsterRaider.GAME_WIDTH;
		if(y < 0)
			y += AsterRaider.GAME_HEIGHT;
		if(y > AsterRaider.GAME_HEIGHT)
			y -= AsterRaider.GAME_HEIGHT;
	}

	public double getVelX() {
		return xVel;
	}

	public double getVelY() {
		return yVel;
	}

	public void accelerate(double xVelIncr, double yVelIncr) {
		if((xVel+xVelIncr <= AsterRaider.MAX_VEL && yVel+yVelIncr <= AsterRaider.MAX_VEL) || !AsterRaider.SPEED_CAP) {
			xVel += xVelIncr;
			yVel += yVelIncr;
		}
	}

	public void kill() {
		group.removeElement(this);
	}

	public void update() {
		move(xVel,yVel);
	}

	public abstract void drawMe(Graphics g, ImageObserver observer);

}

