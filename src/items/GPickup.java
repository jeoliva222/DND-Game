package items;

import java.io.Serializable;

// Class that represents a pick-up on the GameScreen
public class GPickup implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = 8298057130151904666L;

	// X and Y coordinates on the screen
	protected int xPos, yPos;
	
	// Item that the pickup holds
	protected GItem item;

	// Constructor
	public GPickup(int x, int y, GItem item) {
		this.xPos = x;
		this.yPos = y;
		this.item = item;
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
	public GItem getItem() {
		return this.item;
	}
	
	public String getImage() {
		return this.item.getImagePath();
	}
}
