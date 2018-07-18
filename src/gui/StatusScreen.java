package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

import helpers.ImageHandler;
import managers.EntityManager;

// Panel that displays player information
public class StatusScreen extends JPanel {
	
	// Width and height of the log screen
	private static int statusHeight = GameScreen.getGHeight() * 3 / 4;
	private static int statusWidth = StatusScreen.statusHeight * 4 / 9;

	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Health Panel fields
	private static JPanel healthPanel = new JPanel();
	private static JLabel healthInfo = new JLabel();
	private static int hpWidth = 180;
	private static int hpHeight = 50;
	private static int hpFontSize = 28;
	
	// Player Image Panel fields
	private static EntityImagePanel playerImagePanel = new EntityImagePanel(0, 0);
	private static Image playerImage;
	private static Image tileImage;
	private static int piWidth = 180;
	private static int piHeight = 180;
	private static double upScaleFactor = ((double) piWidth) / GameInitializer.tileArtSize;
	
	// Equipped Weapon Stat Panel fields
	private static WeaponStatPanel primaryWeaponPanel = new WeaponStatPanel();
	private static int pWeaponWidth = 230;
	private static int pWeaponHeight = 160;
	
	// Sheathed Weapon Banner fields
	private static WeaponBanner sheathedWeaponBanner = new WeaponBanner();
	private static int sWeaponWidth = 230;
	private static int sWeaponHeight = 100;
	
	
	// Constructor
	protected StatusScreen() {
		super();
		
		// Set preferred size of the the log screen
		Dimension size = new Dimension(statusWidth, statusHeight);
		this.setPreferredSize(size);
		
		// Set null content layout
		this.setLayout(null);
		
		// Set background color
		this.setBackground(new Color(128, 128, 255));
		
		// Scale down dimensions and fonts
		StatusScreen.hpWidth = (int) (StatusScreen.hpWidth * GameInitializer.scaleFactor);
		StatusScreen.hpHeight = (int) (StatusScreen.hpHeight * GameInitializer.scaleFactor);
		StatusScreen.hpFontSize = (int) (StatusScreen.hpFontSize * GameInitializer.scaleFactor);
		StatusScreen.piWidth = (int) (StatusScreen.piWidth * GameInitializer.scaleFactor);
		StatusScreen.piHeight = (int) (StatusScreen.piHeight * GameInitializer.scaleFactor);
		StatusScreen.pWeaponWidth = (int) (StatusScreen.pWeaponWidth * GameInitializer.scaleFactor);
		StatusScreen.pWeaponHeight = (int) (StatusScreen.pWeaponHeight * GameInitializer.scaleFactor);
		StatusScreen.sWeaponWidth = (int) (StatusScreen.sWeaponWidth * GameInitializer.scaleFactor);
		StatusScreen.sWeaponHeight = (int) (StatusScreen.sWeaponHeight * GameInitializer.scaleFactor);
		
		// Set up health panel
		StatusScreen.healthInfo.setText("HP: " + Integer.toString(EntityManager.getPlayer().getCurrentHP())
				+ " / " + Integer.toString(EntityManager.getPlayer().getMaxHP()));
		StatusScreen.healthInfo.setFont(new Font(Font.SERIF, Font.BOLD, StatusScreen.hpFontSize));
		StatusScreen.healthPanel.add(StatusScreen.healthInfo);
		this.add(StatusScreen.healthPanel);
		StatusScreen.healthPanel.setBounds(statusWidth *9/54, statusHeight *10/27, hpWidth, hpHeight);
		
		// Set up player image panel
		StatusScreen.updatePlayerImage();
		this.add(StatusScreen.playerImagePanel);
		StatusScreen.playerImagePanel.setBounds(statusWidth *9/54, statusHeight / 18, piWidth, piHeight);
		
		// Set up the primary weapon panel
		this.add(StatusScreen.primaryWeaponPanel);
		StatusScreen.primaryWeaponPanel.setBounds(statusWidth *4/54, statusHeight *13/27, pWeaponWidth, pWeaponHeight);
		
		// Set up the primary weapon panel
		this.add(StatusScreen.sheathedWeaponBanner);
		StatusScreen.sheathedWeaponBanner.setBounds(statusWidth *4/54, statusHeight *21/27, sWeaponWidth, sWeaponHeight);
	}
	
	// Updates the values in the health panel
	public static void updateHealthValues() {
		// Get HP values
		int hp = EntityManager.getPlayer().getCurrentHP();
		int max = EntityManager.getPlayer().getMaxHP();
		
		// Set Health text
		StatusScreen.healthInfo.setText("HP: " + Integer.toString(EntityManager.getPlayer().getCurrentHP())
				+ " / " + Integer.toString(EntityManager.getPlayer().getMaxHP()));
		
		// Based on percentage of health left, get color
		Color healthColor;
		if(hp > (max*3/4)) {
			healthColor = new Color(102, 255, 102);
		} else if(hp > (max/2)) {
			healthColor = new Color(204, 255, 102);
		} else if(hp > (max/4)) {
			healthColor = new Color(255, 255, 0);
		} else if(hp > 0) {
			healthColor = new Color(255, 153, 0);
		} else {
			healthColor = new Color(204, 51, 0);
		}
		
		// Set color background
		StatusScreen.healthPanel.setBackground(healthColor);
		
		// Repaint the panel
		StatusScreen.healthPanel.repaint();
	}
	
	public static void updatePlayerImage() {
		// Get position of player
		int xPos = EntityManager.getPlayer().getXPos();
		int yPos = EntityManager.getPlayer().getYPos();
		
		// Get new scale factor
		double scaleFactor = upScaleFactor;
		
		// Sets tile image
		StatusScreen.tileImage = GameScreen.getTile(xPos, yPos).bgImage;
		StatusScreen.tileImage = ImageHandler.scaleImage(StatusScreen.tileImage, 180, 180, scaleFactor, scaleFactor);
		StatusScreen.playerImagePanel.setTImage(StatusScreen.tileImage);
		
		// Gets player image
		StatusScreen.playerImage = GameScreen.getTile(xPos, yPos).entityImage;
		StatusScreen.playerImage = ImageHandler.scaleImage(StatusScreen.playerImage, 180, 180, scaleFactor, scaleFactor);
		StatusScreen.playerImagePanel.setEImage(StatusScreen.playerImage);

		// Repaint the panel
		StatusScreen.playerImagePanel.repaint();
	}
	
	// Updates the weapon panel(s)
	public static void updateWeapons() {
		StatusScreen.primaryWeaponPanel.updateWeaponPanel();
		StatusScreen.sheathedWeaponBanner.updateWeaponBanner();
	}
	
	// Updates everything on the status screen
	public static void updateStatusScreen() {
		StatusScreen.updateHealthValues();
		StatusScreen.updatePlayerImage();
	}
	
	// *******************
	// Getters and Setters
	
	public static int getStatusWidth() {
		return StatusScreen.statusWidth;
	}
	
	public static int getStatusHeight() {
		return StatusScreen.statusHeight;
	}

}
