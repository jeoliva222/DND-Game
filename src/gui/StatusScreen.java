package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import helpers.ImageHandler;
import managers.EntityManager;

/**
 * Panel class that displays player information
 * @author jeoliva
 */
public class StatusScreen extends JPanel {
	
	// Width and height of the log screen
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
	
	// Energy Panel fields
	private static JPanel energyPanel = new JPanel();
	private static JLabel energyInfo = new JLabel();
	private static int nrgWidth = 180;
	private static int nrgHeight = 50;
	private static int nrgFontSize = 28;
	
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
		
		// Set preferred size of the status screen
		Dimension size = new Dimension(statusWidth, statusHeight);
		setPreferredSize(size);
		
		// Set vertical Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Set background color
		setBackground(new Color(128, 128, 255));
		
		// Scale down dimensions and fonts
		hpWidth = (int) (hpWidth * GameInitializer.scaleFactor);
		hpHeight = (int) (hpHeight * GameInitializer.scaleFactor);
		hpFontSize = (int) (hpFontSize * GameInitializer.scaleFactor);
		arWidth = (int) (arWidth * GameInitializer.scaleFactor);
		arHeight = (int) (arHeight * GameInitializer.scaleFactor);
		arFontSize = (int) (arFontSize * GameInitializer.scaleFactor);
		nrgWidth = (int) (nrgWidth * GameInitializer.scaleFactor);
		nrgHeight = (int) (nrgHeight * GameInitializer.scaleFactor);
		nrgFontSize = (int) (nrgFontSize * GameInitializer.scaleFactor);
		piWidth = (int) (piWidth * GameInitializer.scaleFactor);
		piHeight = (int) (piHeight * GameInitializer.scaleFactor);
		pWeaponWidth = (int) (pWeaponWidth * GameInitializer.scaleFactor);
		pWeaponHeight = (int) (pWeaponHeight * GameInitializer.scaleFactor);
		sWeaponWidth = (int) (sWeaponWidth * GameInitializer.scaleFactor);
		sWeaponHeight = (int) (sWeaponHeight * GameInitializer.scaleFactor);
		
		// Set up player image panel
		updatePlayerImage();
		//--
		add(playerImagePanel);
		playerImagePanel.setPreferredSize(new Dimension(piWidth, piHeight));
		
		// Vertical spacer
		add(Box.createVerticalGlue());
		
		// Set up health panel
		healthInfo.setText("HP: " + Integer.toString(EntityManager.getInstance().getPlayer().getCurrentHP())
				+ " / " + Integer.toString(EntityManager.getInstance().getPlayer().getMaxHP()));
		healthInfo.setHorizontalAlignment(SwingConstants.CENTER);
		healthInfo.setFont(new Font(Font.SERIF, Font.BOLD, hpFontSize));
		healthPanel.setLayout(new GridLayout(1, 1));
		healthPanel.add(healthInfo);
		//--
		add(healthPanel);
		healthPanel.setPreferredSize(new Dimension(hpWidth, hpHeight));
		
		// Set up armor panel
		armorInfo.setText("DEF: " + Integer.toString(EntityManager.getInstance().getPlayer().getArmor()));
		armorInfo.setHorizontalAlignment(SwingConstants.CENTER);
		armorInfo.setFont(new Font(Font.SERIF, Font.BOLD, arFontSize));
		armorPanel.setLayout(new GridLayout(1, 1));
		armorPanel.add(armorInfo);
		armorPanel.setBackground(new Color(130, 130, 130));
		//--
		add(armorPanel);
		armorPanel.setPreferredSize(new Dimension(arWidth, arHeight));
		
		// Set up energy panel
		energyInfo.setText("NRG: " + Integer.toString(EntityManager.getInstance().getPlayer().getCurrentEnergy())
				+ " / " + Integer.toString(EntityManager.getInstance().getPlayer().getMaxEnergy()));
		energyInfo.setHorizontalAlignment(SwingConstants.CENTER);
		energyInfo.setFont(new Font(Font.SERIF, Font.BOLD, nrgFontSize));
		energyPanel.setLayout(new GridLayout(1, 1));
		energyPanel.add(energyInfo);
		//--
		add(energyPanel);
		energyPanel.setPreferredSize(new Dimension(nrgWidth, nrgHeight));
		
		// Vertical spacer
		add(Box.createVerticalGlue());
		
		// Set up the primary weapon panel
		add(primaryWeaponPanel);
		primaryWeaponPanel.setPreferredSize(new Dimension(pWeaponWidth, pWeaponHeight));
		
		// Vertical spacer
		add(Box.createVerticalGlue());
		
		// Set up the sheathed weapon panel
		add(sheathedWeaponBanner);
		sheathedWeaponBanner.setPreferredSize(new Dimension(sWeaponWidth, sWeaponHeight));
	}
	
	// Updates the values in the health panel
	public static void updateHealthValues() {
		// Get HP values
		int hp = EntityManager.getInstance().getPlayer().getCurrentHP();
		int max = EntityManager.getInstance().getPlayer().getMaxHP();
		
		// Set Health text
		healthInfo.setText("HP: " + Integer.toString(hp) + " / " + Integer.toString(max));
		
		// Based on percentage of health left, get color
		Color healthColor;
		if (hp > (max*3/4)) {
			healthColor = new Color(102, 255, 102);
		} else if (hp > (max/2)) {
			healthColor = new Color(204, 255, 102);
		} else if (hp > (max/4)) {
			healthColor = new Color(255, 255, 0);
		} else if (hp > 0) {
			healthColor = new Color(255, 153, 0);
		} else {
			healthColor = new Color(204, 51, 0);
		}
		
		// Set color background
		healthPanel.setBackground(healthColor);
		
		// Repaint the panel
		healthPanel.repaint();
	}
	
	// Updates the values in the energy panel
	public static void updateEnergyValues() {
		// Get energy values
		int nrg = EntityManager.getInstance().getPlayer().getCurrentEnergy();
		int max = EntityManager.getInstance().getPlayer().getMaxEnergy();
		
		// Set energy text
		energyInfo.setText("NRG: " + Integer.toString(nrg) + " / " + Integer.toString(max));
		
		// Based on percentage of energy left, set color
		int hue = 180;
		energyPanel.setBackground(new Color(hue, (int) Math.floor(hue - ((nrg / ((double) max)) * hue)), hue));
		
		// Repaint the panel
		energyPanel.repaint();
	}
	
	public static void updatePlayerImage() {
		// Get position of player
		int xPos = EntityManager.getInstance().getPlayer().getXPos();
		int yPos = EntityManager.getInstance().getPlayer().getYPos();
		
		// Get new scale factor
		double scaleFactor = upScaleFactor;
		
		// Sets tile image
		tileImage = GameScreen.getTile(xPos, yPos).bgImage;
		tileImage = ImageHandler.scaleImage(tileImage, piWidth, piHeight, scaleFactor, scaleFactor);
		playerImagePanel.setBackgroundImage(tileImage);
		
		// Gets player image
		playerImage = GameScreen.getTile(xPos, yPos).entityImage;
		playerImage = ImageHandler.scaleImage(playerImage, piWidth, piHeight, scaleFactor, scaleFactor);
		playerImagePanel.setForegroundImage(playerImage);

		// Repaint the panel
		playerImagePanel.repaint();
	}
	
	// Updates the displayed armor value of the player
	public static void updateArmorValues() {
		// Fetch current armor value
		int armor = EntityManager.getInstance().getPlayer().getArmor();
		
		// Update text
		armorInfo.setText("DEF: " + Integer.toString(armor));
		
		// Repaint the panel
		armorPanel.repaint();
	}
	
	// Updates the weapon panel(s)
	public static void updateWeapons() {
		primaryWeaponPanel.updateWeaponPanel();
		sheathedWeaponBanner.updateWeaponBanner();
	}
	
	// Updates everything on the status screen
	public static void updateStatusScreen() {
		updateHealthValues();
		updateEnergyValues();
		updatePlayerImage();
		updateArmorValues();
	}
	
	// *******************
	// Getters and Setters
	
	public static int getStatusWidth() {
		return statusWidth;
	}
	
	public static int getStatusHeight() {
		return statusHeight;
	}

}
