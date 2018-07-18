package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class DebuffScreen extends JPanel {

	// Width and height of the log screen
	private static int debuffHeight = (GameScreen.getGHeight() + LogScreen.getLHeight()) - StatusScreen.getStatusHeight();
	private static int debuffWidth = StatusScreen.getStatusWidth();

	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	public DebuffScreen() {
		super();
		
		//System.out.println("Left width: " + Integer.toString(DebuffScreen.getDebuffWidth()));
		
		// Set preferred size of the the log screen
		Dimension size = new Dimension(debuffWidth, debuffHeight);
		this.setPreferredSize(size);
		
		/// TEMP
		this.setBackground(Color.BLUE);
	}
	
	// *******************
	// Getters and Setters
	
	public static int getDebuffWidth() {
		return DebuffScreen.debuffWidth;
	}
	
	public static int getDebuffHeight() {
		return DebuffScreen.debuffHeight;
	}

}
