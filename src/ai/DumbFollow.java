package ai;

import java.util.Random;

import characters.GCharacter;

/**
 * AI Movement class for dumb AI follow methods
 * @author jeoliva
 */
public class DumbFollow {
	
	/**
	 * Tries to pursue a location blindly, disregarding walls and other obstacles
	 * @param distX X-distance from target in tiles
	 * @param distY Y-distance from target in tiles
	 * @param dx Distance at which character should try to move X-wise
	 * @param dy Distance at which character should try to move Y-wise
	 * @param npc Character to move
	 */
	public static void blindPursue(int distX, int distY, int dx, int dy, GCharacter npc) {
		if ((Math.abs(distX)) > (Math.abs(distY))) {
			// If movement in the x direction fails, try the y direction
			if (!npc.moveCharacter(dx, 0)) {
				npc.moveCharacter(0, dy);
			}
		} else if((Math.abs(distX)) < (Math.abs(distY)))  {
			// If movement in the y direction fails, try the x direction
			if (!npc.moveCharacter(0, dy)) {
				npc.moveCharacter(dx, 0);
			}
		} else {
			// If equi-distant both ways, randomly select whether to move x-wise or y-wise
			Random rand = new Random();
			int xOrY = rand.nextInt(2);
			
			// Depending on the random selector, try to move x-wise or y-wise first
			if (xOrY == 0) {
				// If movement in the x direction fails, try the y direction
				if (!npc.moveCharacter(dx, 0)) {
					npc.moveCharacter(0, dy);
				}
			} else {
				// If movement in the y direction fails, try the x direction
				if (!npc.moveCharacter(0, dy)) {
					npc.moveCharacter(dx, 0);
				}
			}
		}
		
	}
	
	/**
	 * Tries to pursue a location blindly, disregarding walls and other obstacles.
	 * Defaults character movement to 1 tile.
	 * @param distX X-distance from target in tiles
	 * @param distY Y-distance from target in tiles
	 * @param npc Character to move
	 */
	public static void blindPursue(int distX, int distY, GCharacter npc) {
		// Relative movement direction (Initialize at 0)
		int dx = 0;
		int dy = 0;
		
		// Calculate relative movement directions
		if (distX > 0) {
			dx = 1;
		} else if (distX < 0) {
			dx = -1;
		}
		
		if (distY > 0) {
			dy = 1;
		} else if (distY < 0) {
			dy = -1;
		}
		
		// Input variables into other method
		blindPursue(distX, distY, dx, dy, npc);
	}

}
