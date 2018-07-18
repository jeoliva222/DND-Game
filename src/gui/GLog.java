package gui;

import java.awt.Color;

import helpers.GColors;

// Class that represent an in-game log on the Log Screen
public class GLog {

	// Message that the log displays
	protected String message;
	
	// Color of the message
	protected Color color;
	
	// Constructor with specified color
	protected GLog(String message, Color color) {
		this.message = message;
		this.color = color;
	}
	
	// Constructor that defaults color to black
	protected GLog(String message) {
		this(message, GColors.BASIC);
	}
	
}
