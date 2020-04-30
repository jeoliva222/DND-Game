package ai;

import java.awt.Dimension;
import java.util.ArrayList;

import gui.GameScreen;
import gui.GameTile;
import tiles.MovableType;

/**
 * Class that does a 'virus spread' check to see if two coordinates are on the same 'island'
 * @author jeoliva
 */
public class IslandChecker {
	
	// List of coordinates that have been checked
	private static ArrayList<Dimension> checkedCoords = new ArrayList<Dimension>();

	/**
	 * Performs a check to see if two positions are on the same 'island'
	 * @param originX Origin X-position
	 * @param originY Origin Y-position
	 * @param destX Destination X-position
	 * @param destY Destination Y-position
	 * @param mt MovableType mask indicating tiles that are valid for the same 'island'
	 * @return True if positions are connected by 'island' | False if not
	 */
	public static boolean virusStart(int originX, int originY, int destX, int destY, Short mt) {
		// Start by clearing checked coordinates list
		IslandChecker.checkedCoords.clear();
		IslandChecker.checkedCoords = null;
		IslandChecker.checkedCoords = new ArrayList<Dimension>();
		
		
		// Start the virus spread at the origin location
		return virusSpread(originX, originY, destX, destY, mt);
	}
	
	/**
	 * Recursing function that spreads 'virus' check to nearby tiles
	 * @param x Current tile X-position
	 * @param y Current tile Y-position
	 * @param destX Destination X-position
	 * @param destY Destination Y-position
	 * @param mt MovableType mask indicating tiles that are valid to spread 'virus' to
	 * @return True if located destination | False if not
	 */
	private static boolean virusSpread(int x, int y, int destX, int destY, Short mt) {
		// Firstly, check if we've already seen this coordinate
		Dimension coord = new Dimension(x, y);
		if (checkedCoords.contains(coord)) {
			// If we've seen it, return false
			return false;
		} else {
			// If we haven't seen it, add it to the list of seen coordinates and continue
			checkedCoords.add(coord);
		}
		
		// Surround in try/catch to detect OOB checks and return false
		try {
			// Fetch the tile from the GameScreen
			GameTile tile = GameScreen.getTile(x, y);
			
			// Check if the tile is the right MovableType
			if (MovableType.canMove(mt, tile.getTileType().getMovableType())) {
				// Check if we've reached our destination
				if (x == destX && y == destY) {
					// If we have, return true
					return true;
				} else {
					// Otherwise, continue to spread
					return (virusSpread(x-1, y, destX, destY, mt) ||
							virusSpread(x+1, y, destX, destY, mt) ||
							virusSpread(x, y-1, destX, destY, mt) ||
							virusSpread(x, y+1, destX, destY, mt));
				}
			} else {
				// If not the correct MovableType, return false
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// We're out of bounds, so return false
			return false;
		}
	}
	
}
