package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import helpers.GColors;
import helpers.GPath;
import helpers.ImageHandler;
import items.GItem;
import weapons.Weapon;

// Class representing a tile in the Inventory Screen
public class InventoryTile extends JPanel {

	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Index of the tile on the InventoryScreen
	private int xIndex, yIndex;
	
	// Images to draw
	private Image itemImage;
	private Image borderImage;
	
	// X and Y buffers to set image in center
	private int xBuffer = 10;
	private int yBuffer = 10;
	
	// Coordinates that stack size text is placed
	private int stackTextX = 15;
	private int stackTextY = 40;
	
	// Tile width and height
	private int tileWidth = 100;
	private int tileHeight = 100;
	
	// Font size and Font for stack size number
	private int stackFontSize = 28;
	private Font stackFont;
	
	// New scale factor
	private double upScaleFactor = ((double) tileWidth) / GameInitializer.tileArtSize;
	
	// Inventory Item on the tile
	private GItem item = null;
	
	// Number of stacks of the item held
	private int stackSize = 0;
	
	// Constructor
	protected InventoryTile(int xIndex, int yIndex, int xBuf, int yBuf) {
		super();
		
		// Set the indices for this tile
		this.xIndex = xIndex;
		this.yIndex = yIndex;
		
		// Adjust buffer sizes
		this.xBuffer = (int) (xBuf * GameInitializer.scaleFactor);
		this.yBuffer = (int) (yBuf * GameInitializer.scaleFactor);
		this.stackTextX = (int) (this.stackTextX * GameInitializer.scaleFactor);
		this.stackTextY = (int) (this.stackTextY * GameInitializer.scaleFactor);
		
		// Scale the width and height
		this.tileWidth = (int) (this.tileWidth * GameInitializer.scaleFactor);
		this.tileHeight = (int) (this.tileHeight * GameInitializer.scaleFactor);
		
		// Scale the stack font
		this.stackFontSize = (int) (this.stackFontSize * GameInitializer.scaleFactor);
		this.stackFont = new Font(Font.SERIF, Font.BOLD, this.stackFontSize);
		
		this.upScaleFactor = this.upScaleFactor * GameInitializer.scaleFactor;
		
		// Set size of panels
		this.setMinimumSize(new Dimension(tileWidth, tileHeight));
		this.setMaximumSize(new Dimension(tileWidth, tileHeight));
		
		// Mouse Listener for selecting items
		this.addMouseListener(new MouseAdapter() {
		     @Override
		     public void mousePressed(MouseEvent mouseEvent) {
		    	 InventoryScreen.leapSelected(InventoryTile.this.xIndex, InventoryTile.this.yIndex);
		     }
		});
		
		this.setBackground(Color.LIGHT_GRAY);
	}
	
	// Use the tile's item
	public boolean useItem() {
		// If tile is empty, don't do anything
		if(this.item == null) {
			return false;
		}
		
		// Check if the item uses correctly
		if(this.item.use()) {
			if(!(item instanceof Weapon)) {
				// If we're not a weapon, remove one count of the
				// item from the inventory
				if(this.stackSize >= this.item.maxStack) {
					// If a full stack, search for a smaller stack to remove from
					InventoryScreen.removeFromSmallestStack(this.item);
				} else if(this.stackSize > 1) {
					// If not a full stack (but still more than 1), decrease stack count by 1
					this.decrementStack();
				} else {
					// Otherwise, clear the item
					this.clearItem();
					InventoryScreen.organizeInventoryScreen();
					InventoryScreen.incrementItemCount(-1);
				}
			}
			// Defocus item from InfoScreen
			InfoScreen.defocusIfItem();
			
			this.repaint();
			return true;
		} else {
			// If item didn't use properly or was null, return false
			LogScreen.log("Using "+this.item.getName()+" didn't work.", GColors.ITEM);
			return false;
		}
	}
	
	// Sets the item image
	public void setItemPath(String filepath) {
		try {
			File file = new File(filepath);
			URL url = file.toURI().toURL();
			this.itemImage = new ImageIcon(url).getImage();
		} catch (Exception e) {
			System.out.println(filepath + " not found.");
			e.printStackTrace();
		}
		this.itemImage = ImageHandler.scaleImage(this.itemImage, 100, 100, upScaleFactor, upScaleFactor);
		this.repaint();
	}
	
	// Sets the border image
	public void setBorderPath(String filepath) {
		try {
			File file = new File(filepath);
			URL url = file.toURI().toURL();
			this.borderImage = new ImageIcon(url).getImage();
		} catch (Exception e) {
			System.out.println(filepath + " not found.");
			e.printStackTrace();
		}
		this.borderImage = ImageHandler.scaleImage(this.borderImage, 100, 100, upScaleFactor, upScaleFactor);
		this.repaint();
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(itemImage, this.xBuffer, this.yBuffer, this);
        g.drawImage(borderImage, this.xBuffer, this.yBuffer, this);
        if(this.stackSize > 1) {
        	g.setColor(Color.BLACK);
        	g.setFont(this.stackFont);
        	g.drawString(Integer.toString(this.stackSize), this.stackTextX, this.stackTextY);
        }
    }

	
	//------------------------------------
	// Getters and Setters
	
	// Sets background image for the panel
	protected void setBorderImage(Image borderImage) {
		this.borderImage = borderImage;
	}
	
	// Sets the item image for the panel
	protected void setItemImage(Image itemImage) {
		this.itemImage = itemImage;
	}
	
	protected int getInvTileWidth() {
		return this.tileWidth;
	}
	
	protected int getInvTileHeight() {
		return this.tileHeight;
	}
	
	// Sets the item on the tile and repaints the tile
	// with the correct new image
	public void setItem(GItem item) {
		this.item = item;
		this.stackSize = 1;
		this.setItemPath(item.imagePath);
	}
	
	// Clears the item from the tile
	public void clearItem() {
		this.item = null;
		this.stackSize = 0;
		this.setItemPath(GPath.NULL);
	}
	
	// Increases stack size by 1
	public void incrementStack() {
		this.stackSize += 1;
	}
	
	// Decreases stack size by 1
	public void decrementStack() {
		this.stackSize -= 1;
	}
	
	// Gets the item on the tile
	public GItem getItem() {
		return this.item;
	}
	
	// Gets the current stack size of the tile
	public int getStackSize() {
		return this.stackSize;
	}
	
}
