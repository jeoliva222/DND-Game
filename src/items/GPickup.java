package items;

import java.io.Serializable;

// Class that represents a pick-up on the GameScreen
public class GPickup implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = 8298057130151904666L;

	// X and Y coordinates on the screen
	public int xPos, yPos;
	
	// Item that the pickup holds
	public GItem item;

	// Constructor
	public GPickup(int x, int y, GItem item) {
		this.xPos = x;
		this.yPos = y;
		this.item = item;
	}
	
	// Controls adding an item to the inventory from the screen
	public void grabPickup() {
		/// Nothing yet
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
	public String getImage() {
		return this.item.imagePath;
	}
}
