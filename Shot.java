// Shot class for AsterRaider
// by Mike Lee

public class Shot extends SpaceSpriteObject {
	// Fields
	private final int LIFESPAN = 20;
	private final double SHOT_SPEED = 10;
	private int life;
	private int theta;
	private int power;

	// Constructor
	public Shot(SpaceSprite ss, int angle, Ship firingShip, int p, SpaceObjectGroup sog) {
		super(ss,firingShip.getX(),firingShip.getY(),sog);
		life = LIFESPAN;
		theta = angle;
		double frame = ((double)theta/360*(double)numFrames+9)%36;
		sprite.setFrame((int)frame);
		xVel = firingShip.getVelX() + SHOT_SPEED*Math.cos(theta*Math.PI/180);
		yVel = firingShip.getVelY() + SHOT_SPEED*Math.sin(theta*Math.PI/180);
		power = p;
	}

	// Methods
	public int getPower() {
		return power;
	}

	public void update() {
		move(xVel,yVel);
		life--;
		if(life < 0)
			kill();
	}

}

