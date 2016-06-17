// ErrorLogManager class for AsterRaider
// by Mike Lee

import java.io.*;
import java.awt.Image;
import java.util.Vector;

public class ErrorLogManager {
	// Fields
	private FileOutputStream errFile;
	private PrintStream errStream;

	// Constructor
	public ErrorLogManager(File errorLogFile) {
		try {
			errFile = new FileOutputStream(errorLogFile);
			errStream = new PrintStream(errFile);
			System.setErr(errStream);
		} catch(IOException ioe) {
			System.out.println("Error: Can't open error log... :P");
		}
	}

	// Methods
	public void close() {
		try {
			errFile.close();
		} catch(IOException ioe) {}
		errStream.close();
	}

	public void catchException(Exception e, String message) {
		System.err.println("Error: "+message+" ("+e+" caught).\n");
	}

	public void catchException(Exception e, String message, boolean quit) {
		System.err.println("Error: "+message+" ("+e+" caught).\n");
		if(quit) {
			System.out.println("Error: "+message+".");
			System.exit(1);
		}
	}

	public void handleGameError(String message) {
		System.err.println("Error: "+message+".\n");
	}

	public void handleGameError(String message, boolean quit) {
		System.err.println("Error: "+message+".\n");
		if(quit) {
			System.out.println("Error: "+message+".");
			System.exit(1);
		}
	}

	public void handleImageError(Exception e, String message, Image image) {
		System.err.println("Error: "+message+".\n");
		System.err.println("Error: "+message+" ("+e+" caught).\n");
		System.err.println("\tImage: "+image+'\n');
		System.exit(1);
	}

	public void handleGameUpdateException(Exception e, Level currentLevel, int secondsCounter, Ship playerShip, SpaceObjectGroup rocks, SpaceObjectGroup explosions, SpaceObjectGroup particles, Vector texts) {
		System.err.println("Error: gameUpdate error");
		System.err.println("\tat Level "+currentLevel.getNumber()+", "+secondsCounter+" seconds");
		System.err.println("\tSpaceObjectGroup Listing:");
		System.err.println("\t\tship: " + playerShip);
		System.err.println("\t\tshots: " + playerShip.shots);
		System.err.println("\t\texhaust: " + playerShip.exhaust);
		System.err.println("\t\trocks: " + rocks);
		System.err.println("\t\texplosions: " + explosions);
		System.err.println("\t\tparticles: " + particles);
		System.err.println("\t\ttexts: " + texts);
		System.err.println("\tException Stack Trace:");
		e.printStackTrace();
		System.err.println("\tException Message: "+e.getMessage());
		System.err.println("\tLocalized Message: "+e.getLocalizedMessage()+'\n');
	}

}

