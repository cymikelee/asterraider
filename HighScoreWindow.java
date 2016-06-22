// HighScoreWindow class for AsterRaider
// by Mike Lee

import java.awt.*;
import java.awt.event.WindowEvent;

public class HighScoreWindow extends GameFrame {
	// Fields
	AsterRaider ar;

	// Constructor
	public HighScoreWindow(String title, int width, int height, AsterRaider ar) {
		super(title,width,height);
		this.ar = ar;
		addNotify();
		setResizable(false);
		setForeground(Color.white);
		setBackground(Color.black);
	}

	// Methods
	public void windowClosing(WindowEvent e) {
		ar.returnToMenu();
	}

}
