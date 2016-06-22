// RaiderStartup class for AsterRaider
// by Mike Lee

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class RaiderStartup extends GameFrame implements ActionListener {
	// Constants
	public static final int LOAD_FRAME_WIDTH = 360;
	public static final int LOAD_FRAME_HEIGHT = 360;
	public static final int MIN_LOAD_TIME = 3000;
	public static final int BUTTON_WIDTH = 128;
	public static final int BUTTON_HEIGHT = 20;

	// Fields
	private AsterRaider ar;
	private HighScoreManager hsManager;
	private int state = -1; // 1 = loading; 2 = main menu; 3 = about
	private int loadPercent;
	private long loadStart;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Graphics g;
	private FontMetrics fm;
	private Font defaultFont;
	private Font boldFont;
	private Font smallFont;
	private String str;
	private String versionString;
	private Image[] titles;
	private int splashW;
	private int splashH;
	private Button startButton;
	private Button aboutButton;
	private Button highButton;
	private Button quitButton;
	private Button backButton;

	// Constructor
	public RaiderStartup(ErrorLogManager errManager, HighScoreManager hsManager) {
		super("AsterRaider",LOAD_FRAME_WIDTH,LOAD_FRAME_HEIGHT,false);
		loadTitles(errManager);
		this.hsManager = hsManager;
		int xtemp = LOAD_FRAME_WIDTH/2-128/2;
		// Initialize frame
		Dimension dim = toolkit.getScreenSize();
		setLocation(dim.width/2-LOAD_FRAME_WIDTH/2,dim.height/2-LOAD_FRAME_HEIGHT/2);
		addNotify();
		setBackground(Color.black);
		setForeground(Color.white);
		setIconImage(titles[0]);
		// Set graphics elements
		g = getGraphics();
		defaultFont = g.getFont();
		boldFont = new Font(defaultFont.getName(),Font.BOLD,defaultFont.getSize());
		smallFont = new Font("Sans Serif",Font.PLAIN,9);
		setLayout(null);
		splashW = titles[1].getWidth(this);
		splashH = titles[1].getHeight(this);
		// Start button
		startButton = new Button("Start Game");
		add(startButton);
		startButton.addActionListener(this);
		startButton.setForeground(Color.black);
		startButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		startButton.setLocation(xtemp,titles[1].getHeight(this)+48);
		startButton.setVisible(false);
		// About button
		aboutButton = new Button("About");
		add(aboutButton);
		aboutButton.addActionListener(this);
		aboutButton.setForeground(Color.black);
		aboutButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		aboutButton.setLocation(xtemp,titles[1].getHeight(this)+48+BUTTON_HEIGHT);
		aboutButton.setVisible(false);
		// High Scores button
		highButton = new Button("High Scores");
		add(highButton);
		highButton.addActionListener(this);
		highButton.setForeground(Color.black);
		highButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		highButton.setLocation(xtemp,titles[1].getHeight(this)+48+2*BUTTON_HEIGHT);
		highButton.setVisible(false);
		// Quit button
		quitButton = new Button("Quit");
		add(quitButton);
		quitButton.addActionListener(this);
		quitButton.setForeground(Color.black);
		quitButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		quitButton.setLocation(xtemp,titles[1].getHeight(this)+48+3*BUTTON_HEIGHT);
		quitButton.setVisible(false);
		// Back button (from About screen)
		backButton = new Button("Back");
		add(backButton);
		backButton.addActionListener(this);
		backButton.setForeground(Color.black);
		backButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		backButton.setLocation(xtemp,titles[1].getHeight(this)+48+3*BUTTON_HEIGHT);
		backButton.setVisible(false);
		// Other stuff
		versionString = "AsterRaider version "+AsterRaider.VERSION;
	}

	// Methods
	public void startLoading() {
		state = 0;
		loadPercent = 0;
		loadStart = System.currentTimeMillis();
		str = "Loading...";
		show();
		state = 1;
		repaint();
		try {
			Thread.sleep(500);
		} catch(InterruptedException ie) {}
	}

	private void loadTitles(ErrorLogManager errManager) {
		MediaTracker mt = new MediaTracker(this);
		URL[] titleImagePaths = {	getClass().getResource("images/icon.gif"),
						getClass().getResource("images/splash.gif")
		};
		titles = new Image[titleImagePaths.length];
		for(int i=0; i<titles.length; i++) {
			titles[i] = toolkit.getImage(titleImagePaths[i]);
			mt.addImage(titles[i],i);
		}
		try {
			mt.waitForAll();
		} catch (InterruptedException ie) {
			errManager.catchException(ie,"Can't load other images",true);
		}
	}

	public Image titleAt(int ind) {
		return titles[ind];
	}

	public void changePercent(int n) {
		loadPercent = n;
	}

	public void finishLoading(AsterRaider a) {
		repaint();
		try {
			Thread.sleep(Math.max(0,loadStart+MIN_LOAD_TIME-System.currentTimeMillis()));
		} catch(InterruptedException ie) {}
		reset(a);
	}

	public void changeString(String s) {
		int xtemp = LOAD_FRAME_WIDTH/2-splashW/2;
		g.setColor(Color.black);
		g.setFont(defaultFont);
		fm = g.getFontMetrics();
		g.drawString(str,xtemp,splashH+60);
		str = s;
		repaint();
	}

	public void reset(AsterRaider a) {
		ar = a;
		state = 2;
		erase();
		startButton.setVisible(true);
		aboutButton.setVisible(true);
		highButton.setVisible(true);
		quitButton.setVisible(true);
		backButton.setVisible(false);
		repaint();
	}

	public void showAbout() {
		state = 3;
		erase();
		startButton.setVisible(false);
		aboutButton.setVisible(false);
		highButton.setVisible(false);
		quitButton.setVisible(false);
		backButton.setVisible(true);
		repaint();
	}

	public void setState(int st) {
		state = st;
	}
	
	public void erase() {
		g.setColor(getBackground());
		g.fillRect(0,0,LOAD_FRAME_WIDTH,LOAD_FRAME_HEIGHT);
		g.setColor(getForeground());
	}

	public void paint(Graphics g) {
		if(state > -1) {
			int xtemp = LOAD_FRAME_WIDTH/2-splashW/2;
			g.drawImage(titles[1],xtemp,36,this);
			g.setColor(Color.white);
			fm = g.getFontMetrics();
			if(state == 1) {
				g.setFont(defaultFont);
				g.drawString(str,xtemp,splashH+60);
				g.setColor(Color.lightGray);
				g.drawRect(xtemp,splashH+72,splashW,12);
				g.setColor(Color.blue);
				g.fillRect(xtemp+1,splashH+73,(splashW-1)*loadPercent/100,11);
			} else if(state == 3) {
				g.setFont(boldFont);
				g.drawString("AsterRaider by Mike Lee",xtemp,splashH+60);
				g.setFont(defaultFont);
				g.drawString("Special thanks to Mark Lee and Kurt Preston.",xtemp,splashH+76);
				if(versionString.charAt(versionString.length()-2) == 'd')
					g.drawString("Note: This is a DEVELOPMENT version of AsterRaider.",xtemp,splashH+92);
			}
			g.setColor(Color.white);
			g.setFont(smallFont);
			fm = g.getFontMetrics();
			g.drawString(versionString,LOAD_FRAME_WIDTH-fm.stringWidth(versionString)-16,LOAD_FRAME_HEIGHT-16);
		}
	}

	public void windowActivated(WindowEvent e) {
		repaint();
	}
	public void windowClosing(WindowEvent e) {
		if(state > 1) {
			ar.errManager.close();
			dispose();
			System.exit(0);
		}
	}

	// ActionListener interface method
	public void actionPerformed(ActionEvent e) {
		Button source = (Button)e.getSource();
		if(source == startButton)
			ar.launch();
		else if(source == aboutButton) {
			showAbout();
		} else if(source == highButton) {
			hsManager.showWindow(toolkit.getScreenSize());
			dispose();
		} else if(source == quitButton)
			ar.quit();
		else if(source == backButton)
			reset(ar);
	}

}
