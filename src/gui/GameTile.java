package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import characters.GCharacter;
import characters.allies.Player;
import effects.GEffect;
import helpers.GPath;
import helpers.ImageHandler;
import items.GPickup;
import managers.EntityManager;
import managers.ImageBank;
import projectiles.GProjectile;
import tiles.TileType;

/**
 * Panel class that represents a single tile position for the game
 * @author jeoliva
 */
public class GameTile extends JPanel {

	private static final long serialVersionUID = 1L;
	
	// Image for darkness
	private static Image darkImg = null;
	
	// Image for faded darkness (slightly less dark)
	private static Image fadeImg = null;
	
	// Image for eye corruption effect
	private static Image eyeImg = null;
	
	private static Boolean useEye = null;
	
	// Chance for an eye image to show up replacing a character image
	private static final double EYE_CHANCE = 0.001;
	
	// X and Y position of GameTile on the GameScreen grid
	private int gridX, gridY;
	
	// Image for background of tile
	protected Image bgImage;
	
	// Image for entity on tile
	protected Image entityImage;
	
	// Image for corpse on tile
	protected Image corpseImage;
	
	// Mapping of current projectile images
	protected HashMap<GProjectile, Image> projImages = new HashMap<GProjectile, Image>();
	
	// Mapping of current effect images
	protected HashMap<GEffect, Image> fxImages = new HashMap<GEffect, Image>();
	
	// Image for the pickup on the tile
	protected Image pickupImage;
	
	// Type of tile
	private TileType tType;
	
	// Flag indicating whether tile is currently visible or not
	private boolean isVisible = false;
	
	// Scale factor of game images
	private double scaleFactor;
	
	private int tileLength;
	
	// Flags to determine whether objects should be rendered or not
	private boolean setCorpse = false;
	private boolean setEntity = false;
	private boolean setPickup = false;
	
	// Constructor with image names
	protected GameTile(int gridX, int gridY, TileType type, double scaleFactor) {
		super(new BorderLayout());
		
		this.gridX = gridX;
		this.gridY = gridY;
		
		this.tType = type;
		this.scaleFactor = scaleFactor;
		this.tileLength = (int) (GameInitializer.tileArtSize * scaleFactor);
		
		String bgpath = type.selectImage();
		String mgpath = GPath.NULL;
		String fgpath = GPath.NULL;
		
		// Set background color of black
		setBackground(Color.BLACK);
		
		// Mouse Listener for finding NPCs/items
		addMouseListener(new MouseAdapter() {
		     @Override
		     public void mouseEntered(MouseEvent mouseEvent) {
		    	 // Only try to focus if tile is currently visible
		    	 if (isVisible) {
		    		 // Try to focus on NPC
			    	 if (findNPC()) {
			    		 return;
			    	 } else {
			    		 // If we couldn't find an NPC, try to find item
			    		 findItem();
			    	 }
		    	 }
		     }
		});
		
		// Set background image, corpse image, effect image, and foreground image
		setBG(bgpath);
	    setCorpseImage(mgpath);
	    setFG(fgpath);
	    //--
		setVisible(true);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        EntityManager em = EntityManager.getInstance();
        Player player = em.getPlayer();
        byte distance = 0;
        
        if (em.isDark()) {
    		// Get player's location
    		int plrX = player.getXPos();
    		int plrY = player.getYPos();
    		
    		// Get relative location to player
    		byte distX = (byte) (plrX - gridX);
    		byte distY = (byte) (plrY - gridY);
    		
    		distance = (byte) (Math.abs(distX) + Math.abs(distY));
        }
        
        if (distance <= (player.getVision() + 1)) {
        	// Visible square
	        g.drawImage(this.bgImage, 0, 0, null);
	        if (setCorpse) {
	        	g.drawImage(corpseImage, 0, 0, null);
	        }
	        if (setPickup) {
	        	g.drawImage(pickupImage, 0, 0, null);
	        }
	        if (setEntity) {
	        	g.drawImage(entityImage, 0, 0, null);
	        }
	        for (Image projImg: projImages.values()) {
	        	g.drawImage(projImg, 0, 0, null); 
	        }
	        for (Image fxImg: fxImages.values()) {
	        	g.drawImage(fxImg, 0, 0, null); 
	        }
	        
        	// Paint partial darkness square if at the edge of our vision
        	if (distance == (player.getVision() + 1)) {
        		g.drawImage(getDarkImage(), 0, 0, null);
        	} else if (distance == player.getVision()) {
        		g.drawImage(getFadingDarkImage(), 0, 0, null);
        	}
	        
	        this.isVisible = true;
        } else {
        	// Display nothing (AKA: Darkness)
        	this.isVisible = false;
        }
    }
	
	// Sets the tile (ground) image
	public void setBG(String filepath) {
		this.bgImage = loadImage(filepath, true);
		this.bgImage = ImageHandler.scaleImage(bgImage, tileLength, tileLength, scaleFactor, scaleFactor);
		repaint();
	}
	
	// Sets the corpse image
	public void setCorpseImage(String filepath) {
		// Sets flag to not render item if null path
		if (filepath != GPath.NULL) {
			this.setCorpse = true;
		} else {
			this.setCorpse = false;
		}
		
		this.corpseImage = loadImage(filepath, true);
		this.corpseImage = ImageHandler.scaleImage(corpseImage, tileLength, tileLength, scaleFactor, scaleFactor);
		repaint();
	}
	
	// Sets the pickup image
	public void setPickupImage(String filepath) {
		// Sets flag to not render item if null path
		if (filepath != GPath.NULL) {
			this.setPickup = true;
		} else {
			this.setPickup = false;
		}
		
		this.pickupImage = loadImage(filepath, true);
		this.pickupImage = ImageHandler.scaleImage(pickupImage, tileLength, tileLength, scaleFactor, scaleFactor);
		repaint();
	}
	
	// Sets the effect image
	public void setEffectImage(String filepath, GEffect fx) {
		// Do not render if null path
		if (filepath == GPath.NULL) {
			return;
		}
		
		Image newEffect = loadImage(filepath, true);
		newEffect = ImageHandler.scaleImage(newEffect, tileLength, tileLength, scaleFactor, scaleFactor);
		fxImages.put(fx, newEffect);
		repaint();
	}
	
	// Sets the projectile image
	public void setProjectileImage(String filepath, GProjectile proj) {
		// Do not render if null path
		if (filepath == GPath.NULL) {
			return;
		}
		
		Image newProj = loadImage(filepath, true);
		newProj = ImageHandler.scaleImage(newProj, tileLength, tileLength, scaleFactor, scaleFactor);
		projImages.put(proj, newProj);
		repaint();
	}
	
	// Sets the character image
	public void setFG(String filepath) {
		// Sets flag to not render item if null path
		if (filepath != GPath.NULL) {
			this.setEntity = true;
		} else {
			this.setEntity = false;
		}
		
		this.entityImage = loadImage(filepath);
		this.entityImage = ImageHandler.scaleImage(entityImage, tileLength, tileLength, scaleFactor, scaleFactor);
		repaint();
	}
	
	// Gets the dark image used for partial vision
	private Image getDarkImage() {
		if (darkImg == null) {
			darkImg = loadImage(GPath.createImagePath(GPath.TILE, GPath.GENERIC, "area_dark.png"), true);
			darkImg = ImageHandler.scaleImage(darkImg, tileLength, tileLength, scaleFactor, scaleFactor);
		}
		return darkImg;
	}
	
	// Gets the fading image (slightly less dark) used for partial vision
	private Image getFadingDarkImage() {
		if (fadeImg == null) {
			fadeImg = loadImage(GPath.createImagePath(GPath.TILE, GPath.GENERIC, "area_fade.png"), true);
			fadeImg = ImageHandler.scaleImage(fadeImg, tileLength, tileLength, scaleFactor, scaleFactor);
		}
		return fadeImg;
	}
	
	// Gets the eye image (used for corruption effects on characters)
	private Image getEyeImage() {
		if (eyeImg == null) {
			eyeImg = loadImage(GPath.createImagePath(GPath.ENEMY, GPath.GAZER, "Gazer_IDLE.png"), true);
		}
		return eyeImg;
	}
	
	// Focuses on an NPC if it exists, returning true if we do
	private boolean findNPC() {
		for (GCharacter npc: EntityManager.getInstance().getNPCManager().getCharacters()) {
			if (gridX == npc.getXPos() && gridY == npc.getYPos() && npc.getFocusable()) {
				InfoScreen.setNPCFocus(npc);
				return true;
			}
		}
		return false;
	}
	
	// Focuses on an NPC if it exists, returning true if we do
	private boolean findItem() {
		for (GPickup pu: EntityManager.getInstance().getPickupManager().getPickups()) {
			if (gridX == pu.getXPos() && gridY == pu.getYPos()) {
				InfoScreen.setItemFocus(pu.getItem());
				return true;
			}
		}
		return false;
	}
	
	// Loads in an image from a file path
	private Image loadImage(String filepath, boolean skipEye) {
		// One time load of whether game is "corrupted"
		if (useEye == null) {
			File madFile = new File(GPath.EYE_PATH);
			useEye = madFile.exists();
		}
		
		// If game is "corrupted", give a small chance to replace the image with an eye
		if (useEye && !skipEye && (Math.random() < EYE_CHANCE)) {
			return getEyeImage();
		}
		
		// Initialize output
		Image output = null;
		
		// First, try to lookup the image in the ImageBank
		Image storedImg = ImageBank.lookup(filepath);
		if (storedImg != null) {
			return storedImg;
		}
		
		// Load image
		try {
			File file = new File(filepath);
			URL url = file.toURI().toURL();
			output = new ImageIcon(url).getImage();
		} catch (Exception ex) {
			System.out.println("'" + filepath + "' not found.");
			ex.printStackTrace();
		}
		
		// Store image in the bank
		ImageBank.storeImage(filepath, output);
		
		// Return output image
		return output;
	}
	
	// Loads in an image from a file path
	private Image loadImage(String filepath) {
		return loadImage(filepath, false);
	}
	
	public void clearEffect(GEffect fx) {
		if (fxImages.containsKey(fx)) {
			fxImages.remove(fx);
		}
	}
	
	public void clearEffects() {
		this.fxImages.clear();
		this.fxImages = null;
		this.fxImages = new HashMap<GEffect, Image>();
		repaint();
	}
	
	public void clearCorpse() {
		setCorpseImage(GPath.NULL);
	}
	
	public void clearFG() {
		setFG(GPath.NULL);
	}
	
	public void clearProj(GProjectile proj) {
		if (projImages.containsKey(proj)) {
			projImages.remove(proj);
			repaint();
		}
	}
	
	public void clearProjs() {
		this.projImages.clear();
		this.projImages = null;
		this.projImages = new HashMap<GProjectile, Image>();
		repaint();
	}
	
	public void clearPickup() {
		setPickupImage(GPath.NULL);
	}
	
	public void clearAll() {
		clearEffects();
		clearCorpse();
		clearFG();
		clearProjs();
		clearPickup();
	}

	// Sets a GameTile to a new TileType and then updates its look
	public void setTileType(TileType type) {
		this.tType = type;
		
		// Get images for the TileType
		String bgpath = type.selectImage();
		
		// Reset background image and foreground image
		setBG(bgpath);  
	    
	    // Repaint the tile to update the look
	    repaint();
	}
	
	// Return the type of the tile (Eg. Ground, Wall, Water, etc)
	public TileType getTileType() {
		return this.tType;
	}
	
	// Returns X position of tile
	public int getGridX() {
		return this.gridX;
	}
	
	// Returns Y position of tile
	public int getGridY() {
		return this.gridY;
	}
	
}
