package tiles;

import helpers.GPath;

public class KeyDoor extends TileType {
	
	// Serialization ID
	private static final long serialVersionUID = 4889367543245630675L;
	
	// Key-codes for the door
	// AKA: What type of key unlocks the door
	public static final int YELLOW = 0;
	public static final int GREEN = 1;
	public static final int RED = 2;
	public static final int BLUE = 3;
	
	// Key-code unique to the specific instance of the door
	protected int keycode;
	
	// Constructor
	public KeyDoor(int keycode) {
		this.keycode = keycode;
		this.moveType = MovableType.WALL;
	}

	@Override
	public String selectImage() {
		// Fetch key-code to display image for the correct door type
		switch(this.keycode) {
			case YELLOW:
				return GPath.createImagePath(GPath.TILE, GPath.DOOR, "yellow_door.png");
			case GREEN:
				return GPath.createImagePath(GPath.TILE, GPath.DOOR, "green_door.png");
			case RED:
				return GPath.createImagePath(GPath.TILE, GPath.DOOR, "red_door.png");
			case BLUE:
				return GPath.createImagePath(GPath.TILE, GPath.DOOR, "blue_door.png");
			default:
				System.out.println("Couldn't find the right door image!");
				return GPath.NULL;
		}
		
		
	}

	@Override
	public void onStep() {
		// Do nothing
	}
	
	public int getKeyCode() {
		return this.keycode;
	}

}
