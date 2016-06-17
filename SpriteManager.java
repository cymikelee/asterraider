// SpriteManager class
// by Mike Lee

import java.awt.*;
import java.io.*;
import java.net.URL;

public class SpriteManager {
	// Fields
	private MediaTracker mt;
	private Toolkit toolkit;
	private SpaceSprite[] sprites;
	private int attributes[];
	private int numSprites;

	// Constructor
	public SpriteManager(MediaTracker mt, Toolkit tk, File spriteListFile) {
		this.mt = mt;
		toolkit = tk;
		try {
			FileReader fr = new FileReader(spriteListFile);
			BufferedReader br = new BufferedReader(fr);
			numSprites = Integer.parseInt(br.readLine()); // number of sprites
			int lastID = Integer.parseInt(br.readLine()); // last sprite's ID
			sprites = new SpaceSprite[lastID+1];
			String[] filenames = new String[lastID+1];
			attributes = new int[numSprites*4];
			int id;
			for(int i=0; i<numSprites; i++) {
				br.readLine();
				id = Integer.parseInt(br.readLine()); // id
				filenames[id] = br.readLine(); // filename
				attributes[i*4] = id;
				attributes[i*4+1] = Integer.parseInt(br.readLine()); // rows
				attributes[i*4+2] = Integer.parseInt(br.readLine()); // cols
				attributes[i*4+3] = Integer.parseInt(br.readLine()); // trim
			}
			loadSprites(filenames);
			br.close();
			fr.close();
		} catch(IOException ioe) {
			AsterRaider.errManager.catchException(ioe,"Can't load sprite list",true);
		}
	}

	// Methods
	public SpaceSprite spriteAt(int id) {
		return sprites[id];
	}

	public SpaceSprite[] getSprites() {
		return sprites;
	}

	public void loadSprites(String[] filenames) {
		int curID;
		URL filepath;
		Image[] images = new Image[numSprites];
		for(int i=0; i<numSprites; i++) {
			curID = attributes[i*4];
			filepath = getClass().getResource("images/"+filenames[curID]);
			images[i] = toolkit.getImage(filepath);
			mt.addImage(images[i],i);
		}
		try {
			mt.waitForAll();
		} catch (InterruptedException ie) {
			System.out.println("Error: Can't load sprite images.");
			System.err.println("Error: Can't load sprite images (InterruptedException caught).\n");
			System.exit(1);
		}
		for(int i=0; i<numSprites; i++) {
			curID = attributes[i*4];
			sprites[curID] = new SpaceSprite(images[i],attributes[i*4+1],attributes[i*4+2],attributes[i*4+3]);
		}
	}

}

