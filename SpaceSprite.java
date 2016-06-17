// SpaceSprite class
// by Mike Lee

// Useful for images with multiple frames

import java.awt.*;
import java.awt.image.*;

public class SpaceSprite {
	// Fields
	private Image[] frames;
	private int rows;
	private int cols;
	private int width;
	private int height;
	private int trim;
	private int currentFrame;
	private int numFrames;

	// Constructors
	public SpaceSprite(Image image) {
		this(image,1,1,0);
	}

	public SpaceSprite(Image image, int t) {
		this(image,1,1,t);
	}

	public SpaceSprite(Image image, int r, int c, int t) {
		rows = r;
		cols = c;
		numFrames = rows*cols;
		frames = new Image[rows*cols];
		width = image.getWidth(null)/cols;
		height = image.getHeight(null)/rows;
		trim = t;
		currentFrame = 0;
		Toolkit tk = Toolkit.getDefaultToolkit();
		for(int row=0; row<rows; row++)
			for(int col=0; col<cols; col++) {
				int[] pixels = new int[width*height];
				PixelGrabber pg = new PixelGrabber(image,col*width,row*height,width,height,pixels,0,width);
				try {
					pg.grabPixels();
				} catch(InterruptedException ie) {
					AsterRaider.errManager.handleImageError(ie,"Can't generate sprite frames",image);
				}
				MemoryImageSource mis = new MemoryImageSource(width,height,pixels,0,width);
				frames[row*cols+col] = tk.createImage(mis);
			}
	}

	public SpaceSprite(SpaceSprite ss) {
		frames = ss.frames;
		rows = ss.rows;
		cols = ss.cols;
		width = ss.width;
		height = ss.height;
		trim = ss.trim;
		currentFrame = ss.currentFrame;
		numFrames = ss.numFrames;
	}

	// Methods
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTrim() {
		return trim;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public int totalFrames() {
		return numFrames;
	}

	public void incFrame() {
		if(currentFrame == numFrames-1)
			currentFrame = 0;
		else
			currentFrame++;
	}

	public void decFrame() {
		if(currentFrame == 0)
			currentFrame = numFrames-1;
		else
			currentFrame--;
	}

	public void setFrame(int f) {
		currentFrame = f;
	}

	public void drawMe(Graphics g, int x, int y, ImageObserver observer) {
		g.drawImage(frames[currentFrame],x-width/2,y-height/2,observer);
	}

}

