// Ship class for AsterRaider
// by Mike Lee

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class Ship extends SpaceSpriteObject {
	// Fields: Properties
	private ShipType type;
	private int turnDelay; // needs to stay between 2 and 10
	private double accelRate;
	private int theta;
	private int thetaIncr;
	private int shotPower;
	public int shRad;

	// Fields: Flags
	private boolean alive;
	private int rotation, rotStart;
	private boolean thrustersOn;
	private boolean shieldsOn;

	// Fields: Interfacing with AsterRaider
	public SpaceObjectGroup shots;
	public SpaceObjectGroup exhaust;
	public boolean reincarnating;

	// Constructor
	public Ship(ShipType type, Graphics offScreen, ImageObserver observer) {
		super(type.shipSprite,AsterRaider.GAME_WIDTH/2,AsterRaider.GAME_HEIGHT/2,null);
		this.type = type;
		shots = new SpaceObjectGroup(AsterRaider.MAX_SHOTS,offScreen,observer);
		exhaust = new SpaceObjectGroup(AsterRaider.MAX_PARTICLES,offScreen,observer);
		turnDelay = type.defaultTurnDelay;
		theta = 270;
		thetaIncr = 360/numFrames;
		rotation = 0;
		rotStart = 0;
		accelRate = type.defaultAccel;
		shotPower = type.defaultShotPower;
		shRad = type.shieldRadius;
		thrustersOn = false;
		shieldsOn = true;
		alive = true;
		reincarnating = true;
		if(AsterRaider.soundEnabled)
			type.shieldSound.loop();
	}

	// Methods
	public int orientation() {
		return theta;
	}

	public boolean isAlive() {
		return alive;
	}

	public void rotateCW() {
		theta += thetaIncr;
		if(theta >= 360)
			theta -= 360;
		sprite.incFrame();
	}

	public void rotateCCW() {
		theta -= thetaIncr;
		if(theta < 0)
			theta += 360;
		sprite.decFrame();
	}

	public void thrust() {
		double costh = Math.cos(theta*Math.PI/180);
		double sinth = Math.sin(theta*Math.PI/180);
		double xAccel = accelRate*costh;
		double yAccel = accelRate*sinth;
		accelerate(xAccel,yAccel);
		if(AsterRaider.getTickCount()%type.makeExDelay == 0 && exhaust.size() < AsterRaider.MAX_PARTICLES) {
			double exVelX = xVel - type.exSpeedFrac*xAccel;
			double exVelY = yVel - type.exSpeedFrac*yAccel;
			if(type.exhaustPorts == 1 && AsterRaider.useParticleEffects)
				exhaust.addParticle(x-(xrad-8)*costh,y-(yrad-8)*sinth,type.exPartSize,exVelX,exVelY,type.exPartLife,type.exPartColor,true);
			else if(type.exhaustPorts == 2 && AsterRaider.useParticleEffects) {
				exhaust.addParticle(x-(xrad-8)*costh-type.exPortOffset*sinth,y-(yrad-8)*sinth+type.exPortOffset*costh,type.exPartSize,exVelX,exVelY,type.exPartLife,type.exPartColor,true);
				exhaust.addParticle(x-(xrad-8)*costh+type.exPortOffset*sinth,y-(yrad-8)*sinth-type.exPortOffset*costh,type.exPartSize,exVelX,exVelY,type.exPartLife,type.exPartColor,true);
			} else if(type.exhaustPorts == 3 && AsterRaider.useParticleEffects) {
				exhaust.addParticle(x-(xrad-8)*costh,y-(yrad-8)*sinth,type.exPartSize,exVelX,exVelY,type.exPartLife,type.exPartColor,true);
				exhaust.addParticle(x-(xrad-8)*costh-type.exPortOffset*sinth,y-(yrad-8)*sinth+type.exPortOffset*costh,type.exPartSize,exVelX,exVelY,type.exPartLife,type.exPartColor,true);
				exhaust.addParticle(x-(xrad-8)*costh+type.exPortOffset*sinth,y-(yrad-8)*sinth-type.exPortOffset*costh,type.exPartSize,exVelX,exVelY,type.exPartLife,type.exPartColor,true);
			} else if (type.exhaustPorts != 0)
				AsterRaider.errManager.handleGameError("Ship has invalid number of exhaust ports (must be between 0 and 2)");
		}
	}

	public void setRotation(int r) {
		rotation = r;
		rotStart = AsterRaider.getTickCount()%turnDelay;
	}

	public void applyThrust() {
		thrustersOn = true;
		if(AsterRaider.soundEnabled)
			type.engineSound.loop();
	}

	public void stopThrust() {
		thrustersOn = false;
		if(AsterRaider.soundEnabled)
			type.engineSound.stop();
	}

	public boolean fire() { // returns whether shot was fired
		if(alive && shots.size() < AsterRaider.MAX_SHOTS && (!shieldsOn || reincarnating)) {
			shots.addShot(type.shotSprite,theta,this,shotPower);
			if(AsterRaider.soundEnabled)
				type.shotSound.play();
			return true;
		}
		return false;
	}

	public void activateShield() {
		type.shieldSound.stop();
		if(alive) {
			shieldsOn = true;
			if(AsterRaider.soundEnabled)
				type.shieldSound.loop();
		}
	}

	public void deactivateShield() {
		type.shieldSound.stop();
		shieldsOn = false;
	}

	public boolean shieldsAreOn() {
		return shieldsOn;
	}

	public void drawShields(Graphics g) {
		Color c = g.getColor();
		if(AsterRaider.getTickCount() % 2 == 0)
			g.setColor(type.shieldColorA);
		else
			g.setColor(type.shieldColorB);
		g.drawOval(getIntX()-shRad,getIntY()-shRad,shRad*2,shRad*2);
		if(getX() > 0 && getX()-shRad < 0)
			g.drawOval(getIntX()-shRad+AsterRaider.GAME_WIDTH,getIntY()-shRad,shRad*2,shRad*2);
		else if(getX() < AsterRaider.GAME_WIDTH && getX()+shRad > AsterRaider.GAME_WIDTH)
			g.drawOval(getIntX()-shRad-AsterRaider.GAME_WIDTH,getIntY()-shRad,shRad*2,shRad*2);
		else if(getY() > 0 && getY()-shRad < 0)
			g.drawOval(getIntX()-shRad,getIntY()-shRad+AsterRaider.GAME_HEIGHT,shRad*2,shRad*2);
		else if(getY() < AsterRaider.GAME_HEIGHT && getY()+shRad > AsterRaider.GAME_HEIGHT)
			g.drawOval(getIntX()-shRad,getIntY()-shRad-AsterRaider.GAME_HEIGHT,shRad*2,shRad*2);
		g.setColor(c);
	}

	public void update() {
		if(alive) {
			if(rotation > 0 && rotStart == AsterRaider.getTickCount()%turnDelay)
				rotateCW();
			else if(rotation < 0 && rotStart == AsterRaider.getTickCount()%turnDelay)
				rotateCCW();
			if(thrustersOn)
				thrust();
			move(xVel,yVel);
		} else
			AsterRaider.errManager.handleGameError("Cannot update dead ship");
	}

	public void kill() {
		alive = false;
		type.engineSound.stop();
		type.shieldSound.stop();
	}

}

