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
		setLayout(new GridLayout(2, 1));
		
		// Set background color
		setBackground(new Color(179, 179, 179));
		
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
		topPanel.add(weaponImagePanel);
		weaponImagePanel.setPreferredSize(new Dimension(weaponWidth, weaponHeight));
		
		//-----------------------
		// Upper right panel
		JPanel topRightPanel = new JPanel();
		topRightPanel.setLayout(new GridLayout(2, 1));
		topRightPanel.setOpaque(false);
		
		// Add in Name label to Upper right panel
		nameLabel.setFont(usualFont);
		topRightPanel.add(nameLabel);
		
		// Add in Damage label to Upper right panel
		dmgLabel.setFont(usualFont);
		topRightPanel.add(dmgLabel);
		
		// Add Upper right panel to Upper half panel
		topPanel.add(topRightPanel);
		
		//-----------------------
		// Bottom half panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(2, 1));
		bottomPanel.setOpaque(false);
		
		// Add in Critical label
		critLabel.setFont(usualFont);
		bottomPanel.add(critLabel);
		critLabel.setBorder(BorderFactory.createEmptyBorder(0, labelXBuffer, 0, 0));
		
		// Add in Critical label
		chargeLabel.setFont(usualFont);
		bottomPanel.add(chargeLabel);
		chargeLabel.setBorder(BorderFactory.createEmptyBorder(0, labelXBuffer, 0, 0));
		
		// Mouse Listener for focusing on equipped item
		addMouseListener(new MouseAdapter() {
		     @Override
		     public void mouseEntered(MouseEvent mouseEvent) {
		    	 // Try to focus on Equipped weapon if it exists
		    	 Weapon activeWeapon = EntityManager.getInstance().getPlayer().getWeapon();
		    	 if (activeWeapon == null) {
		    		 return;
		    	 } else {
		    		 InfoScreen.setItemFocus(activeWeapon);
		    	 }
		     }
		});
		
		// Add components to main panel
		add(topPanel);
		add(bottomPanel);
		
		// Initialize panel with correct information about weapon
		setBorderPath(GPath.BORDER);
		updateWeaponPanel();
	}
	
	public void updateWeaponPanel() {
		// Fetches player's current weapon for reference
		Weapon newWep = EntityManager.getInstance().getPlayer().getWeapon();
		
		// Updates the weapon image
		setWeaponPath(newWep.getImagePath());
		
		// Set border image
		if (newWep.isCharged) {
			setBorderPath(GPath.IMAGE + "template_charge.png");
		} else {
			setBorderPath(GPath.BORDER);
		}
		
		// Sets the new name value
		this.nameLabel.setText(newWep.getName());
		
		// If name is too long, shrink the font size
		if (newWep.getName().length() > 11) {
			nameLabel.setFont(new Font(Font.SERIF, Font.BOLD, this.fontSize * 11 / (newWep.getName().length())));
		} else {
			nameLabel.setFont(this.usualFont);
		}
		
		// Sets new damage values
		dmgLabel.setText("DMG: "+Integer.toString(newWep.minDmg)+" - "
				+Integer.toString(newWep.maxDmg));
		
		// Sets new crit values
		critLabel.setText("CRITS: "+toPercent(newWep.critChance)+" / "
				+Double.toString(newWep.critMult)+"x");
		
		// Sets new charge values
		chargeLabel.setText("CHARGED: "+Double.toString(newWep.chargeMult)+"x");
	}
	
	public static String toPercent(double num) {
		// Convert double to int percent
		int percent = (int) (num * 100);
		
		// Convert to string
		String output = (Integer.toString(percent) + "%");
		
		// Return output
		return output;
	}
	
	// Sets the item image
	public void setWeaponPath(String filepath) {
		try {
			File file = new File(filepath);
			URL url = file.toURI().toURL();
			this.weaponImage = new ImageIcon(url).getImage();
		} catch (Exception ex) {
			System.out.println("'" + filepath + "' not found.");
			ex.printStackTrace();
		}
		this.weaponImage = ImageHandler.scaleImage(weaponImage, weaponWidth, weaponHeight, GameInitializer.scaleFactor, GameInitializer.scaleFactor);
		weaponImagePanel.setForegroundImage(weaponImage);
		repaint();
	}
	
	// Sets the border image
	public void setBorderPath(String filepath) {
		try {
			File file = new File(filepath);
			URL url = file.toURI().toURL();
			this.borderImage = new ImageIcon(url).getImage();
		} catch (Exception ex) {
			System.out.println("'" + filepath + "' not found.");
			ex.printStackTrace();
		}
		this.borderImage = ImageHandler.scaleImage(borderImage, weaponWidth, weaponHeight, GameInitializer.scaleFactor, GameInitializer.scaleFactor);
		weaponImagePanel.setBackgroundImage(borderImage);
		repaint();
	}

}
