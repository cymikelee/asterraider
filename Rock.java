// Rock class for AsterRaider
// by Mike Lee

public class Rock extends SpaceSpriteObject {
	// Fields
	public static final double MAX_VEL = Level.MAX_VEL/2;
	private boolean rotForward;
	private int rotDelay;
	private int size;
	private int life;

	// Constructor
	public Rock(SpaceSprite ss, int s, SpaceObjectGroup sog) {
		super(ss,sog);
		if((int)(Math.random()*2) == 0) {
			x = Math.random()*AsterRaider.GAME_WIDTH;
			y = 0;
		} else {
			x = 0;
			y = Math.random()*AsterRaider.GAME_HEIGHT;
		}
		xVel = Math.random()*MAX_VEL-MAX_VEL/2;
		yVel = Math.random()*MAX_VEL-MAX_VEL/2;
		if((int)(Math.random()*2) == 0)
			rotForward = true;
		else
			rotForward = false;
		rotDelay = (int)(Math.random()*5)+5;
		size = s;
		life = s+1;
	}

	public Rock(SpaceSprite ss, int s, double x, double y, SpaceObjectGroup sog) {
		super(ss,sog);
		this.x = x;
		this.y = y;
		xVel = Math.random()*MAX_VEL-MAX_VEL/2;
		yVel = Math.random()*MAX_VEL-MAX_VEL/2;
		if((int)(Math.random()*2) == 0)
			rotForward = true;
		else
			rotForward = false;
		rotDelay = (int)(Math.random()*5)+5;
		size = s;
		life = s+1;
	}

	// Methods
	public int size() {
		return size;
	}

	public int life() {
		return life;
	}

	public void hit(int power) {
		life -= power;
		if(life <= 0)
			kill();
	}

	public int getScore() {
		return (size+1)*100;
	}

	public void update() {
		move(xVel,yVel);
		if(AsterRaider.getTickCount()%rotDelay == 0) {
			if(rotForward)
				sprite.incFrame();
			else
				sprite.decFrame();
		}
	}

}

