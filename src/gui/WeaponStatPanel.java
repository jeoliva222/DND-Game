package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

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
	private Image weaponImage;
	private Image borderImage;
	private int wepXBuffer = 10;
	private int wepYBuffer = 10;
	private int weaponWidth = 80;
	private int weaponHeight = 80;

	// Usual font to use for the panel and default font size
	private Font usualFont;
	private int fontSize = 22;
	
	// Label for name
	private JLabel nameLabel = new JLabel("Weapon Name");
	private int nameXBuffer = 7;
	private int nameYBuffer = 15;
	private int nameWidth = 130;
	private int nameHeight = 25;
	
	// Label for damage
	private JLabel dmgLabel = new JLabel("DMG: # - #");
	private int dmgWidth = 130;
	private int dmgHeight = 25;
	
	// Label for critical hits
	private JLabel critLabel = new JLabel("CRITS: #% / #.#x");
	private int critWidth = 200;
	private int critHeight = 25;
	private int critYBuffer = 10;
	
	// Label for charge hits
	private JLabel chargeLabel = new JLabel("CHARGE: #.#x");
	private int chargeWidth = 200;
	private int chargeHeight = 25;
	private int chargeYBuffer = 5;
	
	// Constructor
	protected WeaponStatPanel() {
		super();
		
		// Set null content layout
		this.setLayout(null);
		
		// Set background color
		this.setBackground(new Color(179, 179, 179));
		
		// Scale down dimensions and fonts
		double sf = GameInitializer.scaleFactor;
		this.weaponWidth = (int) (this.weaponWidth * sf);
		this.weaponHeight = (int) (this.weaponHeight * sf);
		this.wepXBuffer = (int) (this.wepXBuffer * sf);
		this.wepYBuffer = (int) (this.wepYBuffer * sf);
		this.nameXBuffer = (int) (this.nameXBuffer * sf);
		this.nameYBuffer = (int) (this.nameYBuffer * sf);
		this.nameWidth = (int) (this.nameWidth * sf);
		this.nameHeight = (int) (this.nameHeight * sf);
		this.dmgWidth = (int) (this.dmgWidth * sf);
		this.dmgHeight = (int) (this.dmgHeight * sf);
		this.critWidth = (int) (this.critWidth * sf);
		this.critHeight = (int) (this.critHeight * sf);
		this.critYBuffer = (int) (this.critYBuffer * sf);
		this.chargeWidth = (int) (this.chargeWidth * sf);
		this.chargeHeight = (int) (this.chargeHeight * sf);
		this.chargeYBuffer = (int) (this.chargeYBuffer * sf);
		this.fontSize = (int) (this.fontSize * sf);
		
		// Set up usual font
		this.usualFont = new Font(Font.SERIF, Font.BOLD, this.fontSize);
		
		// Add in Name label
		this.nameLabel.setFont(this.usualFont);
		this.add(this.nameLabel);
		this.nameLabel.setBounds((this.nameXBuffer + this.weaponWidth + this.wepXBuffer),
				this.nameYBuffer, this.nameWidth, this.nameHeight);
		
		// Add in Damage label
		this.dmgLabel.setFont(this.usualFont);
		this.add(this.dmgLabel);
		this.dmgLabel.setBounds((this.nameXBuffer + this.weaponWidth + this.wepXBuffer),
				((this.nameYBuffer * 2) + this.nameHeight), this.dmgWidth, this.dmgHeight);
		
		// Add in Critical label
		this.critLabel.setFont(this.usualFont);
		this.add(this.critLabel);
		this.critLabel.setBounds(this.wepXBuffer, (this.critYBuffer + this.weaponHeight + this.wepYBuffer),
				this.critWidth, this.critHeight);
		
		// Add in Critical label
		this.chargeLabel.setFont(this.usualFont);
		this.add(this.chargeLabel);
		this.chargeLabel.setBounds(this.wepXBuffer,
				(this.critYBuffer + this.chargeYBuffer + this.weaponHeight + this.wepYBuffer + this.critHeight),
				this.chargeWidth, this.chargeHeight);
		
		// Mouse Listener for focusing on equipped item
		this.addMouseListener(new MouseAdapter() {
		     @Override
		     public void mouseEntered(MouseEvent mouseEvent) {
		    	 // Try to focus on Equipped weapon if it exists
		    	 if(EntityManager.getPlayer().getWeapon() == null) {
		    		 return;
		    	 } else {
		    		 InfoScreen.setItemFocus(EntityManager.getPlayer().getWeapon());
		    	 }
		     }
		});
		
		// Initialize panel with correct information about weapon
		this.setBorderPath(GPath.BORDER);
		this.updateWeaponPanel();
	}
	
	public void updateWeaponPanel() {
		// Fetches player's current weapon for reference
		Weapon newWep = EntityManager.getPlayer().getWeapon();
		
		// Updates the weapon and border image
		this.setWeaponPath(newWep.imagePath);
		
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
			// TODO Auto-generated catch block
			System.out.println(filepath + " not found.");
			e.printStackTrace();
		}
		this.weaponImage = ImageHandler.scaleImage(this.weaponImage, 80, 80, GameInitializer.scaleFactor, GameInitializer.scaleFactor);
		this.repaint();
	}
	
	// Sets the item image
	public void setBorderPath(String filepath) {
		try {
			File file = new File(filepath);
			URL url = file.toURI().toURL();
			this.borderImage = new ImageIcon(url).getImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(filepath + " not found.");
			e.printStackTrace();
		}
		this.borderImage = ImageHandler.scaleImage(this.borderImage, 80, 80, GameInitializer.scaleFactor, GameInitializer.scaleFactor);
		this.repaint();
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(borderImage, this.wepXBuffer, this.wepYBuffer, this);
        g.drawImage(weaponImage, this.wepXBuffer, this.wepYBuffer, this);
    }

}
