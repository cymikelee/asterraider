// SpaceSpriteObject class
// by Mike Lee

import java.awt.Graphics;
import java.awt.image.ImageObserver;

public abstract class SpaceSpriteObject extends SpaceObject {
	// Fields
	protected SpaceSprite sprite;
	protected int xrad, yrad;
	protected int numFrames;

	// Constructor
	public SpaceSpriteObject(SpaceSprite ss, SpaceObjectGroup sog) {
		this(ss,0,0,0,0,sog);
	}

	public SpaceSpriteObject(SpaceSprite ss, double x, double y, SpaceObjectGroup sog) {
		this(ss,x,y,0,0,sog);
	}

	public SpaceSpriteObject(SpaceSprite ss, double x, double y, double xv, double yv, SpaceObjectGroup sog) {
		super(x,y,xv,yv,sog);
		sprite = new SpaceSprite(ss);
		xrad = ss.getWidth()/2 - ss.getTrim();
		yrad = ss.getHeight()/2 - ss.getTrim();
		numFrames = ss.totalFrames();
	}

	// Methods
	public SpaceSprite getSprite() {
		return sprite;
	}

	public void setSprite(SpaceSprite ss) {
		sprite = ss;
	}

	public int getRadX() {
		return xrad;
	}

	public int getRadY() {
		return yrad;
	}

	public boolean isCollidingWith(SpaceSpriteObject so) {
		boolean east = (x-xrad < so.getX()+so.getRadX()) && (x-xrad > so.getX()-so.getRadX());
		boolean west = (x+xrad > so.getX()-so.getRadX()) && (x+xrad < so.getX()+so.getRadX());
		boolean north = (y-yrad < so.getY()+so.getRadY()) && (y-yrad > so.getY()-so.getRadY());
		boolean south = (y+yrad > so.getY()-so.getRadY()) && (y+yrad < so.getY()+so.getRadY());
		return (east || west) && (north || south);
	}

	public void drawMe(Graphics g, ImageObserver observer) {
		sprite.drawMe(g,getIntX(),getIntY(),observer);
		if(getIntX() > 0 && getIntX()-sprite.getWidth()/2 < 0)
			sprite.drawMe(g,getIntX()+AsterRaider.GAME_WIDTH,getIntY(),observer);
		else if(getX() < AsterRaider.GAME_WIDTH && getIntX()+sprite.getWidth()/2 > AsterRaider.GAME_WIDTH)
			sprite.drawMe(g,getIntX()-AsterRaider.GAME_WIDTH,getIntY(),observer);
		else if(getIntY() > 0 && getIntY()-sprite.getWidth()/2 < 0)
			sprite.drawMe(g,getIntX(),getIntY()+AsterRaider.GAME_HEIGHT,observer);
		else if(getIntY() < AsterRaider.GAME_HEIGHT && getIntY()+sprite.getWidth()/2 > AsterRaider.GAME_HEIGHT)
			sprite.drawMe(g,getIntX(),getIntY()-AsterRaider.GAME_HEIGHT,observer);
	}

}

