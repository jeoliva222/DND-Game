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

// Smaller version of the WeaponStatPanel, but without weapon statistics
// Used to display the player's sheathed weapon
public class WeaponBanner extends JPanel {

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
	
	protected WeaponBanner() {
		super();
		
		// Set null content layout
		this.setLayout(null);
		
		// Set background color
		this.setBackground(new Color(150, 150, 150));
		
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
		this.fontSize = (int) (this.fontSize * sf);
		
		// Set up usual font
		this.usualFont = new Font(Font.SERIF, Font.BOLD, this.fontSize);
		
		// Add in Name label
		this.nameLabel.setFont(this.usualFont);
		this.add(this.nameLabel);
		this.nameLabel.setBounds((this.nameXBuffer + this.weaponWidth + this.wepXBuffer),
				this.nameYBuffer, this.nameWidth, this.nameHeight);
		
		// Mouse Listener for focusing on equipped item
		this.addMouseListener(new MouseAdapter() {
		     @Override
		     public void mouseEntered(MouseEvent mouseEvent) {
		    	 // Try to focus on Equipped weapon if it exists
		    	 if(EntityManager.getInstance().getPlayer().getSheathedWeapon() == null) {
		    		 return;
		    	 } else {
		    		 InfoScreen.setItemFocus(EntityManager.getInstance().getPlayer().getSheathedWeapon());
		    	 }
		     }
		});
		
		// Initialize panel with correct information about weapon
		this.setBorderPath(GPath.BORDER);
		this.updateWeaponBanner();
	}
	
	public void updateWeaponBanner() {
		// Fetches player's current sheathed weapon for reference
		Weapon newWep = EntityManager.getInstance().getPlayer().getSheathedWeapon();
		
		// Updates the weapon image
		this.setWeaponPath(newWep.imagePath);
		
		// Set border image
		if(newWep.isCharged) {
			this.setBorderPath(GPath.createImagePath(GPath.TILE, GPath.GENERIC, "area_charge.png"));
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
