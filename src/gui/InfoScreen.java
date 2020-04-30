package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import characters.GCharacter;
import helpers.ImageHandler;
import items.GItem;

// Panel that displays information about focused items and enemies
public class InfoScreen extends JPanel {

	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Width and Height of the Info Screen
	private static int infoHeight = (GameScreen.getGHeight() + LogScreen.getLHeight()) - InventoryScreen.getInvHeight();
	private static int infoWidth = InventoryScreen.getInvWidth();
	
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
	
	// Name Panel fields
	private static JPanel namePanel = new JPanel();
	private static JLabel nameInfo = new JLabel();
	private static int nameWidth = 230;
	private static int nameHeight = 50;
	private static int nameFontSize = 24;
	private static Font usualNameFont;
	
	// Object Image Panel fields
	private static EntityImagePanel entityImagePanel = new EntityImagePanel();
	private static GCharacter focusedNPC = null;
	private static GItem focusedItem = null;
	private static Image itemImage;
	private static Image npcImage;
	private static Image tileImage;
	private static int npcWidth = 180;
	private static int npcHeight = 180;
	private static double upScaleFactor = ((double)npcWidth) / GameInitializer.tileArtSize;
	
	// Item Description Textbox fields
	private static JTextArea descBox = new JTextArea();
	private static int descWidth = 230;
	private static int descHeight = 185;
	private static int descFontSize = 20;
	private static int descSidePadSize = 8;
	private static Font usualDescFont;
	
	// Constructor
	protected InfoScreen() {
		super();
		
		// Set preferred size of the the log screen
		Dimension size = new Dimension(infoWidth, infoHeight);
		setPreferredSize(size);
		
		// Set vertical Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Set background color
		setBackground(new Color(179, 179, 179));
		
		// Scale down dimensions and fonts
		hpWidth = (int) (hpWidth * GameInitializer.scaleFactor);
		hpHeight = (int) (hpHeight * GameInitializer.scaleFactor);
		hpFontSize = (int) (hpFontSize * GameInitializer.scaleFactor);
		arWidth = (int) (arWidth * GameInitializer.scaleFactor);
		arHeight = (int) (arHeight * GameInitializer.scaleFactor);
		arFontSize = (int) (arFontSize * GameInitializer.scaleFactor);
		nameWidth = (int) (nameWidth * GameInitializer.scaleFactor);
		nameHeight = (int) (nameHeight * GameInitializer.scaleFactor);
		nameFontSize = (int) (nameFontSize * GameInitializer.scaleFactor);
		npcWidth = (int) (npcWidth * GameInitializer.scaleFactor);
		npcHeight = (int) (npcHeight * GameInitializer.scaleFactor);
		descWidth = (int) (descWidth * GameInitializer.scaleFactor);
		descHeight = (int) (descHeight * GameInitializer.scaleFactor);
		descFontSize = (int) (descFontSize * GameInitializer.scaleFactor);
		descSidePadSize = (int) (descSidePadSize * GameInitializer.scaleFactor);
		
		// Set up entity image panel
		updateEntityImage();
		add(entityImagePanel);
		entityImagePanel.setPreferredSize(new Dimension(npcWidth, npcHeight));
		
		// Vertical spacer
		add(Box.createVerticalGlue());
		
		// Set up name panel
		nameInfo.setText("-");
		nameInfo.setHorizontalAlignment(SwingConstants.CENTER);
		usualNameFont = new Font(Font.SERIF, Font.BOLD, nameFontSize);
		nameInfo.setFont(usualNameFont);
		namePanel.setLayout(new GridLayout(1, 1));
		namePanel.add(nameInfo);
		//--
		add(namePanel);
		namePanel.setPreferredSize(new Dimension(nameWidth, nameHeight));
		
		// Vertical spacer
		add(Box.createVerticalGlue());
		
		// Set up health panel
		healthInfo.setText("HP: - / -");
		healthInfo.setHorizontalAlignment(SwingConstants.CENTER);
		healthInfo.setFont(new Font(Font.SERIF, Font.BOLD, hpFontSize));
		healthPanel.setLayout(new GridLayout(1, 1));
		healthPanel.add(healthInfo);
		//--
		add(healthPanel);
		healthPanel.setPreferredSize(new Dimension(hpWidth, hpHeight));
		
		// Set up armor panel
		armorInfo.setText("DEF: -");
		armorInfo.setHorizontalAlignment(SwingConstants.CENTER);
		armorInfo.setFont(new Font(Font.SERIF, Font.BOLD, arFontSize));
		armorPanel.setLayout(new GridLayout(1, 1));
		armorPanel.add(armorInfo);
		armorPanel.setBackground(new Color(130, 130, 130));
		//--
		add(armorPanel);
		armorPanel.setPreferredSize(new Dimension(arWidth, arHeight));
		
		// Vertical spacer
		add(Box.createVerticalGlue());
		
		// Set up description panel
		descBox.setBackground(Color.LIGHT_GRAY);
		descBox.setBorder(BorderFactory.createEmptyBorder(0, descSidePadSize, 0, descSidePadSize));
		usualDescFont = new Font(Font.SERIF, Font.BOLD, descFontSize);
		descBox.setFont(usualDescFont);
		descBox.setLineWrap(true);
		descBox.setWrapStyleWord(true);
		descBox.setEditable(false);
		descBox.setOpaque(true);
		descBox.setFocusable(false);
		descBox.setVisible(true);
		shrinkDescBox();
		//--
		add(InfoScreen.descBox);
	}
	
	// Updates the values in the health panel
	public static void updateHealthValues() {
		// If we have no focused NPC, return
		if (focusedNPC == null) {
			return;
		}
		
		// Get HP values
		int hp = focusedNPC.getCurrentHP();
		int max = focusedNPC.getMaxHP();
		
		// Set Health text
		healthInfo.setText("HP: " + Integer.toString(hp) + " / " + Integer.toString(max));
		
		// Based on percentage of health left, get color
		Color healthColor;
		if (hp > (max/2)) {
			healthColor = new Color(102, 255, 102);
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
	
	public static void updateArmorValues() {
		// If we have no focused NPC, return
		if (focusedNPC == null) {
			return;
		}
		
		// Get armor value
		int armor = focusedNPC.getArmor();
		
		// Set armor text
		armorInfo.setText("DEF: " + Integer.toString(armor));
		
		// Repaint the panel
		armorPanel.repaint();
	}
	
	// Updates the values in the description panel
	public static void updateDescValues() {
		// If we have no focused Item, check for focused NPC
		if (focusedItem == null) {
			// If no focused NPC either, return
			if (focusedNPC == null) {
				return;
			} else {
				// Populate Description Box with NPC description
				
				// Set new text
				descBox.setText(focusedNPC.getStatDescString());
				
				// Repaint the panel
				descBox.repaint();
			}
		} else {
			// Populate Description Box with Item description
			
			// Set new text
			descBox.setText(focusedItem.getDesc());
			
			// Repaint the panel
			descBox.repaint();
		}
	}
	
	// Updates the values in the health panel
	public static void updateNameValues() {
		// Initialize name variable
		String name = "";
		
		// If we have no focused NPC, check for focused item
		if (focusedNPC == null) {
			// If no focused item either, return
			if (focusedItem == null) {
				return;
			} else {
				// Get the name value of the item
				name = focusedItem.getName();
			}
		} else {
			// Get the name value of the NPC
			name = focusedNPC.getName();
		}
		
		// Shrink the font if the name is too long
		if (name.length() > 16) {
			nameInfo.setFont(new Font(Font.SERIF, Font.BOLD, nameFontSize * 16 / (name.length())));
		} else {
			nameInfo.setFont(usualNameFont);
		}
		
		// Set Health text
		nameInfo.setText(name);
		
		// Repaint the panel
		namePanel.repaint();
	}
	
	public static void updateEntityImage() {
		// If we don't have a NPC to follow yet, then return
		if (focusedNPC == null) {
			if (focusedItem == null) {
				return;
			} else {
				// Paint item image
				
				// Get new scale factor
				double scaleFactor = upScaleFactor;
				
				// Get item image
				try {
					File file = new File(focusedItem.getImagePath());
					URL url = file.toURI().toURL();
					itemImage = new ImageIcon(url).getImage();
					itemImage = ImageHandler.scaleImage(itemImage, npcWidth, npcHeight, GameInitializer.scaleFactor, GameInitializer.scaleFactor);
				} catch (Exception ex) {
					// Let us know if we can't find the item image
					System.out.println("'" + focusedItem.getImagePath() + "' not found.");
					ex.printStackTrace();
				}
				
				// Set item as new entity image
				itemImage = ImageHandler.scaleImage(itemImage, npcWidth, npcHeight, scaleFactor, scaleFactor);
				entityImagePanel.setForegroundImage(itemImage);
				
				// Set blank tile image
				entityImagePanel.setBackgroundImage(null);
			}
		} else {
			// Get position of NPC
			int xPos = focusedNPC.getXPos();
			int yPos = focusedNPC.getYPos();
			
			// Get new scale factor
			double scaleFactor = upScaleFactor;
			
			// Sets tile image
			tileImage = GameScreen.getTile(xPos, yPos).bgImage;
			tileImage = ImageHandler.scaleImage(tileImage, npcWidth, npcHeight, scaleFactor, scaleFactor);
			entityImagePanel.setBackgroundImage(tileImage);
			
			// Gets corpse image or entity image of NPC tile depending on NPC's health
			if (focusedNPC.getCurrentHP() <= 0) {
				npcImage = GameScreen.getTile(xPos, yPos).corpseImage;
				npcImage = ImageHandler.scaleImage(npcImage, npcWidth, npcHeight, scaleFactor, scaleFactor);
				entityImagePanel.setForegroundImage(npcImage);
			} else {
				// Gets NPC image
				npcImage = GameScreen.getTile(xPos, yPos).entityImage;
				npcImage = ImageHandler.scaleImage(npcImage, npcWidth, npcHeight, scaleFactor, scaleFactor);
				entityImagePanel.setForegroundImage(npcImage);
			}
		}

		// Repaint the panel
		entityImagePanel.repaint();
	}
	
	// Defocus everything
	public static void defocusAll() {
		// Set focused entities to null
		focusedNPC = null;
		focusedItem = null;
		
		int xIndex = InventoryScreen.getXIndex();
		int yIndex = InventoryScreen.getYIndex();
		GItem newSelected = InventoryScreen.getTile(xIndex, yIndex).getItem();
		if (newSelected != null) {
			setItemFocus(newSelected);
			return;
		}
		
		// Reinitialize visible components
		healthPanel.setVisible(true);
		armorPanel.setVisible(true);
		
		// Reset text values
		healthInfo.setText("HP: - / -");
		armorInfo.setText("DEF: -");
		nameInfo.setText("-");
		descBox.setText("");
		shrinkDescBox();
		
		// Reset color values
		healthPanel.setBackground(Color.WHITE);
		
		// Reset Entity Image panel
		entityImagePanel.setBackgroundImage(null);
		entityImagePanel.setForegroundImage(null);
		
		// Repaint all affected panels
		healthPanel.repaint();
		namePanel.repaint();
		entityImagePanel.repaint();
	}
	
	// Defocuses if we are focused on an item
	public static void defocusIfItem() {
		if (focusedItem != null) {
			defocusAll();
		}
	}
	
	// Defocuses if we are focused on an NPC
	public static void defocusIfNPC() {
		if (focusedNPC != null) {
			defocusAll();
		}
	}
	
	// Update the whole screen
	public static void updateInfoScreen() {
		updateHealthValues();
		updateArmorValues();
		updateNameValues();
		updateDescValues();
		updateEntityImage();
	}
	
	private static void shrinkDescBox() {
		descBox.setPreferredSize(new Dimension(descWidth, (descHeight / 3)));
	}
	
	private static void growDescBox() {
		descBox.setPreferredSize(new Dimension(descWidth, descHeight));
	}
	
	// *******************
	// Getters and Setters
	
	// Sets InfoScreen to relay information about an NPC
	public static void setNPCFocus(GCharacter npc) {
		// If our NPC is null, defocus everything
		if (npc == null) {
			defocusAll();
		}
		
		// Set focus on NPC and not item
		focusedNPC = npc;
		focusedItem = null;
		
		// Make health panel visible and hide description box
		shrinkDescBox();
		healthPanel.setVisible(true);
		armorPanel.setVisible(true);
		
		// Update everything
		updateInfoScreen();
	}
	
	// Sets InfoScreen to relay information about an item
	public static void setItemFocus(GItem item) {
		// If our item is null, defocus everything
		if (item == null) {
			defocusAll();
		}
		
		// Set focus on item and not NPC
		focusedItem = item;
		focusedNPC = null;
		
		// Make description box visible and hide health panel
		growDescBox();
		healthPanel.setVisible(false);
		armorPanel.setVisible(false);
		
		// Update everything
		updateInfoScreen();
	}
	
	public static int getInfoWidth() {
		return infoWidth;
	}
	
	public static int getInfoHeight() {
		return infoHeight;
	}

}
