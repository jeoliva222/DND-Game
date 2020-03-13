package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import helpers.ImageHandler;
import managers.EntityManager;

// Panel that displays player information
public class StatusScreen extends JPanel {
	
	// Width and height of the log screen
	// 3/4 height by default
	private static int statusHeight = GameScreen.getGHeight() * 5 / 6;
	private static int statusWidth = StatusScreen.statusHeight * 6 / 15;

	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Health Panel fields
	private static JPanel healthPanel = new JPanel();
	private static JLabel healthInfo = new JLabel();
	private static int hpWidth = 180;
	private static int hpHeight = 50;
	private static int hpFontSize = 28;
	
	// Armor Panel fields
	private static JPanel armorPanel = new JPanel();
	private static JLabel armorInfo = new JLabel();
	private static int arWidth = 180;
	private static int arHeight = 50;
	private static int arFontSize = 28;
	
	// Player Image Panel fields
	private static EntityImagePanel playerImagePanel = new EntityImagePanel();
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
		
		// Set vertical Box layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Set background color
		this.setBackground(new Color(128, 128, 255));
		
		// Scale down dimensions and fonts
		StatusScreen.hpWidth = (int) (StatusScreen.hpWidth * GameInitializer.scaleFactor);
		StatusScreen.hpHeight = (int) (StatusScreen.hpHeight * GameInitializer.scaleFactor);
		StatusScreen.hpFontSize = (int) (StatusScreen.hpFontSize * GameInitializer.scaleFactor);
		StatusScreen.arWidth = (int) (StatusScreen.arWidth * GameInitializer.scaleFactor);
		StatusScreen.arHeight = (int) (StatusScreen.arHeight * GameInitializer.scaleFactor);
		StatusScreen.arFontSize = (int) (StatusScreen.arFontSize * GameInitializer.scaleFactor);
		StatusScreen.piWidth = (int) (StatusScreen.piWidth * GameInitializer.scaleFactor);
		StatusScreen.piHeight = (int) (StatusScreen.piHeight * GameInitializer.scaleFactor);
		StatusScreen.pWeaponWidth = (int) (StatusScreen.pWeaponWidth * GameInitializer.scaleFactor);
		StatusScreen.pWeaponHeight = (int) (StatusScreen.pWeaponHeight * GameInitializer.scaleFactor);
		StatusScreen.sWeaponWidth = (int) (StatusScreen.sWeaponWidth * GameInitializer.scaleFactor);
		StatusScreen.sWeaponHeight = (int) (StatusScreen.sWeaponHeight * GameInitializer.scaleFactor);
		
		// Set up player image panel
		StatusScreen.updatePlayerImage();
		add(StatusScreen.playerImagePanel);
		playerImagePanel.setPreferredSize(new Dimension(piWidth, piHeight));
		
		// Vertical spacer
		add(Box.createVerticalGlue());
		
		// Set up health panel
		StatusScreen.healthInfo.setText("HP: " + Integer.toString(EntityManager.getInstance().getPlayer().getCurrentHP())
				+ " / " + Integer.toString(EntityManager.getInstance().getPlayer().getMaxHP()));
		StatusScreen.healthInfo.setFont(new Font(Font.SERIF, Font.BOLD, StatusScreen.hpFontSize));
		StatusScreen.healthPanel.add(StatusScreen.healthInfo);
		add(StatusScreen.healthPanel);
		healthPanel.setPreferredSize(new Dimension(hpWidth, hpHeight));
		
		// Set up armor panel
		StatusScreen.armorInfo.setText("DEF: " + Integer.toString(EntityManager.getInstance().getPlayer().getArmor()));
		StatusScreen.armorInfo.setFont(new Font(Font.SERIF, Font.BOLD, StatusScreen.arFontSize));
		StatusScreen.armorPanel.add(StatusScreen.armorInfo);
		StatusScreen.armorPanel.setBackground(new Color(130, 130, 130));
		add(StatusScreen.armorPanel);
		armorPanel.setPreferredSize(new Dimension(arWidth, arHeight));
		
		// Vertical spacer
		add(Box.createVerticalGlue());
		
		// Set up the primary weapon panel
		add(StatusScreen.primaryWeaponPanel);
		primaryWeaponPanel.setPreferredSize(new Dimension(pWeaponWidth, pWeaponHeight));
		
		// Vertical spacer
		add(Box.createVerticalGlue());
		
		// Set up the primary weapon panel
		add(StatusScreen.sheathedWeaponBanner);
		sheathedWeaponBanner.setPreferredSize(new Dimension(sWeaponWidth, sWeaponHeight));
	}
	
	// Updates the values in the health panel
	public static void updateHealthValues() {
		// Get HP values
		int hp = EntityManager.getInstance().getPlayer().getCurrentHP();
		int max = EntityManager.getInstance().getPlayer().getMaxHP();
		
		// Set Health text
		StatusScreen.healthInfo.setText("HP: " + Integer.toString(hp)
				+ " / " + Integer.toString(max));
		
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
		int xPos = EntityManager.getInstance().getPlayer().getXPos();
		int yPos = EntityManager.getInstance().getPlayer().getYPos();
		
		// Get new scale factor
		double scaleFactor = upScaleFactor;
		
		// Sets tile image
		StatusScreen.tileImage = GameScreen.getTile(xPos, yPos).bgImage;
		StatusScreen.tileImage = ImageHandler.scaleImage(StatusScreen.tileImage, piWidth, piHeight, scaleFactor, scaleFactor);
		StatusScreen.playerImagePanel.setBackgroundImage(StatusScreen.tileImage);
		
		// Gets player image
		StatusScreen.playerImage = GameScreen.getTile(xPos, yPos).entityImage;
		StatusScreen.playerImage = ImageHandler.scaleImage(StatusScreen.playerImage, piWidth, piHeight, scaleFactor, scaleFactor);
		StatusScreen.playerImagePanel.setForegroundImage(StatusScreen.playerImage);

		// Repaint the panel
		StatusScreen.playerImagePanel.repaint();
	}
	
	// Updates the displayed armor value of the player
	public static void updateArmorValues() {
		// Fetch current armor value
		int armor = EntityManager.getInstance().getPlayer().getArmor();
		
		// Update text
		StatusScreen.armorInfo.setText("DEF: " + Integer.toString(armor));
		
		// Repaint the panel
		StatusScreen.armorPanel.repaint();
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
		StatusScreen.updateArmorValues();
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
