// AsterRaider v0.5d3
// by Mike Lee, 06.09.03

// Copyright © 2003 Cymltaneous Solutions
// Website: http://cs.paching.com/
// Author's e-mail: mikelee@cyml.cjb.net

// *** NOTICE ***
// This is an DEVELOPMENT VERSION of AsterRaider.
// Unauthorized copying of this code is prohibited.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Vector;

public class AsterRaider extends GameFrame implements Runnable, KeyListener {
	public static final String VERSION = "0.5d3";

	// Fields: Window/Game Dimension Constants
	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 480;
	public static final int GAME_WIDTH = 600;
	public static final int GAME_HEIGHT = 420;
	public static final int GAME_LEFT = 6;
	public static final int GAME_RIGHT = GAME_LEFT+GAME_WIDTH;
	public static final int GAME_TOP = 32;
	public static final int GAME_BOTTOM = GAME_TOP+GAME_HEIGHT;

	// Fields: Game Limits
	public static final double MAX_VEL = 10;
	public static final boolean SPEED_CAP = false;
	public static final int MAX_SHOTS = 16;
	public static final int MAX_EXPLOSIONS = 8;
	public static final int MAX_PARTICLES = 64;

	// Fields: Game Constants
	public static final int SLEEP_TIME = 17;
	public static final int HIGH_SCORE_LIST_LENGTH = 10;
	public static final int NUM_STARS = 216;
	public static final int NUM_ROCK_TYPES = 3;
	public static final int INIT_LIVES = 3;
	public static final int INIT_BONUS = 5000;
	public static final int BONUS_INC = 500;
	public static final int BONUS_DEC = 50;
	public static final int HIT_BONUS = 75;
	public static final int SHOT_PEN = 50;
	public static final int SHIELD_PEN = 5;
	public static final double DEBRIS_VEL = MAX_VEL*.75;
	public static final int DEBRIS_LIFE = 16;

	// Fields: Preferences
	public static RaiderPrefs preferences;
	public static KeyManager keys;
	public static boolean soundEnabled;
	public static boolean musicEnabled;
	public static boolean useParticleEffects;
	public static boolean fadeSmallDebris;
	public static boolean calcLag;
	public static File prefsFile = new File("arprefs.txt");
	public static File keysFile = new File("keysettings.txt");
	public static File errorLogFile = new File("errorlog.txt");
	public static File highScoreFile = new File("highscores.txt");

	// Fields: Flags
	public boolean loading = true;
	public boolean paused;
	public boolean startingNewLevel;
	public boolean gameOver;
	public boolean endGameNow;

	// Fields: Threads and Timing
	public Thread mainThread = null;
	public static int tickCounter;
	public static int secondsCounter;
	private int startRebirth;
	private int endRebirth;
	private int endLevel;
	private LagCounter lagCounter;

	// Fields: Graphics Buffer and Toolkit
	private Graphics g;
	private Image buffer = null;
	private Graphics offScreen;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	// Fields: Sprites
	private SpriteManager spriteManager;
	private SpaceSprite[] allSprites;
	private SpaceSprite[] rockSprites;
	private SpaceSprite boomSprite;
	private SpaceSprite bigbSprite;

	// Fields: Sound Effects
	private SoundManager soundManager;
	private AUSound[] sounds;
	private AUSound[] loopSounds;
	private AUSound boomSound;
	private AUSound bigBoomSound;
	private AUSound piffSound;
	private AUSound clickSound;
	private AUSound music;

	// Fields: Space Objects
	private Star[] starfield;
	private Vector texts;
	private SpaceObjectGroup rocks;
	private SpaceObjectGroup explosions;
	private SpaceObjectGroup particles;
	private ShipType[] shipTypes;
	private Ship playerShip;
	private int shipID = 0;
	
	// Fields: Other
	public RaiderStartup rs;
	public static ErrorLogManager errManager;
	public PlayerStats playerStats;
	public Level currentLevel;
	public int currentHighestScore;
	public HighScoreManager hsManager;
	private GameOverDialog goDialog;
	private SpaceText pausedText;

	// Fields: Temporary Variables
	private Color c;
	private Shot s;
	private Rock r;
	private Boom b;
	private Particle p;
	private SpaceText stx;
	private FontMetrics fm;
	private String str;
	private int key;

	// Constructor
	public AsterRaider(RaiderStartup rs, ErrorLogManager errManager, HighScoreManager hsManager) {
		super("AsterRaider",WINDOW_WIDTH,WINDOW_HEIGHT,false);
		this.rs = rs;
		this.errManager = errManager;
		this.hsManager = hsManager;
	}

	// Methods
	public void initialize() {
		rs.changeString("Loading...window and graphics buffer");
		// Window
		Dimension dim = toolkit.getScreenSize();
		setLocation(dim.width/2-WINDOW_WIDTH/2,dim.height/2-WINDOW_HEIGHT/2);
		addNotify();
		setBackground(Color.black);
		// Graphics
		g = getGraphics();
		buffer = createImage(GAME_WIDTH,GAME_HEIGHT);
		offScreen = buffer.getGraphics();
	}

	public void loadPrefs() {
		rs.changeString("Loading...preferences");
		preferences = new RaiderPrefs(prefsFile);
		keys = new KeyManager(keysFile);
		soundEnabled = preferences.getBooleanValue("sound");
		musicEnabled = preferences.getBooleanValue("music");
		useParticleEffects = preferences.getBooleanValue("particles");
		fadeSmallDebris = preferences.getBooleanValue("fadeSmall");
		calcLag = preferences.getBooleanValue("calcLag");
	}

	public void loadSprites() {
		rs.changeString("Loading...sprites");
		spriteManager = new SpriteManager(new MediaTracker(this),toolkit,new File("spritelist.txt"));
		allSprites = spriteManager.getSprites();
		rockSprites = new SpaceSprite[NUM_ROCK_TYPES];
		for(int i=0; i<NUM_ROCK_TYPES; i++)
			rockSprites[i] = allSprites[i+16];
		boomSprite = allSprites[preferences.getValue("boomSpt")];
		bigbSprite = allSprites[preferences.getValue("bigBoomSpt")];
		spriteManager = new SpriteManager(new MediaTracker(this),toolkit,new File("spritelist.txt"));
		allSprites = spriteManager.getSprites();
		rockSprites = new SpaceSprite[NUM_ROCK_TYPES];
		for(int i=0; i<NUM_ROCK_TYPES; i++)
			rockSprites[i] = allSprites[i+16];
		boomSprite = allSprites[preferences.getValue("boomSpt")];
		bigbSprite = allSprites[preferences.getValue("bigBoomSpt")];
	}

	public void loadSounds() {
		rs.changeString("Loading...sounds");
		soundManager = new SoundManager(toolkit,new File("soundlist.txt"));
		sounds = soundManager.getSounds();
		loopSounds = soundManager.getLoopSounds();
		boomSound = sounds[preferences.getValue("boomSnd")]; // constant values
		bigBoomSound = sounds[preferences.getValue("bigBoomSnd")];
		piffSound = sounds[preferences.getValue("piffSnd")];
		clickSound = sounds[preferences.getValue("clickSnd")];
		music = loopSounds[preferences.getValue("musicLSnd")];
	}

	public void otherInit() {
		// Initialize ship types and player ship
		shipTypes = ShipType.getShipTypes(new File("shiptypes.txt"),allSprites,sounds,loopSounds);
		playerShip = new Ship(shipTypes[shipID],offScreen,this);
		stopLoopSounds();
		// Initialize high score manager
		hsManager.initialize(this);
		currentHighestScore = hsManager.highestScore();
		// Initialize miscellaneous variables
		pausedText = new SpaceText("Game Paused.  Press P to unpause.",GAME_LEFT+10,GAME_TOP+20,10,Color.white);
		playerStats = new PlayerStats();
		currentLevel = new Level(0);
		// Initialize counters, timers, and flags
		lagCounter = new LagCounter();
		tickCounter = 0;
		secondsCounter = 0;
		startRebirth = 0;
		endRebirth = 0;
		endLevel = 0;
		gameOver = false;
		startingNewLevel = false;
		endGameNow = false;
		paused = true;
	}

	public void launch() {
		rs.setState(-1);
		rs.dispose();
		show();
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		addKeyListener(this);
		loading = false;
		paused = false;
		startNewLevel();
		mainThread = new Thread(this,"armain");
		mainThread.start();
	}

	public void startNewLevel() {
		// Suspend actions, stop looping sounds, clear screen and draw border
		if(paused)
			errManager.handleGameError("Trying to start level when game suspended");
		startingNewLevel = true;
		paused = true;
		removeKeyListener(this);
		stopLoopSounds();
		music.stop();
		g.setColor(getBackground());
		g.fillRect(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		g.setColor(Color.white);
		g.drawRect(GAME_LEFT,GAME_TOP,GAME_WIDTH-1,GAME_HEIGHT-1);
		// Create Level object and update PlayerStats
		currentLevel = new Level(currentLevel.getNumber()+1);
		playerStats.level = currentLevel.getNumber();
		playerStats.runBonusCalc(g,clickSound);
		playerStats.bonus = INIT_BONUS+currentLevel.getNumber()*BONUS_INC;
		try {
			Thread.sleep(500);
		} catch(InterruptedException ie) {}
		// Reinitialize counters
		long startTime = System.currentTimeMillis();
		tickCounter = 0;
		secondsCounter = 0;
		lagCounter.reset();
		endLevel = 0;
		// Create objects
		starfield = Star.generateStarfield(NUM_STARS);
		texts = new Vector(4);
		rocks = new SpaceObjectGroup(currentLevel.numInitRocks()*12,offScreen,this);
		for(int i=0; i<currentLevel.numInitRocks(); i++)
			rocks.addRock(rockSprites[NUM_ROCK_TYPES-1],NUM_ROCK_TYPES-1);
		explosions = new SpaceObjectGroup(MAX_EXPLOSIONS,offScreen,this);
		particles = new SpaceObjectGroup(MAX_PARTICLES,offScreen,this);
		playerShip.kill();
		// Resume
		System.gc();
		try {
			Thread.sleep(Math.max(0,startTime+2500-System.currentTimeMillis()));
		} catch(InterruptedException ie) {}
		reincarnatePlayer();
		addKeyListener(this);
		if(musicEnabled)
			music.loop();
		paused = false;
		startingNewLevel = false;
	}

	// RUN!!!
	public void run() {
		while(!endGameNow) {
			if(!paused) {
				long startTime = System.currentTimeMillis();
				gameUpdate();
				if(endLevel != 0 && secondsCounter >= endLevel && !startingNewLevel)
					startNewLevel();
				if(calcLag)
					lagCounter.add(System.currentTimeMillis()-startTime);
				try {
					Thread.sleep(Math.max(0,SLEEP_TIME+startTime-System.currentTimeMillis()));
				} catch(InterruptedException ie) {}
				if(tickCounter == 30) {
					tickCounter = 0;
					secondsCounter++;
					if(playerStats.bonus > BONUS_DEC) {
						if(secondsCounter != endLevel)
							playerStats.bonus -= BONUS_DEC;
					} else if(playerStats.bonus > 0)
						playerStats.bonus = 0;
					if(calcLag)
						lagCounter.update();
				} else
					tickCounter++;
			}
		}
	}

	public void gameUpdate() {
		try {
			// Check if not supposed to be running
			if(startingNewLevel)
				return;
			// Clear offscreen graphics and draw starfield
			offScreen.setColor(getBackground());
			offScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
			offScreen.setColor(getForeground());
			for(int star=0; star<starfield.length; star++)
				starfield[star].drawMe(offScreen);
			// Update engine exhaust particles
			if(useParticleEffects)
				playerShip.exhaust.updateAll();
			// Update ship
			if(playerShip.isAlive()) {
				playerShip.update();
				playerShip.drawMe(offScreen,this);
				if(playerShip.reincarnating && secondsCounter >= endRebirth) {
					playerShip.deactivateShield();
					playerShip.reincarnating = false;
				}
			} else if(secondsCounter >= startRebirth) {
				if(playerStats.lives >= 0 && !gameOver)
					reincarnatePlayer();
				else {
					endGameNow = true;
					endGame();
					return;
				}
			} else if(playerShip.shieldsAreOn())
				playerShip.deactivateShield();
			// Draw shields and deal penalty, if on
			if(playerShip.shieldsAreOn()) {
				playerShip.drawShields(offScreen);
				if(playerStats.bonus > SHIELD_PEN)
					playerStats.bonus -= SHIELD_PEN;
				else if(playerStats.bonus > 0)
					playerStats.bonus = 0;
			}
			// Update space object groups and check for collisions
			playerShip.shots.updateAll();
			rocks.updateAll();
			explosions.updateAll();
			checkCollisions();
			// Update non-exhaust particles
			if(useParticleEffects)
				particles.updateAll();
			// Draw game border, player stats, text messages, and lag time
			offScreen.setColor(Color.white);
			offScreen.drawRect(0,0,GAME_WIDTH-1,GAME_HEIGHT-1);
			playerStats.display(offScreen,currentHighestScore);
			for(int text=texts.size()-1; text>=0; text--) {
				stx = (SpaceText)texts.elementAt(text);
				if(stx.isAlive()) {
					stx.drawMe(offScreen);
					stx.update();
				} else
					texts.removeElementAt(text);
			}
			if(calcLag)
				lagCounter.display(offScreen);
			// Transfer buffer to screen
			g.drawImage(buffer,GAME_LEFT,GAME_TOP,this);
		} catch(Exception e) {
			errManager.handleGameUpdateException(e,currentLevel,secondsCounter,playerShip,rocks,explosions,particles,texts);
		}
	}

	public void checkCollisions() {
		for(int rInd=0; rInd<rocks.size(); rInd++) {
			r = (Rock)rocks.elementAt(rInd);
			// Check rock-shot collisions
			for(int sInd=0; sInd<playerShip.shots.size(); sInd++) {
				s = (Shot)playerShip.shots.elementAt(sInd);
				if(r.isCollidingWith(s)) {
					s.kill();
					hitRock(r,s.getPower());
					playerStats.bonus += HIT_BONUS;
					if(useParticleEffects)
						for(int i=0; i<8 && particles.size()<MAX_PARTICLES-1; i++)
							particles.generateDebris(s,1,DEBRIS_LIFE,shipTypes[shipID].shPartColor);
				}
			}
			// Check rock-ship collision
			if(playerShip.isAlive()) {
				if(playerShip.shieldsAreOn() && playerShip.distanceTo(r) < playerShip.shRad) {
					if(explosions.size() < MAX_EXPLOSIONS-1)
						explosions.addBoom(boomSprite,r.getX(),r.getY());
					hitRock(r,100);
				} else if(r.isCollidingWith(playerShip)) {
					if(explosions.size() < MAX_EXPLOSIONS-1)
						explosions.addBoom(boomSprite,r.getX(),r.getY());
					hitRock(r,100);
					if(!playerShip.shieldsAreOn() || (r.size() > 0 && !playerShip.reincarnating))
						killPlayer();
				}
			}
		}
	}

	public void hitRock(Rock r, int power) {
		r.hit(power);
		if(r.life() <= 0) {
			playerStats.score+=r.getScore();
			if(explosions.size() < MAX_EXPLOSIONS)
				explosions.addBoom(boomSprite,r.getX(),r.getY());
			if(r.size() > 0) {
				for(int i=0; i<3; i++)
					rocks.addRock(rockSprites[r.size()-1],r.size()-1,r.getX(),r.getY());
			}
			if(useParticleEffects)
				for(int i=0; i<12 && particles.size()<MAX_PARTICLES-1; i++)
					particles.generateDebris(r,1,DEBRIS_LIFE,Color.lightGray);
			if(soundEnabled)
				boomSound.play();
		} else if(soundEnabled)
			piffSound.play();
		if(rocks.isEmpty())
			endLevel = secondsCounter+6;
	}

	public void killPlayer() {
		System.gc();
		playerShip.kill();
		startRebirth = secondsCounter+6;
		if(explosions.size() < MAX_EXPLOSIONS)
			explosions.addBoom(bigbSprite,playerShip.getX(),playerShip.getY());
		if(useParticleEffects)
			for(int i=0; i<16 && particles.size()<MAX_PARTICLES-1; i++)
				particles.generateDebris(playerShip,2,DEBRIS_LIFE*3,Color.lightGray);
		if(soundEnabled)
			bigBoomSound.play();
		if(playerStats.lives > 0)
			playerStats.lives--;
		else
			gameOver = true;
	}

	public void reincarnatePlayer() {
		if(playerStats.lives < 0) {
			errManager.handleGameError("No lives left",true);
		} else if(playerShip != null) {
			if(playerShip.isAlive())
				errManager.handleGameError("Trying to reincarnate live player\n");
		}
		playerShip = new Ship(shipTypes[shipID],offScreen,this);
		endRebirth = secondsCounter+6;
	}

	public void redraw() {
		offScreen.setColor(getBackground());
		offScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
		offScreen.setColor(getForeground());
		for(int star=0; star<starfield.length; star++)
			starfield[star].drawMe(offScreen);
		if(useParticleEffects)
			playerShip.exhaust.drawAll();
		if(playerShip.isAlive())
			playerShip.drawMe(offScreen,this);
		if(playerShip.shieldsAreOn())
			playerShip.drawShields(offScreen);
		playerShip.shots.drawAll();
		rocks.drawAll();
		explosions.drawAll();
		if(useParticleEffects)
			particles.drawAll();
		offScreen.setColor(Color.white);
		offScreen.drawRect(0,0,GAME_WIDTH-1,GAME_HEIGHT-1);
		playerStats.display(offScreen,currentHighestScore);
		if(calcLag)
			lagCounter.display(offScreen);
		g.drawImage(buffer,GAME_LEFT,GAME_TOP,this);
	}

	public void stopLoopSounds() {
		for(int i=0; i<loopSounds.length; i++)
			loopSounds[i].stop();
	}

	public void toggleMusic(boolean musicOn) {
		if(musicOn)
			music.loop();
		else
			music.stop();
	}

	public void pause() {
		paused = true;
		try {
			Thread.sleep(10);
		} catch(InterruptedException ie) {}
		if(!gameOver)
			pausedText.drawMe(g);
	}

	public void resume() {
		System.gc();
		paused = false;
		SpaceText.addFadingMessage("Game resumed.",texts);
	}

	public void endGame() {
		paused = true;
		stopLoopSounds();
		stx = new SpaceText("GAME OVER",GAME_LEFT+10,GAME_TOP+20,10,Color.white);
		stx.drawMe(g);
		if(playerStats.score > hsManager.lowestScore())
			hsManager.newHighScore = playerStats.score;
		goDialog = new GameOverDialog(this,true,toolkit.getScreenSize(),hsManager,currentLevel.getNumber());
		removeKeyListener(this);
	}

	public void returnToMenu() {
		hsManager.highScoreWindow.dispose();
		setVisible(false);
		otherInit();
		rs = new RaiderStartup(errManager,hsManager);
		rs.reset(this);
		rs.show();
		rs.repaint();
	}

	public void quit() {
		paused = true;
		mainThread = null;
		stopLoopSounds();
		preferences.save(soundEnabled,musicEnabled,useParticleEffects,fadeSmallDebris,calcLag);
		rs = null;
		g.dispose();
		g = null;
		errManager.close();
		System.exit(0);
	}

	public static int getTickCount() {
		return tickCounter;
	}

	// WindowListener interface methods
	public void windowDeactivated(WindowEvent e) {
		if(!loading && !paused)
			pause();
	}
	public void windowActivated(WindowEvent e) {
		if(!endGameNow && !loading && paused) {
			redraw();
			pausedText.drawMe(g);
		}
	}
	public void windowClosing(WindowEvent e) {
		quit();
	}
	
	// KeyListener interface methods
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		key = e.getKeyCode();
		if(key == keys.thrust)
			playerShip.applyThrust();
		else if(key == keys.left)
			playerShip.setRotation(-1);
		else if(key == keys.right)
			playerShip.setRotation(1);
		else if(key == keys.shield)
			playerShip.activateShield();
	}
	public void keyReleased(KeyEvent e) {
		key = e.getKeyCode();
		if(key == keys.thrust)
			playerShip.stopThrust();
		else if(key == keys.left || key == keys.right)
			playerShip.setRotation(0);
		else if(key == keys.shield)
			playerShip.deactivateShield();
		else if(key == keys.fire) {
			if(playerShip.fire()) {
				if(playerStats.bonus > SHOT_PEN)
					playerStats.bonus -= SHOT_PEN;
				else if(playerStats.bonus > 0)
					playerStats.bonus = 0;
			}
		} else
			KeyManager.checkOtherKeys(this,key,texts);
	}

	// MAIN
	public static void main(String[] args) {
		ErrorLogManager errManager = new ErrorLogManager(errorLogFile);
		HighScoreManager hsManager = new HighScoreManager(highScoreFile,HIGH_SCORE_LIST_LENGTH);
		RaiderStartup rs = new RaiderStartup(errManager,hsManager);
		AsterRaider a = new AsterRaider(rs,errManager,hsManager);
		rs.startLoading();
		a.setIconImage(rs.titleAt(0));
		a.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		rs.changePercent(25);
		a.initialize();
		rs.changePercent(35);
		a.loadPrefs();
		rs.changePercent(40);
		a.loadSprites();
		rs.changePercent(65);
		a.loadSounds();
		rs.changePercent(85);
		rs.changeString("Wrapping up...");
		a.otherInit();
		rs.changePercent(100);
		rs.finishLoading(a);
	}

}

