// Boom class (explosions) for AsterRaider
// by Mike Lee

public class Boom extends SpaceSpriteObject {
	// Fields
	private final int EVOLVE_DELAY = 2;
	private int life;
	private int start;

	// Constructor
	public Boom(SpaceSprite ss, double x, double y, SpaceObjectGroup sog) {
		super(ss,x,y,sog);
		life = numFrames;
		start = AsterRaider.getTickCount()%EVOLVE_DELAY;
	}

	// Methods
	public void update() {
		if(start == AsterRaider.getTickCount()%EVOLVE_DELAY) {
			sprite.incFrame();
			life--;
			if(life < 0)
				kill();
		}
	}

}

