package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
	private static int nameFontSize = 28;
	private static Font usualNameFont;
	
	// Object Image Panel fields
	private static EntityImagePanel entityImagePanel = new EntityImagePanel(0, 0);
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
	private static int descFontSize = 22;
	private static Font usualDescFont;
	
	// Constructor
	protected InfoScreen() {
		super();
		
		// Set preferred size of the the log screen
		Dimension size = new Dimension(infoWidth, infoHeight);
		this.setPreferredSize(size);
		
		// Set null content layout
		this.setLayout(null);
		
		// Set background color
		this.setBackground(new Color(179, 179, 179));
		
		// Scale down dimensions and fonts
		InfoScreen.hpWidth = (int) (InfoScreen.hpWidth * GameInitializer.scaleFactor);
		InfoScreen.hpHeight = (int) (InfoScreen.hpHeight * GameInitializer.scaleFactor);
		InfoScreen.hpFontSize = (int) (InfoScreen.hpFontSize * GameInitializer.scaleFactor);
		InfoScreen.arWidth = (int) (InfoScreen.arWidth * GameInitializer.scaleFactor);
		InfoScreen.arHeight = (int) (InfoScreen.arHeight * GameInitializer.scaleFactor);
		InfoScreen.arFontSize = (int) (InfoScreen.arFontSize * GameInitializer.scaleFactor);
		InfoScreen.nameWidth = (int) (InfoScreen.nameWidth * GameInitializer.scaleFactor);
		InfoScreen.nameHeight = (int) (InfoScreen.nameHeight * GameInitializer.scaleFactor);
		InfoScreen.nameFontSize = (int) (InfoScreen.nameFontSize * GameInitializer.scaleFactor);
		InfoScreen.npcWidth = (int) (InfoScreen.npcWidth * GameInitializer.scaleFactor);
		InfoScreen.npcHeight = (int) (InfoScreen.npcHeight * GameInitializer.scaleFactor);
		InfoScreen.descWidth = (int) (InfoScreen.descWidth * GameInitializer.scaleFactor);
		InfoScreen.descHeight = (int) (InfoScreen.descHeight * GameInitializer.scaleFactor);
		InfoScreen.descFontSize = (int) (InfoScreen.descFontSize * GameInitializer.scaleFactor);
		
		// Set up health panel
		InfoScreen.healthInfo.setText("HP: - / -");
		InfoScreen.healthInfo.setFont(new Font(Font.SERIF, Font.BOLD, InfoScreen.hpFontSize));
		InfoScreen.healthPanel.add(InfoScreen.healthInfo);
		this.add(InfoScreen.healthPanel);
		InfoScreen.healthPanel.setBounds(infoWidth *9/54, infoHeight *16/27, hpWidth, hpHeight);
		
		// Set up armor panel
		InfoScreen.armorInfo.setText("DEF: -");
		InfoScreen.armorInfo.setFont(new Font(Font.SERIF, Font.BOLD, InfoScreen.arFontSize));
		InfoScreen.armorPanel.add(InfoScreen.armorInfo);
		InfoScreen.armorPanel.setBackground(new Color(130, 130, 130));
		this.add(InfoScreen.armorPanel);
		InfoScreen.armorPanel.setBounds(infoWidth *9/54, infoHeight *77/108, arWidth, arHeight);
		
		// Set up description panel
		InfoScreen.descBox.setBackground(Color.LIGHT_GRAY);
		InfoScreen.usualDescFont = new Font(Font.SERIF, Font.BOLD, InfoScreen.descFontSize);
		InfoScreen.descBox.setFont(InfoScreen.usualDescFont);
		InfoScreen.descBox.setLineWrap(true);
		InfoScreen.descBox.setWrapStyleWord(true);
		InfoScreen.descBox.setEditable(false);
		InfoScreen.descBox.setOpaque(true);
		InfoScreen.descBox.setFocusable(false);
		InfoScreen.descBox.setVisible(true);
		InfoScreen.shrinkDescBox();
		this.add(InfoScreen.descBox);
		
		// Set up name panel
		InfoScreen.nameInfo.setText("-");
		InfoScreen.usualNameFont = new Font(Font.SERIF, Font.BOLD, InfoScreen.nameFontSize);
		InfoScreen.nameInfo.setFont(InfoScreen.usualNameFont);
		InfoScreen.namePanel.add(InfoScreen.nameInfo);
		this.add(InfoScreen.namePanel);
		InfoScreen.namePanel.setBounds(infoWidth *4/54, infoHeight *25/54, nameWidth, nameHeight);
		
		// Set up entity image panel
		InfoScreen.updateEntityImage();
		this.add(InfoScreen.entityImagePanel);
		InfoScreen.entityImagePanel.setBounds(infoWidth *9/54, infoHeight / 18, npcWidth, npcHeight);
	}
	
	// Updates the values in the health panel
	public static void updateHealthValues() {
		// If we have no focused NPC, return
		if(focusedNPC == null) {
			return;
		}
		
		// Get HP values
		int hp = focusedNPC.getCurrentHP();
		int max = focusedNPC.getMaxHP();
		
		// Set Health text
		InfoScreen.healthInfo.setText("HP: " + Integer.toString(hp)
				+ " / " + Integer.toString(max));
		
		// Based on percentage of health left, get color
		Color healthColor;
		if(hp > (max/2)) {
			healthColor = new Color(102, 255, 102);
		} else if(hp > 0) {
			healthColor = new Color(255, 153, 0);
		} else {
			healthColor = new Color(204, 51, 0);
		}
		
		// Set color background
		InfoScreen.healthPanel.setBackground(healthColor);
		
		// Repaint the panel
		InfoScreen.healthPanel.repaint();
	}
	
	public static void updateArmorValues() {
		// If we have no focused NPC, return
		if(focusedNPC == null) {
			return;
		}
		
		// Get armor value
		int armor = focusedNPC.getArmor();
		
		// Set armor text
		InfoScreen.armorInfo.setText("DEF: " + Integer.toString(armor));
		
		InfoScreen.armorPanel.repaint();
	}
	
	// Updates the values in the description panel
	public static void updateDescValues() {
		// If we have no focused Item, check for focused NPC
		if(focusedItem == null) {
			// If no focused NPC either, return
			if(focusedNPC == null) {
				return;
			} else {
				// Populate Description Box with NPC description
				
				// Set new text
				InfoScreen.descBox.setText(focusedNPC.getStatDescString());
				
				// Repaint the panel
				InfoScreen.descBox.repaint();
			}
		} else {
			// Populate Description Box with Item description
			
			// Set new text
			InfoScreen.descBox.setText(focusedItem.getDesc());
			
			// Repaint the panel
			InfoScreen.descBox.repaint();
		}
	}
	
	// Updates the values in the health panel
	public static void updateNameValues() {
		// Initialize name variable
		String name = "";
		
		// If we have no focused NPC, check for focused item
		if(focusedNPC == null) {
			// If no focused item either, return
			if(focusedItem == null) {
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
		if(name.length() > 16) {
			InfoScreen.nameInfo.setFont(new Font(Font.SERIF, Font.BOLD, InfoScreen.nameFontSize * 16 / (name.length())));
		} else {
			InfoScreen.nameInfo.setFont(InfoScreen.usualNameFont);
		}
		
		// Set Health text
		InfoScreen.nameInfo.setText(name);
		
		// Repaint the panel
		InfoScreen.namePanel.repaint();
	}
	
	public static void updateEntityImage() {
		// If we don't have a NPC to follow yet, then return
		if(focusedNPC == null) {
			if(focusedItem == null) {
				return;
			} else {
				// Paint item image
				
				// Get new scale factor
				double scaleFactor = upScaleFactor;
				
				// Get item image
				try {
					File file = new File(focusedItem.imagePath);
					URL url = file.toURI().toURL();
					InfoScreen.itemImage = new ImageIcon(url).getImage();
					
					InfoScreen.itemImage = ImageHandler.scaleImage(InfoScreen.itemImage, 80, 80, GameInitializer.scaleFactor, GameInitializer.scaleFactor);
				} catch (Exception e) {
					// Let us know if we can't find the item image
					System.out.println(focusedItem.imagePath + " not found.");
					e.printStackTrace();
				}
				// Set item as new entity image
				InfoScreen.itemImage = ImageHandler.scaleImage(InfoScreen.itemImage, 180, 180, scaleFactor, scaleFactor);
				InfoScreen.entityImagePanel.setEImage(InfoScreen.itemImage);
				
				// Set blank tile image
				InfoScreen.entityImagePanel.setTImage(null);
				
			}
		} else {
			// Get position of NPC
			int xPos = InfoScreen.focusedNPC.getXPos();
			int yPos = InfoScreen.focusedNPC.getYPos();
			
			// Get new scale factor
			double scaleFactor = upScaleFactor;
			
			// Sets tile image
			InfoScreen.tileImage = GameScreen.getTile(xPos, yPos).bgImage;
			InfoScreen.tileImage = ImageHandler.scaleImage(InfoScreen.tileImage, 180, 180, scaleFactor, scaleFactor);
			InfoScreen.entityImagePanel.setTImage(InfoScreen.tileImage);
			
			// Gets corpse image or entity image of NPC tile depending on NPC's health
			if(focusedNPC.getCurrentHP() <= 0) {
				InfoScreen.npcImage = GameScreen.getTile(xPos, yPos).corpseImage;
				InfoScreen.npcImage = ImageHandler.scaleImage(InfoScreen.npcImage, 180, 180, scaleFactor, scaleFactor);
				InfoScreen.entityImagePanel.setEImage(InfoScreen.npcImage);
			} else {
				// Gets NPC image
				InfoScreen.npcImage = GameScreen.getTile(xPos, yPos).entityImage;
				InfoScreen.npcImage = ImageHandler.scaleImage(InfoScreen.npcImage, 180, 180, scaleFactor, scaleFactor);
				InfoScreen.entityImagePanel.setEImage(InfoScreen.npcImage);
			}
		}

		// Repaint the panel
		InfoScreen.entityImagePanel.repaint();
	}
	
	// Defocus everything
	public static void defocusAll() {
		// Set focused entities to null
		InfoScreen.focusedNPC = null;
		InfoScreen.focusedItem = null;
		
		int xIndex = InventoryScreen.getXIndex();
		int yIndex = InventoryScreen.getYIndex();
		GItem newSelected = InventoryScreen.getTile(xIndex, yIndex).getItem();
		if(newSelected != null) {
			InfoScreen.setItemFocus(newSelected);
			return;
		}
		
		// Reinitialize visible components
		InfoScreen.healthPanel.setVisible(true);
		InfoScreen.armorPanel.setVisible(true);
		
		// Reset text values
		InfoScreen.healthInfo.setText("HP: - / -");
		InfoScreen.armorInfo.setText("DEF: -");
		InfoScreen.nameInfo.setText("-");
		InfoScreen.descBox.setText("");
		InfoScreen.shrinkDescBox();
		
		// Reset color values
		InfoScreen.healthPanel.setBackground(Color.WHITE);
		
		// Reset Entity Image panel
		InfoScreen.entityImagePanel.setTImage(null);
		InfoScreen.entityImagePanel.setEImage(null);
		
		// Repaint all affected panels
		InfoScreen.healthPanel.repaint();
		InfoScreen.namePanel.repaint();
		InfoScreen.entityImagePanel.repaint();
	}
	
	// Defocuses if we are focused on an item
	public static void defocusIfItem() {
		if(InfoScreen.focusedItem != null) {
			InfoScreen.defocusAll();
		}
	}
	
	// Defocuses if we are focused on an NPC
	public static void defocusIfNPC() {
		if(InfoScreen.focusedNPC != null) {
			InfoScreen.defocusAll();
		}
	}
	
	// Update the whole screen
	public static void updateInfoScreen() {
		InfoScreen.updateHealthValues();
		InfoScreen.updateArmorValues();
		InfoScreen.updateNameValues();
		InfoScreen.updateDescValues();
		InfoScreen.updateEntityImage();
	}
	
	private static void shrinkDescBox() {
		InfoScreen.descBox.setBounds(infoWidth *4/54, infoHeight *23/27, descWidth, (descHeight / 3));
	}
	
	private static void growDescBox() {
		InfoScreen.descBox.setBounds(infoWidth *4/54, infoHeight *16/27, descWidth, descHeight);
	}
	
	// *******************
	// Getters and Setters
	
	// Sets InfoScreen to relay information about an NPC
	public static void setNPCFocus(GCharacter npc) {
		// If our NPC is null, defocus everything
		if(npc == null) {
			InfoScreen.defocusAll();
		}
		
		// Set focus on NPC and not item
		InfoScreen.focusedNPC = npc;
		InfoScreen.focusedItem = null;
		
		// Make health panel visible and hide description box
		InfoScreen.shrinkDescBox();
		InfoScreen.healthPanel.setVisible(true);
		InfoScreen.armorPanel.setVisible(true);
		
		// Update everything
		InfoScreen.updateInfoScreen();
	}
	
	// Sets InfoScreen to relay information about an item
	public static void setItemFocus(GItem item) {
		// If our item is null, defocus everything
		if(item == null) {
			InfoScreen.defocusAll();
		}
		
		// Set focus on item and not NPC
		InfoScreen.focusedItem = item;
		InfoScreen.focusedNPC = null;
		
		// Make description box visible and hide health panel
		InfoScreen.growDescBox();
		InfoScreen.healthPanel.setVisible(false);
		InfoScreen.armorPanel.setVisible(false);
		
		// Update everything
		InfoScreen.updateInfoScreen();
	}
	
	public static int getInfoWidth() {
		return InfoScreen.infoWidth;
	}
	
	public static int getInfoHeight() {
		return InfoScreen.infoHeight;
	}

}
