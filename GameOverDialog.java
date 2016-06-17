// GameOverDialog class for AsterRaider
// by Mike Lee

import java.awt.*;
import java.awt.event.*;

public class GameOverDialog extends Dialog implements ActionListener, KeyListener {
	// Fields
	public final int DIALOG_WIDTH = 320;
	public final int DIALOG_HEIGHT = 140;
	private Dimension screenSize;
	private HighScoreManager hsManager;
	private int levelNumber;
	private TextField nameField;
	public Button gookay;

	// Constructor
	public GameOverDialog(Frame parent, boolean modal, Dimension dim, HighScoreManager hsManager, int lv) {
		super(parent,modal);
		screenSize = dim;
		this.hsManager = hsManager;
		levelNumber = lv;
		addNotify();
		setLocation(screenSize.width/2-DIALOG_WIDTH/2,screenSize.height/2-DIALOG_HEIGHT/2);
		setSize(DIALOG_WIDTH,DIALOG_HEIGHT);
		setBackground(SystemColor.control);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		Font font = new Font("SansSerif",Font.BOLD,14);
		Label l1 = new Label("GAME OVER!");
		l1.setFont(font);
		add(l1);
		if(hsManager.newHighScore > 0) {
			Label l2 = new Label("You got a high score!");
			add(l2);
			nameField = new TextField("Anonymous",32);
			nameField.selectAll();
			nameField.addKeyListener(this);
			add(nameField);
		} else {
			Label l2 = new Label("Sorry, your score didn't make the high score list.");
			add(l2);
		}
		gookay = new Button("Okay");
		gookay.addActionListener(this);
		gookay.addKeyListener(this);
		add(gookay);
		setResizable(false);
		show();
	}

	// Methods
	public void close() {
		// Close dialog box
		if(hsManager.newHighScore > 0)
			hsManager.addToHighScoreList(nameField.getText(),hsManager.newHighScore,levelNumber);
		dispose();
		removeNotify();
		hsManager.showWindow(screenSize);
	}

	// ActionListener interface method
	public void actionPerformed(ActionEvent e) {
		close();
	}

	// KeyListener interface methods
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			close();
	}
	public void keyReleased(KeyEvent e) {}

}
