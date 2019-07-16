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

public class GameTile extends JPanel {

	private static final long serialVersionUID = 1L;
	
	// Image for darkness
	private static Image darkImg = null;
	
	// Image for faded darkness (slightly less dark)
	private static Image fadeImg = null;
	
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
		
		String bgpath = type.selectImage();
		String mgpath = GPath.NULL;
		String fgpath = GPath.NULL;
		
		// Set background color of black
		this.setBackground(Color.BLACK);
		
		// Mouse Listener for finding NPCs/items
		this.addMouseListener(new MouseAdapter() {
		     @Override
		     public void mouseEntered(MouseEvent mouseEvent) {
		    	 // Only try to focus if tile is currently visible
		    	 if(GameTile.this.isVisible) {
		    		// Try to focus on NPC
			    	 if(GameTile.this.findNPC()) {
			    		 return;
			    	 } else {
			    		 // If we couldn't find an NPC, try to find item
			    		 GameTile.this.findItem();
			    	 }
		    	 }
		     }
		});
		
		// Set background image, corpse image, effect image, and foreground image
		this.setBG(bgpath);
	    this.setCorpseImage(mgpath);
	    this.setFG(fgpath);

		this.setVisible(true);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        EntityManager em = EntityManager.getInstance();
        Player player = em.getPlayer();
        byte distance = 0;
        
        if(em.isDark()) {
    		// Get player's location
    		int plrX = player.getXPos();
    		int plrY = player.getYPos();
    		
    		// Get relative location to player
    		byte distX = (byte) (plrX - this.gridX);
    		byte distY = (byte) (plrY - this.gridY);
    		
    		distance = (byte) (Math.abs(distX) + Math.abs(distY));
        }
        
        if(distance <= (player.getVision() + 1)) {
        	// Visible square
	        g.drawImage(this.bgImage, 0, 0, null);
	        if(this.setCorpse)
	        	g.drawImage(this.corpseImage, 0, 0, null);
	        if(this.setPickup)
	        	g.drawImage(this.pickupImage, 0, 0, null);
	        if(this.setEntity)
	        	g.drawImage(this.entityImage, 0, 0, null);
	        for(Image projImg: this.projImages.values()) {
	        	g.drawImage(projImg, 0, 0, null); 
	        }
	        for(Image fxImg: this.fxImages.values()) {
	        	g.drawImage(fxImg, 0, 0, null); 
	        }
	        
        	// Paint partial darkness square if at the edge of our vision
        	if(distance == (player.getVision() + 1)) {
        		g.drawImage(this.getDarkImage(), 0, 0, null);
        	} else if(distance == player.getVision()) {
        		g.drawImage(this.getFadingDarkImage(), 0, 0, null);
        	}
	        
	        this.isVisible = true;
        } else {
        	// Display nothing (AKA: Darkness)
        	this.isVisible = false;
        }
    }
	
	// Sets the tile (ground) image
	public void setBG(String filepath) {
		this.bgImage = this.loadImage(filepath);
		this.bgImage = ImageHandler.scaleImage(this.bgImage, 80, 80, this.scaleFactor, this.scaleFactor);
		this.repaint();
	}
	
	// Sets the corpse image
	public void setCorpseImage(String filepath) {
		// Sets flag to not render item if null path
		if(filepath != GPath.NULL) {
			this.setCorpse = true;
		} else {
			this.setCorpse = false;
		}
		
		this.corpseImage = this.loadImage(filepath);
		this.corpseImage = ImageHandler.scaleImage(this.corpseImage, 80, 80, this.scaleFactor, this.scaleFactor);
		this.repaint();
	}
	
	// Sets the pickup image
	public void setPickupImage(String filepath) {
		// Sets flag to not render item if null path
		if(filepath != GPath.NULL) {
			this.setPickup = true;
		} else {
			this.setPickup = false;
		}
		
		this.pickupImage = this.loadImage(filepath);
		this.pickupImage = ImageHandler.scaleImage(this.pickupImage, 80, 80, this.scaleFactor, this.scaleFactor);
		this.repaint();
	}
	
	// Sets the effect image
	public void setEffectImage(String filepath, GEffect fx) {
		// Do not render if null path
		if(filepath == GPath.NULL) {
			return;
		}
		
		Image newEffect = this.loadImage(filepath);
		newEffect = ImageHandler.scaleImage(newEffect, 80, 80, this.scaleFactor, this.scaleFactor);
		this.fxImages.put(fx, newEffect);
		this.repaint();
	}
	
	// Sets the projectile image
	public void setProjectileImage(String filepath, GProjectile proj) {
		// Do not render if null path
		if(filepath == GPath.NULL) {
			return;
		}
		
		Image newProj = this.loadImage(filepath);
		newProj = ImageHandler.scaleImage(newProj, 80, 80, this.scaleFactor, this.scaleFactor);
		this.projImages.put(proj, newProj);
		this.repaint();
	}
	
	// Sets the character image
	public void setFG(String filepath) {
		// Sets flag to not render item if null path
		if(filepath != GPath.NULL) {
			this.setEntity = true;
		} else {
			this.setEntity = false;
		}
		
		this.entityImage = this.loadImage(filepath);
		this.entityImage = ImageHandler.scaleImage(this.entityImage, 80, 80, this.scaleFactor, this.scaleFactor);
		this.repaint();
	}
	
	// Gets the dark image used for partial vision
	private Image getDarkImage() {
		if(GameTile.darkImg == null) {
			GameTile.darkImg = this.loadImage(GPath.createImagePath(GPath.TILE, GPath.GENERIC, "area_dark.png"));
			GameTile.darkImg = ImageHandler.scaleImage(GameTile.darkImg, 80, 80, this.scaleFactor, this.scaleFactor);
		}
		return GameTile.darkImg;
	}
	
	// Gets the fading image (slightly less dark) used for partial vision
	private Image getFadingDarkImage() {
		if(GameTile.fadeImg == null) {
			GameTile.fadeImg = this.loadImage(GPath.createImagePath(GPath.TILE, GPath.GENERIC, "area_fade.png"));
			GameTile.fadeImg = ImageHandler.scaleImage(GameTile.fadeImg, 80, 80, this.scaleFactor, this.scaleFactor);
		}
		return GameTile.fadeImg;
	}
	
	// Focuses on an NPC if it exists, returning true if we do
	private boolean findNPC() {
		for(GCharacter npc: EntityManager.getInstance().getNPCManager().getCharacters()) {
			if(this.gridX == npc.getXPos() && this.gridY == npc.getYPos() && npc.getFocusable()) {
				InfoScreen.setNPCFocus(npc);
				return true;
			}
		}
		return false;
	}
	
	// Focuses on an NPC if it exists, returning true if we do
	private boolean findItem() {
		for(GPickup pu: EntityManager.getInstance().getPickupManager().getPickups()) {
			if(this.gridX == pu.getXPos() && this.gridY == pu.getYPos()) {
				InfoScreen.setItemFocus(pu.item);
				return true;
			}
		}
		return false;
	}
	
	// Loads in an image from a file path
	private Image loadImage(String filepath) {
		// Initialize output
		Image output = null;
		
		// First, try to lookup the image in the ImageBank
		Image storedImg = ImageBank.lookup(filepath);
		if(storedImg != null) {
			return storedImg;
		}
		
		// Load image
		try {
			File file = new File(filepath);
			URL url = file.toURI().toURL();
			output = new ImageIcon(url).getImage();
		} catch (Exception e) {
			System.out.println(filepath + " not found.");
			e.printStackTrace();
		}
		
		// Store image in the bank
		ImageBank.storeImage(filepath, output);
		
		// Return output image
		return output;
	}
	
	public void clearEffect(GEffect fx) {
		if(this.fxImages.containsKey(fx)) {
			this.fxImages.remove(fx);
		}
	}
	
	public void clearEffects() {
		this.fxImages.clear();
		this.fxImages = null;
		this.fxImages = new HashMap<GEffect, Image>();
		this.repaint();
	}
	
	public void clearCorpse() {
		this.setCorpseImage(GPath.NULL);
	}
	
	public void clearFG() {
		this.setFG(GPath.NULL);
	}
	
	public void clearProj(GProjectile proj) {
		if(this.projImages.containsKey(proj)) {
			this.projImages.remove(proj);
			this.repaint();
		}
	}
	
	public void clearProjs() {
		this.projImages.clear();
		this.projImages = null;
		this.projImages = new HashMap<GProjectile, Image>();
		this.repaint();
	}
	
	public void clearPickup() {
		this.setPickupImage(GPath.NULL);
	}
	
	public void clearAll() {
		this.clearEffects();
		this.clearCorpse();
		this.clearFG();
		this.clearProjs();
		this.clearPickup();
	}

	// Sets a GameTile to a new TileType and then updates its look
	public void setTileType(TileType type) {
		this.tType = type;
		
		// Get images for the TileType
		String bgpath = type.selectImage();
		
		// Reset background image and foreground image
		this.setBG(bgpath);  
	    
	    // Repaint the tile to update the look
	    this.repaint();
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
