package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import helpers.GPath;
import helpers.ImageHandler;
import managers.EntityManager;
import weapons.Weapon;

public class WeaponStatPanel extends JPanel {
	
	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Weapon's image and image panel
	private EntityImagePanel weaponImagePanel = new EntityImagePanel();
	private Image weaponImage;
	private Image borderImage;
	private int weaponWidth = 80;
	private int weaponHeight = 80;

	// Usual font to use for the panel and default font size
	private Font usualFont;
	private int fontSize = 20;
	
	// Bottom label buffer
	private int labelXBuffer = 7;
	
	// Label for name
	private JLabel nameLabel = new JLabel("Weapon Name");
	
	// Label for damage
	private JLabel dmgLabel = new JLabel("DMG: # - #");
	
	// Label for critical hits
	private JLabel critLabel = new JLabel("CRITS: #% / #.#x");
	
	// Label for charge hits
	private JLabel chargeLabel = new JLabel("CHARGE: #.#x");
	
	// Constructor
	protected WeaponStatPanel() {
		super();
		
		// Set grid layout
		this.setLayout(new GridLayout(2, 1));
		
		// Set background color
		this.setBackground(new Color(179, 179, 179));
		
		// Scale down dimensions and fonts
		double sf = GameInitializer.scaleFactor;
		this.weaponWidth = (int) (this.weaponWidth * sf);
		this.weaponHeight = (int) (this.weaponHeight * sf);
		this.labelXBuffer = (int) (this.labelXBuffer * sf);
		this.fontSize = (int) (this.fontSize * sf);
		
		// Set up usual font
		this.usualFont = new Font(Font.SERIF, Font.BOLD, this.fontSize);
		
		//-----------------------
		// Upper half panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setOpaque(false);
		
		// Add in Weapon image panel to Upper half panel
		topPanel.add(this.weaponImagePanel);
		weaponImagePanel.setPreferredSize(new Dimension(weaponWidth, weaponHeight));
		
		//-----------------------
		// Upper right panel
		JPanel topRightPanel = new JPanel();
		topRightPanel.setLayout(new GridLayout(2, 1));
		topRightPanel.setOpaque(false);
		
		// Add in Name label to Upper right panel
		this.nameLabel.setFont(this.usualFont);
		topRightPanel.add(this.nameLabel);
		
		// Add in Damage label to Upper right panel
		this.dmgLabel.setFont(this.usualFont);
		topRightPanel.add(this.dmgLabel);
		
		// Add Upper right panel to Upper half panel
		topPanel.add(topRightPanel);
		
		//-----------------------
		// Bottom half panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(2, 1));
		bottomPanel.setOpaque(false);
		
		// Add in Critical label
		this.critLabel.setFont(this.usualFont);
		bottomPanel.add(this.critLabel);
		this.critLabel.setBorder(BorderFactory.createEmptyBorder(0, labelXBuffer, 0, 0));
		
		// Add in Critical label
		this.chargeLabel.setFont(this.usualFont);
		bottomPanel.add(this.chargeLabel);
		this.chargeLabel.setBorder(BorderFactory.createEmptyBorder(0, labelXBuffer, 0, 0));
		
		// Mouse Listener for focusing on equipped item
		this.addMouseListener(new MouseAdapter() {
		     @Override
		     public void mouseEntered(MouseEvent mouseEvent) {
		    	 // Try to focus on Equipped weapon if it exists
		    	 if(EntityManager.getInstance().getPlayer().getWeapon() == null) {
		    		 return;
		    	 } else {
		    		 InfoScreen.setItemFocus(EntityManager.getInstance().getPlayer().getWeapon());
		    	 }
		     }
		});
		
		// Add components to main panel
		this.add(topPanel);
		this.add(bottomPanel);
		
		// Initialize panel with correct information about weapon
		this.setBorderPath(GPath.BORDER);
		this.updateWeaponPanel();
	}
	
	public void updateWeaponPanel() {
		// Fetches player's current weapon for reference
		Weapon newWep = EntityManager.getInstance().getPlayer().getWeapon();
		
		// Updates the weapon image
		this.setWeaponPath(newWep.imagePath);
		
		// Set border image
		if(newWep.isCharged) {
			this.setBorderPath(GPath.IMAGE + "template_charge.png");
		} else {
			this.setBorderPath(GPath.BORDER);
		}
		
		// Sets the new name value
		this.nameLabel.setText(newWep.name);
		
		// If name is too long, shrink the font size
		if(newWep.name.length() > 11) {
			this.nameLabel.setFont(new Font(Font.SERIF, Font.BOLD, this.fontSize * 11 / (newWep.name.length())));
		} else {
			this.nameLabel.setFont(this.usualFont);
		}
		
		// Sets new damage values
		this.dmgLabel.setText("DMG: "+Integer.toString(newWep.minDmg)+" - "
				+Integer.toString(newWep.maxDmg));
		
		// Sets new crit values
		this.critLabel.setText("CRITS: "+toPercent(newWep.critChance)+" / "
				+Double.toString(newWep.critMult)+"x");
		
		// Sets new charge values
		this.chargeLabel.setText("CHARGED: "+Double.toString(newWep.chargeMult)+"x");
	}
	
	public String toPercent(double num) {
		// Convert double to int percent
		int percent = (int) (num * 100);
		
		// Convert to string
		String output = Integer.toString(percent) + "%";
		
		// Return output
		return output;
	}
	
	// Sets the item image
	public void setWeaponPath(String filepath) {
		try {
			File file = new File(filepath);
			URL url = file.toURI().toURL();
			this.weaponImage = new ImageIcon(url).getImage();
		} catch (Exception e) {
			System.out.println(filepath + " not found.");
			e.printStackTrace();
		}
		this.weaponImage = ImageHandler.scaleImage(this.weaponImage, weaponWidth, weaponHeight, GameInitializer.scaleFactor, GameInitializer.scaleFactor);
		this.weaponImagePanel.setForegroundImage(this.weaponImage);
		this.repaint();
	}
	
	// Sets the item image
	public void setBorderPath(String filepath) {
		try {
			File file = new File(filepath);
			URL url = file.toURI().toURL();
			this.borderImage = new ImageIcon(url).getImage();
		} catch (Exception e) {
			System.out.println(filepath + " not found.");
			e.printStackTrace();
		}
		this.borderImage = ImageHandler.scaleImage(this.borderImage, weaponWidth, weaponHeight, GameInitializer.scaleFactor, GameInitializer.scaleFactor);
		this.weaponImagePanel.setBackgroundImage(this.borderImage);
		this.repaint();
	}

}
