package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import helpers.GColors;

// Panel that displays logs of game events
public class LogScreen extends JPanel {

	// Generated to prevent warnings
	private static final long serialVersionUID = 1L;
	
	private static JPanel innerPanel = new JPanel();
	
	// Default font size of log messages
	private static int fontSize;
	
	// Max number of logs
	private static int maxLogs = 5;
	
	// Stack of currently queued log messages waiting to be displayed
	private static GLog[] logStack;
	
	// List of logs made to the game so far
	private static JLabel[] gameLogs;
	
	// Width and height of the log screen
	private static int lWidth = GameScreen.getGWidth();
	private static int lHeight = LogScreen.lWidth / 6;
	
	// Prefix before any log
	private static final String logPrefix = "+ ";
	
	// Color of the LogScreen
	private static final Color tint = new Color(221, 221, 136);
	
	protected LogScreen() {
		super();
		
		// Retrieve the scale factor from the GameInitializer
		double scaleFactor = GameInitializer.scaleFactor;
		
		// If screen is too small, lower number of maximum logs
		if (scaleFactor <= 0.45) {
			maxLogs = 3;
		} else if (scaleFactor <= 0.75) {
			maxLogs = 4;
		}
		
		// Set font size based off how many logs can fit on the screen
		fontSize = (int) (90 / maxLogs);
		
		// Initialize arrays
		gameLogs = new JLabel[maxLogs];
		logStack = new GLog[maxLogs];
		for (int i = 0; i < maxLogs; i++) {
			logStack[i] = new GLog("");
		}
		
		// Set font to new scaled size
		fontSize = (int) Math.floor(fontSize * GameInitializer.scaleFactor);
		
		// Set preferred size of the the log screen
		Dimension size = new Dimension(lWidth, lHeight);
		setPreferredSize(size);
		
		// Set background color
		setBackground(tint);
		
		// Initialize inner panel
		innerPanel.setLayout(new GridLayout(maxLogs, 1));
		for (int i = 0; i < maxLogs; i++) {
			JLabel label = new JLabel();
			label.setFont(new Font(Font.SERIF, Font.BOLD, fontSize));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			gameLogs[i] = label;
			innerPanel.add(label);
		}
		innerPanel.setBackground(tint);
		add(innerPanel);
	}
	
	// Cues a message up for the LogScreen
	public static void log(String message, Color color) {
		// Shift every message up
		for (int i = 1; i < maxLogs; i++) {
			logStack[i - 1] = logStack[i];
		}
		// Log new message at bottom (top of stack)
		logStack[maxLogs - 1] = new GLog((logPrefix + message), color);
	}
	
	// Cues a message up for the LogScreen
	public static void log(String message) {
		log(message, GColors.BASIC);
	}
	
	protected static void displayLogs() {
		// Display all the new labels
		int count = 0;
		for (GLog log: logStack) {
			if (!log.isEmpty()) {
				gameLogs[count].setText(log.message);
				gameLogs[count].setForeground(log.color);
				count += 1;
			}
		}
		
		// Remove 'new' indicator from logs that have it
		for (int j = 0; j < maxLogs; j++) {
			GLog currentLog = logStack[j];
			if ((currentLog != null) && currentLog.message.startsWith(logPrefix)) {
				logStack[j].message = currentLog.message.substring(logPrefix.length(), currentLog.message.length());
			}
		}
		
		// Repaint the panel to reflect the changes
		innerPanel.repaint();
		
	}

	// *******************
	// Getters and Setters
	
	public static int getLWidth() {
		return LogScreen.lWidth;
	}
	
	public static int getLHeight() {
		return LogScreen.lHeight;
	}
}
