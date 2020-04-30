package items;

import java.io.Serializable;

public abstract class GItem implements Serializable {

	// Serialization ID
	private static final long serialVersionUID = -7222629182476419595L;

	// Name of the item
	protected String name;
	
	// Description of the item
	protected String description;
	
	// Image Path of the item
	protected String imagePath;
	
	// Maximum amount of the item that can be stacked
	protected int maxStack;
	
	// Constructor
	public GItem(String name, String description, String imagePath, int maxStack) {
		this.name = name;
		this.description = description;
		this.imagePath = imagePath;
		this.maxStack = maxStack;
	}
	
	// Function that controls the logic of using an item
	// from the inventory screen
	abstract public boolean use();
	
	public String getName() {
		return this.name;
	}
	
	public String getDesc() {
		return this.description;
	}
	
	public String getImagePath() {
		return this.imagePath;
	}
	
	public int getMaxStack() {
		return this.maxStack;
	}
	
}
