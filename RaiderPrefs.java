// RaiderPrefs class for AsterRaider
// by Mike Lee

import java.io.*;

public class RaiderPrefs {
	// Constants
	public static final Pref[] DEFAULT_PREFS = {
		new Pref("sound=1"),
		new Pref("particles=1"),
		new Pref("fadeSmall=0"),
		new Pref("music=0"),
		new Pref("calcLag=0"),
		new Pref("boomSpt=24"),
		new Pref("bigBoomSpt=25"),
		new Pref("boomSnd=0"),
		new Pref("bigBoomSnd=1"),
		new Pref("piffSnd=2"),
		new Pref("clickSnd=4"),
		new Pref("musicLSnd=2")
	};
	private static final int PREFS_LENGTH = DEFAULT_PREFS.length;

	// Fields
	private File prefsFile;
	public Pref[] prefs;

	// Constructor
	public RaiderPrefs(File pFile) {
		prefsFile = pFile;
		prefs = new Pref[PREFS_LENGTH];
		// create prefs file if it doesn't exist
		if(!prefsFile.exists()) {
			try {
				FileWriter fw = new FileWriter(prefsFile);
				PrintWriter writer = new PrintWriter(fw);
				prefs = DEFAULT_PREFS;
				for(int i=0; i<PREFS_LENGTH; i++)
					writer.println(prefs[i]);
				writer.flush();
			} catch(IOException ioe) {
				AsterRaider.errManager.catchException(ioe,"Can't create new preferences file");
			}
		}
		// read prefs file
		try {
			FileReader fr = new FileReader(prefsFile);
			BufferedReader br = new BufferedReader(fr);
			for(int i=0; i<PREFS_LENGTH; i++)
				prefs[i] = new Pref(br.readLine());
			br.close();
			fr.close();
		} catch(IOException ioe) {
			AsterRaider.errManager.catchException(ioe,"Can't load preferences file");
		}
	}

	// Methods
	public Pref getPref(int ind) {
		return prefs[ind];
	}

	public int getValue(String name) {
		for(int i=0; i<prefs.length; i++)
			if(prefs[i].getName().equals(name))
				return prefs[i].getValue();
		return -999;
	}

	public boolean getBooleanValue(String name) {
		for(int i=0; i<prefs.length; i++) {
			if(prefs[i].getName().equals(name)) {
				if(prefs[i].getValue() == 0)
					return false;
				else
					return true;
			}
		}
		return false;
	}

	public void setValue(String name, int newVal) {
		for(int i=0; i<prefs.length; i++)
			if(prefs[i].getName().equals(name))
				prefs[i].setValue(newVal);
	}

	public boolean setBooleanValue(String name, boolean newVal) {
		for(int i=0; i<prefs.length; i++) {
			if(prefs[i].getName().equals(name)) {
				if(newVal)
					prefs[i].setValue(1);
				else
					prefs[i].setValue(0);
			}
		}
		return false;
	}

	public void save(boolean soundEnabled, boolean musicEnabled, boolean useParticleEffects, boolean fadeSmallDebris, boolean calcLag) {
		setBooleanValue("sound",soundEnabled);
		setBooleanValue("music",musicEnabled);
		setBooleanValue("particles",useParticleEffects);
		setBooleanValue("fadeSmall",fadeSmallDebris);
		setBooleanValue("calcLag",calcLag);
		try {
			FileWriter fw = new FileWriter(prefsFile);
			PrintWriter writer = new PrintWriter(fw);
			for(int i=0; i<PREFS_LENGTH; i++)
				writer.println(prefs[i]);
			writer.flush();
			writer.close();
			fw.close();
		} catch(IOException ioe) {
			AsterRaider.errManager.catchException(ioe,"Can't write preferences to file");
		}
	}

}
