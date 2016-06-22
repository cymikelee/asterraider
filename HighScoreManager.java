// HighScoreManager class
// for AsterRaider (b/c of custom modifications)
// by Mike Lee

import java.awt.*;
import java.io.*;

public class HighScoreManager {
	// Fields
	public final String windowName = "AsterRaider High Scores";
	public final int HS_WIDTH = 400;
	public final int HS_HEIGHT = 360;
	private File highScoreFile;
	private HighScore[] highScoreList;
	private int listLength;
	public int newHighScore;
	private int highIndex;
	public HighScoreWindow highScoreWindow;
	private AsterRaider ar;

	// Constructor
	public HighScoreManager(File hsfile, int length) {
		highScoreFile = hsfile;
		listLength = length;
		highScoreList = new HighScore[listLength];
		newHighScore = -1;
		highIndex = -1;
		if(!highScoreFile.exists()) {
			try {
				FileWriter fw = new FileWriter(highScoreFile);
				PrintWriter writer = new PrintWriter(fw);
				for(int i=0; i<listLength; i++) {
					highScoreList[i] = new HighScore();
					writer.println(highScoreList[i]);
				}
				writer.flush();
			} catch(IOException ioe) {
				AsterRaider.errManager.catchException(ioe,"Can't create new high score list");
			}
		} else {
			try {
				FileReader fr = new FileReader(highScoreFile);
				BufferedReader br = new BufferedReader(fr);
				String entry, name;
				for(int i=0; i<listLength; i++) {
					entry = br.readLine();
					highScoreList[i] = new HighScore(entry);
				}
				br.close();
				fr.close();
			} catch(IOException ioe) {
				AsterRaider.errManager.catchException(ioe,"Can't load high score list");
			}
		}
	}

	// Methods
	public void initialize(AsterRaider a) {
		ar = a;
		highScoreWindow = new HighScoreWindow(windowName,HS_WIDTH,HS_HEIGHT,ar);
		highScoreWindow.setIconImage(ar.rs.titleAt(0));
	}

	public int highestScore() {
		return highScoreList[0].getScore();
	}

	public int lowestScore() {
		return highScoreList[listLength-1].getScore();
	}

	public void addToHighScoreList(String pl, int sc, int lv) {
		HighScore newhs = new HighScore(pl,sc,lv);
		HighScore old;
		int i = 0;
		while(sc <= highScoreList[i].getScore())
			i++;
		highIndex = i;
		for(; i<listLength; i++) {
			old = highScoreList[i];
			highScoreList[i] = newhs;
			newhs = new HighScore(old);
		}
		try {
			FileWriter fw = new FileWriter(highScoreFile);
			PrintWriter writer = new PrintWriter(fw);
			for(int j=0; j<listLength; j++)
				writer.println(highScoreList[j]);
			writer.flush();
			writer.close();
			fw.close();
		} catch(IOException ioe) {
			AsterRaider.errManager.catchException(ioe,"Can't write high score list to file");
		}
	}

	public void showWindow(Dimension screenSize) {
		// Create high score window
		highScoreWindow.setLocation(screenSize.width/2-HS_WIDTH/2,screenSize.height/2-HS_HEIGHT/2);
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		highScoreWindow.setLayout(gridBag);
		// Display title
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTH;
		Label lbl = new Label("HIGH SCORES:");
		lbl.setFont(new Font("Monospaced",Font.BOLD,24));
		if(highIndex == -1)
			lbl.setForeground(Color.red);
		else if(highIndex == 0)
			lbl.setForeground(Color.green);
		else
			lbl.setForeground(Color.yellow);
		gridBag.setConstraints(lbl,gbc);
		highScoreWindow.add(lbl);
		// Display column headings
		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		lbl = new Label("Name:");
		lbl.setFont(new Font("Monospaced",Font.BOLD,12));
		lbl.setForeground(Color.lightGray);
		gridBag.setConstraints(lbl,gbc);
		highScoreWindow.add(lbl);
		gbc.gridx = 2;
		gbc.weightx = 0;
		lbl = new Label("Score:");
		lbl.setFont(new Font("Monospaced",Font.BOLD,12));
		lbl.setForeground(Color.lightGray);
		gridBag.setConstraints(lbl,gbc);
		highScoreWindow.add(lbl);
		gbc.gridx = 3;
		lbl = new Label("Level:");
		lbl.setFont(new Font("Monospaced",Font.BOLD,12));
		lbl.setForeground(Color.lightGray);
		gridBag.setConstraints(lbl,gbc);
		highScoreWindow.add(lbl);
		// Display high score list
		int lv;
		for(int i=0; i<listLength; i++) {
			gbc.gridy = i+2;
			lv = highScoreList[i].getLevel();
			// place
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.EAST;
			lbl = new Label(" "+Integer.toString(i+1)+") ");
			lbl.setFont(new Font("Monospaced",Font.PLAIN,12));
			if(i == highIndex)
				lbl.setForeground(Color.cyan);
			else if(lv == 0)
				lbl.setForeground(Color.gray);
			gridBag.setConstraints(lbl,gbc);
			highScoreWindow.add(lbl);
			// name
			gbc.gridx = 1;
			gbc.weightx = 1;
			gbc.anchor = GridBagConstraints.WEST;
			lbl = new Label(highScoreList[i].getPlayer());
			lbl.setFont(new Font("Monospaced",Font.PLAIN,12));
			if(i == highIndex)
				lbl.setForeground(Color.cyan);
			else if(lv == 0)
				lbl.setForeground(Color.gray);
			gridBag.setConstraints(lbl,gbc);
			highScoreWindow.add(lbl);
			// score
			gbc.gridx = 2;
			gbc.weightx = 0;
			lbl = new Label(Integer.toString(highScoreList[i].getScore()));
			lbl.setFont(new Font("Monospaced",Font.PLAIN,12));
			if(i == highIndex)
				lbl.setForeground(Color.cyan);
			else if(lv == 0)
				lbl.setForeground(Color.gray);
			gridBag.setConstraints(lbl,gbc);
			highScoreWindow.add(lbl);
			// level
			gbc.gridx = 3;
			lbl = new Label(Integer.toString(lv));
			lbl.setFont(new Font("Monospaced",Font.PLAIN,12));
			if(i == highIndex)
				lbl.setForeground(Color.cyan);
			else if(lv == 0)
				lbl.setForeground(Color.gray);
			gridBag.setConstraints(lbl,gbc);
			highScoreWindow.add(lbl);
		}
		highScoreWindow.show();
	}
	
}

