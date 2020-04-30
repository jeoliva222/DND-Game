package gui;

/**
 * Class containing static definitions for application sizing
 * @author jeoliva
 */
public class GameInitializer {

	// ***NEVER CHANGE*** //
	// These are constants for the original size of the different
	// dimensions of the art
	public static final int tileArtSize = 80; // Leave this at 80
	
	// Dimensions of the tiles on the GameScreen
	public static final int xDimen = 10; // Leave these at 10
	public static final int yDimen = 10; //
	
	
	//--------------------
	// Variables to change
	
	// Factor by which the entire game is scaled
	// Used to make game playable on smaller screens
	public static double scaleFactor = 1.0;
	
	// Offsets to window size to account for the frame's natural border dimensions
	// TODO - These might be unnecessary now
	
	// Old value = 18
	public static int xOffset = 18;
	
	// Old value = 40
	public static int yOffset = 40;
	
}
