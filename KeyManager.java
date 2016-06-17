// KeyManager class for AsterRaider
// by Mike Lee

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Vector;

public class KeyManager {
	// Constants
	public static final Pref[] DEFAULT_KEYS = {
		new Pref("thrust",KeyEvent.VK_UP),
		new Pref("left",KeyEvent.VK_LEFT),
		new Pref("right",KeyEvent.VK_RIGHT),
		new Pref("fire",KeyEvent.VK_SPACE),
		new Pref("shield",KeyEvent.VK_DOWN)
	};
	private static final int KEYS_LENGTH = DEFAULT_KEYS.length;

	// Fields
	private File keysFile;
	private Pref[] keySettings;
	public int thrust;
	public int left;
	public int right;
	public int fire;
	public int shield;

	// Constructor
	public KeyManager(File kFile) {
		keysFile = kFile;
		keySettings = new Pref[KEYS_LENGTH];
		// create key settings file if it doesn't exist
		if(!keysFile.exists()) {
			try {
				FileWriter fw = new FileWriter(keysFile);
				PrintWriter writer = new PrintWriter(fw);
				keySettings = DEFAULT_KEYS;
				for(int i=0; i<KEYS_LENGTH; i++)
					writer.println(keySettings[i]);
				writer.flush();
			} catch(IOException ioe) {
				AsterRaider.errManager.catchException(ioe,"Can't create new key settings file");
			}
		}
		// read key settings file
		try {
			FileReader fr = new FileReader(keysFile);
			BufferedReader br = new BufferedReader(fr);
			for(int i=0; i<KEYS_LENGTH; i++)
				keySettings[i] = new Pref(br.readLine());
			br.close();
			fr.close();
		} catch(IOException ioe) {
			AsterRaider.errManager.catchException(ioe,"Can't load key settings file");
		}
		thrust = keySettings[0].getValue();
		left = keySettings[1].getValue();;
		right = keySettings[2].getValue();;
		fire = keySettings[3].getValue();;
		shield = keySettings[4].getValue();;
	}

	// Methods
	public Pref getKeyPref(int ind) {
		return keySettings[ind];
	}

	public int getKey(String name) {
		for(int i=0; i<keySettings.length; i++)
			if(keySettings[i].getName().equals(name))
				return keySettings[i].getValue();
		return -999;
	}

	public void setKey(String name, int newVal) {
		for(int i=0; i<keySettings.length; i++)
			if(keySettings[i].getName().equals(name))
				keySettings[i].setValue(newVal);
	}

	public static void checkOtherKeys(AsterRaider ar, int key, Vector texts) {
		if(key == KeyEvent.VK_P) {
			if(ar.paused)
				ar.resume();
			else
				ar.pause();
		} else if(key == KeyEvent.VK_K) {
			String[] message = {	"KEY CONTROLS --",
						"UP: Thrust",
						"LEFT/RIGHT: Rotate ship",
						"DOWN: Activate shield",
						"SPACE: Fire primary weapon"
			};
			SpaceText.addFadingMessages(message,texts);
		} else if(key == KeyEvent.VK_F1) {
			String[] message = {	"HELP -- Key Commands: ",
						"F1: Help",
						"F3: Toggle music",
						"F4: Toggle sound",
						"F5: Toggle small debris fading",
						"F6: Toggle particle effects",
						"F9: Toggle lag calculation",
						"F10/Esc: abort game",
						"K: Show key controls",
						"S: Toggle sound",
						"P: Pause/resume game"
			};
			SpaceText.addFadingMessages(message,texts);
		} else if(key == KeyEvent.VK_F3) {
			if(ar.musicEnabled) {
				ar.musicEnabled = false;
				ar.toggleMusic(false);
				SpaceText.addFadingMessage("Music disabled",texts);
			} else {
				ar.musicEnabled = true;
				ar.toggleMusic(true);
				SpaceText.addFadingMessage("Music enabled.",texts);
			}
		} else if(key == KeyEvent.VK_S || key == KeyEvent.VK_F4) {
			if(ar.soundEnabled) {
				ar.soundEnabled = false;
				ar.stopLoopSounds();
				SpaceText.addFadingMessage("Sounds disabled",texts);
			} else {
				ar.soundEnabled = true;
				SpaceText.addFadingMessage("Sounds enabled.",texts);
			}
		} else if(key == KeyEvent.VK_F5) {
			if(ar.fadeSmallDebris) {
				ar.fadeSmallDebris = false;
				SpaceText.addFadingMessage("Small debris fading off.",texts);
			} else {
				ar.fadeSmallDebris = true;
				SpaceText.addFadingMessage("Small debris fading on.",texts);
			}
		} else if(key == KeyEvent.VK_F6) {
			if(ar.useParticleEffects) {
				ar.useParticleEffects = false;
				SpaceText.addFadingMessage("Particle effects off.",texts);
			} else {
				ar.useParticleEffects = true;
				SpaceText.addFadingMessage("Particle effects on.",texts);
			}
		} else if(key == KeyEvent.VK_L || key == KeyEvent.VK_F9) {
			if(ar.calcLag) {
				ar.calcLag = false;
				SpaceText.addFadingMessage("Lag calculation off.",texts);
			} else {
				ar.calcLag = true;
				SpaceText.addFadingMessage("Lag calculation on.",texts);
			}
		} else if(key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_F10) {
			ar.endGameNow = true;
			ar.endGame();
		}
	}
}

