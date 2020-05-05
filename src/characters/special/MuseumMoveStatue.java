package characters.special;

import characters.Signpost;
import characters.allies.Player;
import managers.EntityManager;

/**
 * Class representing special entity used in the Museum area 
 * that moves when the player reaches a certain tile
 * @author jeoliva
 */
public class MuseumMoveStatue extends Signpost {

	// Serialization ID
	private static final long serialVersionUID = -7556002728280912471L;
	
	//----------------------------
	
	// Additional parameters
	
	// Tile at which the move triggers
	private int triggerX, triggerY;
	
	// New tile to move to
	private int moveX, moveY;
	
	// Flag dictating whether statue has moved yet
	private boolean hasMoved = false;

	public MuseumMoveStatue(int startX, int startY, String imageStr, String message,
			int triggerX, int triggerY, int moveX, int moveY) {
		super(startX, startY, imageStr, message);
		this.triggerX = triggerX;
		this.triggerY = triggerY;
		this.moveX = moveX;
		this.moveY = moveY;
	}
	
	@Override
	public void takeTurn() {
		// Get reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if (!isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Check to move if player is in location
		if ((!hasMoved) && (plrX == triggerX && plrY == triggerY)) {
			// Update position and new origin location
			this.xPos = moveX;
			this.yPos = moveY;
			this.xOrigin = xPos;
			this.yOrigin = yPos;
			
			// Flag that we've moved
			this.hasMoved = true;
		}
	}
	
}
