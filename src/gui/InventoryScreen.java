package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import helpers.GColors;
import helpers.GPath;
import items.GItem;
import managers.EntityManager;

public class InventoryScreen extends JPanel {

	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Width and Height of Inventory Screen
	private static int inventoryHeight = (GameScreen.getGHeight() + LogScreen.getLHeight()) / 2;
	private static int inventoryWidth = InventoryScreen.inventoryHeight * 4 / 7;
	
	// Grid of Inventory Tiles
	private static byte invRows = 4;
	private static byte invCols = 2;
	private static InventoryTile[][] invTiles = new InventoryTile[invRows][invCols];
	
	// Current / Max items in inventory
	private static byte maxItems = (byte) (invRows * invCols);
	private static byte currentItems = 0;
	
	// Buffer spaces in between tiles
	private static int rowBuffer = 22;
	private static int colBuffer = 13;
	
	// Current index of what item we have selected
	private static byte currentX = 0;
	private static byte currentY = 0;
	
	// Path to select border
	private static final String selectBorder = GPath.createImagePath(GPath.TILE, GPath.GENERIC, "area_charge.png");
	
	// Constructor
	protected InventoryScreen() {
		super();
		
		// Set preferred size of the the log screen
		Dimension size = new Dimension(inventoryWidth, inventoryHeight);
		this.setPreferredSize(size);
		
		// Set up correct layout
		this.setLayout(null);
		
		// Scale buffers
		InventoryScreen.rowBuffer = (int) (InventoryScreen.rowBuffer * GameInitializer.scaleFactor);
		InventoryScreen.colBuffer = (int) (InventoryScreen.colBuffer * GameInitializer.scaleFactor);
		
		// Set all tiles
		for(int y = 0; y < invRows; y++) {
			for(int x = 0; x < invCols; x++) {
				// Create new InventoryTile and add it screen/references
				InventoryTile newTile = new InventoryTile(x, y, 0, 0);
				InventoryScreen.invTiles[y][x] = newTile;
				int tileWidth = newTile.getInvTileWidth();
				int tileHeight = newTile.getInvTileHeight();
				newTile.setBounds(((x * (tileWidth + rowBuffer)) + rowBuffer), ((y * (tileHeight + colBuffer)) + colBuffer),
						tileWidth, tileHeight);
				this.add(newTile);
			}
		}
		
		// Set border image on first tile
		InventoryScreen.invTiles[currentY][currentX].setBorderPath(selectBorder);
		
		// Set background color
		this.setBackground(new Color(221, 221, 136));
	}
	
	// Add item to inventory screen
	public static boolean addItem(GItem item) {
		// If we're out of space in our inventory, don't add
		if(currentItems >= maxItems) {
			LogScreen.log("Inventory is too full!", GColors.ITEM);
			return false;
		}
		
		// Figure out which index we need to add at
		int addX = currentItems % invCols;
		int addY = Math.floorDiv(currentItems, invCols);
		
		// Add the item
		InventoryScreen.invTiles[addY][addX].setItem(item);
		
		// Increment the counter of current items
		InventoryScreen.currentItems += 1;
		
		return true;
	}
	
	// Shifts which tile in the inventory is selected
	public static void shiftSelected(int dx) {
		// Deselect current tile
		InventoryScreen.invTiles[currentY][currentX].setBorderPath(GPath.NULL);
		
		// Increment indices
		InventoryScreen.currentX += dx;
		
		// Check if we're going out of the bounds of the row
		if(currentX >= invCols) {
			// If going over right-wise
			// Set X-index back to 0
			InventoryScreen.currentX = 0;
			
			// Increment Y index
			InventoryScreen.currentY += 1;
			
			// If going over the top Y-wise, set to 0
			if(currentY >= invRows) {
				InventoryScreen.currentY = 0;
			}
			
		} else if(currentX < 0) {
			// If going over left-wise
			// Set X-index to right-hand side of lower row
			InventoryScreen.currentX = (byte) (InventoryScreen.invCols - 1);
			
			// Decrement Y index
			InventoryScreen.currentY += -1;
			
			// If going under index Y-wise, set to Y to last row index
			if(currentY < 0) {
				InventoryScreen.currentY = (byte) (InventoryScreen.invRows - 1);
			}
		}
		
		// Set border on the new current selected tile
		InventoryScreen.invTiles[currentY][currentX].setBorderPath(selectBorder);
		
		// Set focus on new item if not null
		GItem newItem = InventoryScreen.invTiles[currentY][currentX].getItem();
		if(newItem != null) {
			InfoScreen.setItemFocus(newItem);
		} else {
			InfoScreen.defocusIfItem();
		}
	}
	
	// Leaps selected tile to new index
	public static void leapSelected(int newX, int newY) {
		// Deselect current tile
		InventoryScreen.invTiles[currentY][currentX].setBorderPath(GPath.NULL);
		
		// Set new selected index
		InventoryScreen.currentX = (byte) newX;
		InventoryScreen.currentY = (byte) newY;
		
		// Set border on the new current selected tile
		InventoryScreen.invTiles[currentY][currentX].setBorderPath(selectBorder);
		
		// Set focus on new item if not null
		GItem newItem = InventoryScreen.invTiles[currentY][currentX].getItem();
		if(newItem != null) {
			InfoScreen.setItemFocus(newItem);
		} else {
			InfoScreen.defocusIfItem();
		}
	}
	
	// Use the currently selected item
	public static boolean useSelected() {
		// If the player isn't alive, return false
		if(!EntityManager.getPlayer().isAlive()) {
			return false;
		}
		
		// Get result of using item
		boolean result = InventoryScreen.invTiles[currentY][currentX].useItem();
		
		// Return the result
		return result;
	}
	
	// Discards/Trashes the currently selected item
	public static boolean discardSelected() {
		// If the player isn't alive or item is null, return false
		if(!EntityManager.getPlayer().isAlive() ||
				(InventoryScreen.invTiles[currentY][currentX].getItem() == null)) {
			return false;
		}
		
		// If item is not discardable, return false
		if(!InventoryScreen.invTiles[currentY][currentX].getItem().isDiscardable) {
			LogScreen.log("You can't discard this item!", GColors.ITEM);
			return false;
		}
		
		// Log what we're discarding
		LogScreen.log("Player discarded "
				+InventoryScreen.invTiles[currentY][currentX].getItem().getName()+".", GColors.ITEM);
		
		// Clear the item
		InventoryScreen.invTiles[currentY][currentX].clearItem();
		
		// Reorganize the inventory
		InventoryScreen.organizeInventoryScreen();
		
		// Defocus InfoScreen
		InfoScreen.defocusIfItem();
		
		// Decrement current items counter
		InventoryScreen.currentItems += -1;
		
		return true;
	}
	
	// Reorganizes the items in the inventory
	public static void organizeInventoryScreen() {
		// Iterate through remaining items, shifting each one slot back
		int startX = (InventoryScreen.currentX + 1);
		Dimension oldIndex = new Dimension(InventoryScreen.currentX, InventoryScreen.currentY);
		for(int y = InventoryScreen.currentY; y < invRows; y++) {
			for(int x = startX; x < invCols; x++) {
				// Get the item
				GItem itemToShift = InventoryScreen.invTiles[y][x].getItem();
				
				// If the item is null, we've finished organizing
				if(itemToShift == null) {
					return;
				}
				
				// Shift item to last index
				InventoryScreen.invTiles[oldIndex.height][oldIndex.width].setItem(itemToShift);
				
				// Clear item from this index
				InventoryScreen.invTiles[y][x].clearItem();
				
				// Update index
				oldIndex = new Dimension(x, y);
			}
			startX = 0;
		}
	}
	
	// *******************
	// Getters and Setters
	
	protected static void incrementItemCount(int dx) {
		InventoryScreen.currentItems += dx;
	}
	
	public static InventoryTile getTile(int x, int y) {
		return InventoryScreen.invTiles[y][x];
	}
	
	// Fetch inventory items in the form of an array
	public static GItem[] getItemArray() {
		GItem[] output = new GItem[(InventoryScreen.invCols * InventoryScreen.invRows)];
		
		int count = 0;
		for(int row = 0; row < invRows; row++) {
			for(int col = 0; col < invCols; col++) {
				if(count >= InventoryScreen.currentItems) {
					return output;
				}
				GItem item = InventoryScreen.invTiles[row][col].getItem();
				//System.out.println("Count: "+Integer.toString(count)+" / Name: "+item.getName());
				output[count] = item;
				count += 1;
			}
		}
		
		return output;
	}
	
	// Set inventory items based off input array
	public static void setItemArray(GItem[] newInv) {
		// Clear inventory
		for(int row = 0; row < invRows; row++) {
			for(int col = 0; col < invCols; col++) {
				InventoryScreen.invTiles[row][col].clearItem();
			}
		}
		
		// Fill inventory with loaded items
		int count = 0;
		for(int row = 0; row < invRows; row++) {
			for(int col = 0; col < invCols; col++) {
				// If next item is null, don't continue
				if(newInv[count] == null) {
					InventoryScreen.currentItems = (byte) count;
					return;
				}
				
				// Set new item
				InventoryScreen.invTiles[row][col].setItem(newInv[count]);
				count += 1;
			}
		}
		
		InventoryScreen.currentItems = (byte) count;
	}
	
	public static int getXIndex() {
		return InventoryScreen.currentX;
	}
	
	public static int getYIndex() {
		return InventoryScreen.currentY;
	}
	
	public static int getInvWidth() {
		return InventoryScreen.inventoryWidth;
	}
	
	public static int getInvHeight() {
		return InventoryScreen.inventoryHeight;
	}

	public static boolean isFull() {
		return (InventoryScreen.currentItems >= InventoryScreen.maxItems);
	}
}
