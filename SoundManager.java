// SoundManager class for AsterRaider
// by Mike Lee

import java.awt.Toolkit;
import java.io.*;

public class SoundManager {
	// Fields
	private Toolkit toolkit;
	private AUSound[] sounds;
	private int numSounds;
	private int numLoopSounds;

	// Constructor
	public SoundManager(Toolkit tk, File soundListFile) {
		toolkit = tk;
		try {
			FileReader fr = new FileReader(soundListFile);
			BufferedReader br = new BufferedReader(fr);
			numSounds = Integer.parseInt(br.readLine());
			numLoopSounds = Integer.parseInt(br.readLine());
			sounds = new AUSound[numSounds+numLoopSounds];
			String filename;
			for(int i=0; i<numSounds; i++) {
				filename = br.readLine();
				sounds[i] = new AUSound(getClass().getResource("sounds/"+filename));
			}
			for(int i=numSounds; i<numSounds+numLoopSounds; i++) {
				filename = br.readLine();
				sounds[i] = new AUSound(getClass().getResource("sounds/"+filename));
			}
			br.close();
			fr.close();
		} catch(IOException ioe) {
			AsterRaider.errManager.catchException(ioe,"Can't load sounds");
		}
	}

	// Methods
	public AUSound soundAt(int id) {
		return sounds[id];
	}

	public AUSound[] getSounds() {
		AUSound[] temp = new AUSound[numSounds];
		for(int i=0; i<numSounds; i++)
			temp[i] = sounds[i];
		return temp;
	}

	public AUSound[] getLoopSounds() {
		AUSound[] temp = new AUSound[numLoopSounds];
		for(int i=0; i<numLoopSounds; i++)
			temp[i] = sounds[i+numSounds];
		return temp;
	}

}

