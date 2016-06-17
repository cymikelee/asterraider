// SpaceObjectGroup class for Asteraider
// by Mike Lee

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.Vector;

public class SpaceObjectGroup extends Vector {
	// Fields
	private static final int MAX_GROUP_CAPACITY = 512;
	private static final int DEFAULT_CAPACITY = 64;
	private SpaceObject curso;
	private Graphics offScreen;
	private ImageObserver observer;

	// Constructor
	public SpaceObjectGroup(Graphics offg, ImageObserver obs) {
		this(DEFAULT_CAPACITY,offg,obs);
	}

	public SpaceObjectGroup(int capacity, Graphics offg, ImageObserver obs) {
		super(capacity);
		offScreen = offg;
		observer = obs;
	}

	// Methods
	public void updateAll() {
		for(int i=size()-1; i>=0; i--) {
			curso = (SpaceObject)(elementAt(i));
			curso.update();
			curso.drawMe(offScreen,observer);
		}
	}

	public void drawAll() {
		for(int i=size()-1; i>=0; i--) {
			curso = (SpaceObject)(elementAt(i));
			curso.drawMe(offScreen,observer);
		}
	}

	public void killAll() {
		for(int i=size()-1; i>=0; i--) {
			curso = (SpaceObject)(elementAt(i));
			curso.kill();
		}
	}

	// Specialized Methods
	public void addRock(SpaceSprite ss, int s) {
		addElement(new Rock(ss,s,this));
	}

	public void addRock(SpaceSprite ss, int s, double x, double y) {
		addElement(new Rock(ss,s,x,y,this));
	}

	public void addShot(SpaceSprite ss, int angle, Ship firingShip, int p) {
		addElement(new Shot(ss,angle,firingShip,p,this));
	}

	public void addBoom(SpaceSprite ss, double x, double y) {
		addElement(new Boom(ss,x,y,this));
	}

	public void addParticle(double x, double y, int z, double vx, double vy, int lf, Color c) {
		addElement(new Particle(x,y,z,vx,vy,lf,c,this));
	}

	public void addParticle(double x, double y, int z, double vx, double vy, int lf, Color c, boolean f) {
		addElement(new Particle(x,y,z,vx,vy,lf,c,f,this));
	}

	public void generateDebris(SpaceObject source, int z, int lf, Color col) {
		if(AsterRaider.fadeSmallDebris || z > 1)
			addElement(new Particle(source.getX(),source.getY(),z,Math.random()*AsterRaider.DEBRIS_VEL-AsterRaider.DEBRIS_VEL/2,Math.random()*AsterRaider.DEBRIS_VEL-AsterRaider.DEBRIS_VEL/2,lf,col,true,this));
		else
			addElement(new Particle(source.getX(),source.getY(),z,Math.random()*AsterRaider.DEBRIS_VEL-AsterRaider.DEBRIS_VEL/2,Math.random()*AsterRaider.DEBRIS_VEL-AsterRaider.DEBRIS_VEL/2,lf,col,this));
	}
	
}
