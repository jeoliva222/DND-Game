package items;

import gui.GameScreen;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.Ground;
import tiles.KeyDoor;
import tiles.TileType;

public class GKey extends GItem {

	// Serialization ID
	private static final long serialVersionUID = 6139074065068494470L;
	
	
	// Key-code for what type of door this unlocks
	protected int keycode;
	
	// Constructor
	public GKey(int keycode) {
		super("", "", GPath.NULL);
		this.keycode = keycode;
		
		// We don't want keys to be discarded
		this.isDiscardable = false;
		
		// Initialize variables of the key
		this.initializeKey();
	}
	
	// Initialize fields for the Key item based off its key-code
	private void initializeKey() {
		// Based off of the key-code, set name, description, and image
		switch(this.keycode) {
			case KeyDoor.YELLOW:
				this.name = "Yellow Key";
				this.description = "This key sends waves of static shock into your palm.";
				this.imagePath = GPath.createImagePath(GPath.PICKUP, GPath.KEY, "yellow_key.png");
				break;
			case KeyDoor.GREEN:
				this.name = "Green Key";
				this.description = "A key symbolizing the green growth of the earth.";
				this.imagePath = GPath.createImagePath(GPath.PICKUP, GPath.KEY, "green_key.png");
				break;
			case KeyDoor.RED:
				this.name = "Red Key";
				this.description = "A red key that eminates a pulsing warmth. Use next to red doors to open them.";
				this.imagePath = GPath.createImagePath(GPath.PICKUP, GPath.KEY, "red_key.png");
				break;
			case KeyDoor.BLUE:
				this.name = "Blue Key";
				this.description = "You see deep terrors looming behind the color of this key.";
				this.imagePath = GPath.createImagePath(GPath.PICKUP, GPath.KEY, "blue_key.png");
				break;
			case KeyDoor.SNAKEBASE:
				this.name = "Snake Base Key";
				this.description = "Key to the snake's stronghold at the center of the desert.";
				this.imagePath = GPath.createImagePath(GPath.PICKUP, GPath.KEY, "snakebase_key.png");
				break;
			case KeyDoor.COMMANDER:
				this.name = "Commander's Key";
				this.description = "A high-level access key held only by commanders. Use it to unlock doors of the snake clan.";
				this.imagePath = GPath.createImagePath(GPath.PICKUP, GPath.KEY, "commander_key.png");
				break;
			default:
				System.out.println("Keycode not found!");
				this.name = "MISSING NO. KEY";
				this.description = "You shouldn't be reading this.";
				this.imagePath = GPath.NULL;
				break;
		}
	}

	@Override
	public boolean use() {
		// Get player's coordinates on the screen
		int plrX = EntityManager.getInstance().getPlayer().getXPos();
		int plrY = EntityManager.getInstance().getPlayer().getYPos();
		
		// Initialize TileType variables
		TileType upTile, downTile, leftTile, rightTile;
		
		// Fetch TileTypes around player
		upTile = this.fetchTileType(plrX, plrY - 1);
		downTile = this.fetchTileType(plrX, plrY + 1);
		leftTile = this.fetchTileType(plrX - 1, plrY);
		rightTile = this.fetchTileType(plrX + 1, plrY);
		
		// Boolean to check if any doors were opened
		boolean openFlag = false;
		
		// Flags for each tile
		boolean upFlag = this.tryKey(upTile, plrX, plrY - 1);
		boolean downFlag = this.tryKey(downTile, plrX, plrY + 1);
		boolean leftFlag = this.tryKey(leftTile, plrX - 1, plrY);
		boolean rightFlag = this.tryKey(rightTile, plrX + 1, plrY);
		
		// Check tiles for unlocking
		openFlag = (upFlag || downFlag || leftFlag || rightFlag);
		
		// Log message and play sound about success of using the key if successful
		if(openFlag) {
			SoundPlayer.playWAV(GPath.createSoundPath("Key_USE.wav"));
			LogScreen.log("You used the key.", GColors.ITEM);
		}
		
		// Return the success/failure of using the key
		return openFlag;
	}
	
	// Try to check if the TileType is a KeyDoor and if the key matches the door
	private boolean tryKey(TileType tile, int x, int y) {
		if(tile != null && tile instanceof KeyDoor) {
			KeyDoor keyTile = (KeyDoor) tile;
			if(this.keycode == keyTile.getKeyCode()) {
				GameScreen.getTile(x, y).setTileType(new Ground());
				return true;
			}
		}
		// If check was not true, return false
		return false;
	}
	
	// Fetches the TileType of a GameTile, returning null if it is OOB
	private TileType fetchTileType(int x, int y) {
		try {
			TileType output = GameScreen.getTile(x, y).getTileType();
			return output;
		} catch (Exception e) {
			return null;
		}
	}

}
