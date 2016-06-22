// PlayerStats class for AsterRaider
// by Mike Lee

import java.awt.*;

public class PlayerStats {
	// Fields
	public int level;
	public int lives;
	public int score;
	public int bonus;

	// Constructor
	public PlayerStats() {
		level = 0;
		lives = AsterRaider.INIT_LIVES;
		score = 0;
		bonus = 0;
	}

	// Methods
	public void runBonusCalc(Graphics g, AUSound clickSound) {
		FontMetrics fm = g.getFontMetrics();
		int xMid = AsterRaider.GAME_LEFT+AsterRaider.GAME_WIDTH/2;
		int yMid = AsterRaider.GAME_TOP+AsterRaider.GAME_HEIGHT/2;
		String str, bstr, sstr;
		if(level > 1) { // bonus calculation/display
			str = "Level "+(level-1)+" completed...";
			g.drawString(str,xMid-fm.stringWidth(str)/2,yMid-fm.getHeight()/2-26);
			try {
				Thread.sleep(1000);
			} catch(InterruptedException ie) {}
			bstr = "Bonus: "+bonus;
			g.drawString(bstr,xMid-fm.stringWidth(bstr)/2,yMid-fm.getHeight()/2-8);
			try {
				Thread.sleep(1000);
			} catch(InterruptedException ie) {}
			if(bonus > 0) {
				while(bonus > 0) {
					g.setColor(Color.black);
					bstr = "Bonus: "+bonus;
					g.drawString(bstr,xMid-fm.stringWidth(bstr)/2,yMid-fm.getHeight()/2-8);
					sstr = "Score: "+score;
					g.drawString(sstr,xMid-fm.stringWidth(sstr)/2,yMid-fm.getHeight()/2+8);
					if(bonus >= 50) {
						bonus -= 50;
						clickSound.play();
						score += 50;
					} else {
						score += bonus;
						bonus = 0;
					}
					g.setColor(Color.white);
					bstr = "Bonus: "+bonus;
					g.drawString(bstr,xMid-fm.stringWidth(bstr)/2,yMid-fm.getHeight()/2-8);
					sstr = "Score: "+score;
					g.drawString(sstr,xMid-fm.stringWidth(sstr)/2,yMid-fm.getHeight()/2+8);
					try {
						Thread.sleep(2);
					} catch(InterruptedException ie) {}
				}
			} else {
				sstr = "Sorry!";
				g.drawString(sstr,xMid-fm.stringWidth(sstr)/2,yMid-fm.getHeight()/2+8);
			}
			str = "Get ready for Level "+level+"...";
			g.drawString(str,xMid-fm.stringWidth(str)/2,yMid-fm.getHeight()/2+26);
		} else {
			str = "Get Ready for Level "+level+"...";
			g.drawString(str,xMid-fm.stringWidth(str)/2,yMid-fm.getHeight()/2-9);
			str = "(Press F1 during game for help)";
			g.drawString(str,xMid-fm.stringWidth(str)/2,yMid-fm.getHeight()/2+9);
		}
	}

	public void display(Graphics g, int currentHighestScore) {
		String str = new String("Level: "+level+"     Lives: "+lives+"     Score: "+score+"     Bonus: "+bonus);
		if(score <= currentHighestScore)
			str += "          Current High: "+currentHighestScore;
		else
			str += "          Prev. High: "+currentHighestScore;
		if(currentHighestScore > 0)
			str+=" (You: "+Integer.toString((int)((float)score/currentHighestScore*100))+"%)";
		g.setColor(Color.white);
		g.drawString(str,10,AsterRaider.GAME_HEIGHT-10);
	}

}
