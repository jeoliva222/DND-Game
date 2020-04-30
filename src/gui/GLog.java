package gui;

import java.awt.Color;

import helpers.GColors;

/**
 * Class that represents an in-game log message on the LogScreen
 * @author jeoliva
 */
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
	
	/**
	 * Returns whether or not the log is empty
	 * @return True if message is null or empty | False otherwise
	 */
	public boolean isEmpty() {
		return (message == null || message.isEmpty());
	}
	
}
