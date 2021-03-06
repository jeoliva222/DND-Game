package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import characters.allies.Player;
import helpers.GColors;
import helpers.GPath;
import items.GItem;
import items.GPickup;
import managers.EntityManager;
import weapons.Armory;

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
		setPreferredSize(size);
		
		// Set up correct layout
		setLayout(null);
		
		// Scale buffers
		rowBuffer = (int) (rowBuffer * GameInitializer.scaleFactor);
		colBuffer = (int) (colBuffer * GameInitializer.scaleFactor);
		
		// Set all tiles
		for (int y = 0; y < invRows; y++) {
			for (int x = 0; x < invCols; x++) {
				// Create new InventoryTile and add it screen/references
				InventoryTile newTile = new InventoryTile(x, y, 0, 0);
				invTiles[y][x] = newTile;
				int tileWidth = newTile.getInvTileWidth();
				int tileHeight = newTile.getInvTileHeight();
				newTile.setBounds(((x * (tileWidth + rowBuffer)) + rowBuffer), ((y * (tileHeight + colBuffer)) + colBuffer),
						tileWidth, tileHeight);
				add(newTile);
			}
		}
		
		// Set border image on first tile
		invTiles[currentY][currentX].setBorderPath(selectBorder);
		
		// Set background color
		setBackground(new Color(221, 221, 136));
	}
	
	// Add item to inventory screen
	public static boolean addItem(GItem item) {
		// Check for open stacks of same item
		for (int y = 0; y < invRows; y++) {
			for (int x = 0; x < invCols; x++) {
				InventoryTile tile = invTiles[y][x];
				if (tile.getItem() != null && tile.getItem().getName().equals(item.getName())) {
					if (invTiles[y][x].getStackSize() < item.getMaxStack()) {
						invTiles[y][x].incrementStack();
						invTiles[y][x].repaint();
						return true;
					}
				}
			}
		}
		
		// If we're out of space in our inventory, don't add
		if (currentItems >= maxItems) {
			LogScreen.log("Inventory is too full!", GColors.ITEM);
			return false;
		}
		
		// Figure out which index we need to add at
		int addX = currentItems % invCols;
		int addY = Math.floorDiv(currentItems, invCols);
		
		// Add the item
		invTiles[addY][addX].setItem(item);
		
		// Increment the counter of current items
		currentItems += 1;
		
		return true;
	}
	
	// Shifts which tile in the inventory is selected
	public static void shiftSelected(int dx) {
		// Deselect current tile
		invTiles[currentY][currentX].setBorderPath(GPath.NULL);
		
		// Increment indices
		currentX += dx;
		
		// Check if we're going out of the bounds of the row
		if (currentX >= invCols) {
			// If going over right-wise
			// Set X-index back to 0
			currentX = 0;
			
			// Increment Y index
			currentY += 1;
			
			// If going over the top Y-wise, set to 0
			if (currentY >= invRows) {
				currentY = 0;
			}
		} else if (currentX < 0) {
			// If going over left-wise
			// Set X-index to right-hand side of lower row
			currentX = (byte) (invCols - 1);
			
			// Decrement Y index
			currentY += -1;
			
			// If going under index Y-wise, set to Y to last row index
			if (currentY < 0) {
				currentY = (byte) (invRows - 1);
			}
		}
		
		// Set border on the new current selected tile
		invTiles[currentY][currentX].setBorderPath(selectBorder);
		
		// Set focus on new item if not null
		GItem newItem = invTiles[currentY][currentX].getItem();
		if (newItem != null) {
			InfoScreen.setItemFocus(newItem);
		} else {
			InfoScreen.defocusIfItem();
		}
	}
	
	// Leaps selected tile to new index
	public static void leapSelected(int newX, int newY) {
		// Deselect current tile
		invTiles[currentY][currentX].setBorderPath(GPath.NULL);
		
		// Set new selected index
		currentX = (byte) newX;
		currentY = (byte) newY;
		
		// Set border on the new current selected tile
		invTiles[currentY][currentX].setBorderPath(selectBorder);
		
		// Set focus on new item if not null
		GItem newItem = invTiles[currentY][currentX].getItem();
		if (newItem != null) {
			InfoScreen.setItemFocus(newItem);
		} else {
			InfoScreen.defocusIfItem();
		}
	}
	
	// Use the currently selected item
	public static boolean useSelected() {
		// If the player isn't alive, return false
		if (!EntityManager.getInstance().getPlayer().isAlive()) {
			return false;
		}
		
		// Get result of using item
		boolean result = invTiles[currentY][currentX].useItem();
		
		// Return the result
		return result;
	}
	
	// Discards/Trashes the currently selected item
	public static boolean discardSelected() {
		InventoryTile tile = invTiles[currentY][currentX];
		
		// Fetch reference to player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If the player isn't alive or item is null, return false
		if (!player.isAlive() || (tile.getItem() == null)) {
			return false;
		}
		
		// Log what we're discarding
		LogScreen.log("Player discarded "
				+tile.getItem().getName()+".", GColors.ITEM);
		
		// Place item on the ground (If not our bare fists)
		if (!tile.getItem().getName().equals(Armory.bareFists.getName())) {
			EntityManager.getInstance().getPickupManager().addPickup(new GPickup(
					player.getXPos(), player.getYPos(), tile.getItem()));
		}
		
		// Discard the item
		if (tile.getStackSize() >= tile.getItem().getMaxStack()) {
			// If we have a full stack of the item, look for the smallest stack to discard from
			removeFromSmallestStack(tile.getItem());
		} else if (tile.getStackSize() > 1) {
			// If multiple stacks of the item (but not a max stack), only discard one
			tile.decrementStack();
			tile.repaint();
		} else {
			// If only one stack left, clear the item
			tile.clearItem();
			
			// Reorganize the inventory
			organizeInventoryScreen();
			
			// Defocus InfoScreen
			InfoScreen.defocusIfItem();
			
			// Decrement current items counter
			currentItems += -1;
		}
		
		return true;
	}
	
	// Reorganizes the items in the inventory around current selection
	public static void organizeInventoryScreen() {
		// Iterate through remaining items, shifting each one slot back
		int startX = (currentX + 1);
		Dimension oldIndex = new Dimension(currentX, currentY);
		for (int y = currentY; y < invRows; y++) {
			for (int x = startX; x < invCols; x++) {
				// Get the item
				InventoryTile currentTile = invTiles[y][x];
				GItem itemToShift = currentTile.getItem();
				
				// If the item is null, we've finished organizing
				if (itemToShift == null) {
					return;
				}
				
				// Shift item to last index
				invTiles[oldIndex.height][oldIndex.width].setItem(itemToShift);
				invTiles[oldIndex.height][oldIndex.width].setStackSize(currentTile.getStackSize());
				
				// Clear item from this index
				currentTile.clearItem();
				
				// Update index
				oldIndex = new Dimension(x, y);
			}
			startX = 0;
		}
	}
	
	// Reorganizes the items in the inventory around a particular index
	public static void organizeInventoryScreen(int xIndex, int yIndex) {
		// Iterate through remaining items, shifting each one slot back
		int startX = (xIndex + 1);
		Dimension oldIndex = new Dimension(xIndex, yIndex);
		for (int y = yIndex; y < invRows; y++) {
			for (int x = startX; x < invCols; x++) {
				// Get the item
				InventoryTile currentTile = invTiles[y][x];
				GItem itemToShift = currentTile.getItem();
				
				// If the item is null, we've finished organizing
				if (itemToShift == null) {
					return;
				}
				
				// Shift item to last index
				invTiles[oldIndex.height][oldIndex.width].setItem(itemToShift);
				invTiles[oldIndex.height][oldIndex.width].setStackSize(currentTile.getStackSize());
				
				// Clear item from this index
				currentTile.clearItem();
				
				// Update index
				oldIndex = new Dimension(x, y);
			}
			startX = 0;
		}
	}
	
	// If we have a full stack of the item, look for the smallest stack to discard from
	// Check for open stacks of same item
	public static boolean removeFromSmallestStack(GItem item) {
		for (int y = (invRows - 1); y >= 0; y--) {
			for (int x = (invCols - 1); x >= 0; x--) {
				InventoryTile stackTile = invTiles[y][x];
				if (stackTile.getItem() != null && stackTile.getItem().getName().equals(item.getName())) {
					// Discard from this stack instead
					if (stackTile.getStackSize() > 1) {
						// If multiple stacks of the item (but not a max stack), only discard one
						stackTile.decrementStack();
						stackTile.repaint();
					} else {
						// If only one stack left, clear the item
						stackTile.clearItem();

						// Reorganize the inventory
						organizeInventoryScreen(x, y);
						
						// Defocus InfoScreen
						InfoScreen.defocusIfItem();
						
						// Decrement current items counter
						currentItems += -1;
					}
					
					// We removed an item, so return true
					return true;
				}
			}
		}
		
		// No items removed, so return false
		return false;
	}
	
	// *******************
	// Getters and Setters
	
	protected static void incrementItemCount(int dx) {
		currentItems += dx;
	}
	
	public static InventoryTile getTile(int x, int y) {
		return invTiles[y][x];
	}
	
	// Fetch inventory items in the form of an array
	public static GItem[] getItemArray() {
		List<GItem> itemList = new ArrayList<GItem>();
		
		int count = 0;
		for (int row = 0; row < invRows; row++) {
			for (int col = 0; col < invCols; col++) {
				if (count >= currentItems) {
					return itemListToArray(itemList);
				}
				
				// Add item to output for each stack we have
				for (int i = 0; i < invTiles[row][col].getStackSize(); i++) {
					GItem item = invTiles[row][col].getItem();
					itemList.add(item);
				}

				// Increment count
				count += 1;
			}
		}
		
		return itemListToArray(itemList);
	}
	
	public static GItem[] itemListToArray(List<GItem> list) {
		GItem[] output = new GItem[list.size()];
		for (int i = 0; i < list.size(); i++) {
			output[i] = list.get(i);
		}
		
		return output;
	}
	
	// Set inventory items based off input array
	public static void setItemArray(GItem[] newInv) {
		// Clear inventory
		for (int row = 0; row < invRows; row++) {
			for (int col = 0; col < invCols; col++) {
				invTiles[row][col].clearItem();
			}
		}
		
		// Reset item count
		currentItems = 0;
		
		// For each item, add it to the inventory
		for (GItem item : newInv) {
			addItem(item);
		}
	}
	
	public static int getXIndex() {
		return currentX;
	}
	
	public static int getYIndex() {
		return currentY;
	}
	
	public static int getInvWidth() {
		return inventoryWidth;
	}
	
	public static int getInvHeight() {
		return inventoryHeight;
	}

	public static boolean isFull() {
		return (currentItems >= maxItems);
	}
}
