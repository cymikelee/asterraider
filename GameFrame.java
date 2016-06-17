// GameFrame class
// by Mike Lee

import java.awt.*;
import java.awt.event.*;

public class GameFrame extends Frame implements WindowListener {
	// Fields
	public static final int DEFAULT_WIDTH = 320;
	public static final int DEFAULT_HEIGHT = 240;

	// Constructors
	public GameFrame() {
		this("Untitled GameFrame",DEFAULT_WIDTH,DEFAULT_HEIGHT);
	}

	public GameFrame(String title) {
		this(title,DEFAULT_WIDTH,DEFAULT_HEIGHT);
	}

	public GameFrame(int width, int height) {
		this("Untitled GameFrame",width,height);
	}

	public GameFrame(String title, int width, int height) {
		super(title);
		setSize(width,height);
		addWindowListener(this);
	}

	public GameFrame(String title, int width, int height, boolean resizable) {
		this(title,width,height);
		if(!resizable)
			setResizable(false);
	}

	// Methods for WindowListener interface
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {
		dispose();
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

}

