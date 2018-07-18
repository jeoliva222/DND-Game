package ai;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import gui.GameScreen;
import gui.GameTile;
import tiles.MovableType;
import tiles.TileType;

// Class that has helper methods that help determine if Line-of-sight (LOS) exists
public class LineDrawer {
	
	// Checks if a position has sight towards another position
	public static boolean hasSight(int originX, int originY, int destX, int destY) {
		// Initialize variables for general direction of line being drawn
		int xDir = 0;
		int yDir = 0;
		double slope = 0.0;
		
		// Calculate the slope
		int dx = destX - originX;
		int dy = destY - originY;
		
		// Set X-Directional
		if(dx > 0) {
			xDir = 1;
		} else if (dx < 0) {
			xDir = -1;
		}
		
		// Set Y-Direction
		if(dy > 0) {
			yDir = 1;
		} else if (dy < 0) {
			yDir = -1;
		}
		
		// If straight line to target, use simpler algorithm
		// Otherwise, calculate slope
		if((dx == 0) || (dy == 0)) {
			ArrayList <GameTile> tiles =
					LineDrawer.drawStraightLine(originX, originY, destX, destY, xDir, yDir);
			return LineDrawer.isLOS(tiles);
		} else {
			slope = (double) dy / dx;
		}
		
		// Set dominant direction
		if(Math.abs(slope) > 1.0) {
			// Dominant direction is Y
			ArrayList <GameTile> tiles =
					LineDrawer.drawYLine(originX, originY, destX, destY, xDir, yDir, slope);
			return LineDrawer.isLOS(tiles);
		} else {
			// Dominant direction is X
			ArrayList <GameTile> tiles =
					LineDrawer.drawXLine(originX, originY, destX, destY, xDir, yDir, slope);
			return LineDrawer.isLOS(tiles);
		}
	}
	
	// Returns a boolean indicating whether it detected any walls in the list of tiles
	public static boolean isLOS(ArrayList<GameTile> tiles) {
		// Check through all tiles and return false if we hit a wall
		for(GameTile tile : tiles) {
			TileType tt = tile.getTileType();
			if(tt.getMovableType() == MovableType.WALL) {
				return false;
			}
		}
		// If no walls hit, then return true
		return true;
	}
	
	// Deals with straight line drawing, returning the set of intersecting GameTiles
	public static ArrayList<GameTile> drawStraightLine(int originX, int originY,
			int destX, int destY, int xDir, int yDir) {
		ArrayList<GameTile> tiles = new ArrayList<GameTile>();
		
		// Increment counter
		int currentX = originX + xDir;
		int currentY = originY + yDir;
		
		// Keep marking until we hit the destination
		while((currentX != destX) || (currentY != destY)) {
			tiles.add(GameScreen.getTile(currentX, currentY));
			currentX += xDir;
			currentY += yDir;
		}
		return tiles;
	}

	// Grabs an Arraylist of Gametiles that intersect the two points
	// Scans along the X-axis
	private static ArrayList<GameTile> drawXLine(int originX, int originY,
			int destX, int destY, int xDir, int yDir, double slope) {
		// Dominant direction is X
		ArrayList<GameTile> tiles = new ArrayList<GameTile>();
		double oldFloor = (double) originY;
		boolean middleFlag = false;
		
		// Initialize start and end coordinates
		double currentY = (double) originY + 0.5;
		double currentX = (double) originX + 0.5 + (0.5 * xDir);
		double endX = (double) destX + 0.5;
		
		// Repeat until we've reached the destination coordinate
		while(currentX != endX) {
			currentY += ((0.5 * slope) * xDir);
			double roundedY = LineDrawer.round(currentY, 2);
			
			// Check if we're on an even x-coordinate
			if((currentX % 1.0) == 0.0) {
				// Check if we're on an even y-coordinate as well
				if (roundedY % 1.0 == 0.0) {
					// If we are, set a flag that we're intersecting the middle of two tiles
					middleFlag = true;
				} else if(Math.floor(currentY) != oldFloor) {
					// If we've changed 'floors', mark the tile we passed
					int markedY = (int) Math.floor(currentY);
					int markedX = 0;
					// Set the x-marked coordinate differently depending on whether we're
					// going right or left x-wise
					if(xDir > 0) {
						markedX = (int) Math.floor(currentX) - xDir;
					} else {
						markedX = (int) Math.floor(currentX);
					}
					// Add the marked tile
					tiles.add(GameScreen.getTile(markedX, markedY));
				}
			} else {
				// Check if we've changed floors AND we didn't intersect 
				// in the middle of two tiles last pass
				if(Math.floor(currentY) != oldFloor && (!middleFlag)) {
					// If so, add marked tile
					int markedY = (int) Math.floor(currentY) - yDir;
					int markedX = (int) Math.floor(currentX);
					tiles.add(GameScreen.getTile(markedX, markedY));
				}
				// Add new marked tile that we crossed into
				int markedY = (int) Math.floor(currentY);
				int markedX = (int) Math.floor(currentX);
				tiles.add(GameScreen.getTile(markedX, markedY));
				
				// Reset middle flag if it had been set to 'true' before
				middleFlag = false;
			}
			
			// Increment currentY and update floor
			oldFloor = Math.floor(currentY);
			currentX += (0.5 * xDir);
			currentX = LineDrawer.round(currentX, 1);
		}
		
		// Return all the tiles we marked
		return tiles;
	}
	
	// Grabs an Arraylist of Gametiles that intersect the two points
	// Scans along the Y-axis
	private static ArrayList<GameTile> drawYLine(int originX, int originY,
			int destX, int destY, int xDir, int yDir, double slope) {
		// Dominant direction is Y
		ArrayList<GameTile> tiles = new ArrayList<GameTile>();
		double oldFloor = (double) originX;
		boolean middleFlag = false;
		
		// Initialize start and end coordinates
		double currentX = (double) originX + 0.5;
		double currentY = (double) originY + 0.5 + (0.5 * yDir);
		double endY = (double) destY + 0.5;
		
		// Repeat until we've reached the destination coordinate
		while(currentY != endY) {
			currentX += ((0.5 / slope) * yDir);
			double roundedX = LineDrawer.round(currentX, 2);
			
			// Check if we're on an even y-coordinate
			if((currentY % 1.0) == 0.0) {
				// Check if we're on an even x-coordinate as well
				if (roundedX % 1.0 == 0.0) {
					// If we are, set a flag that we're intersecting the middle of two tiles
					middleFlag = true;
				} else if(Math.floor(currentX) != oldFloor) {
					// If we've changed 'floors', mark the tile we passed
					int markedX = (int) Math.floor(currentX);
					int markedY = 0;
					// Set the y-marked coordinate differently depending on whether we're
					// going up or down y-wise
					if(yDir > 0) {
						markedY = (int) Math.floor(currentY) - yDir;
					} else {
						markedY = (int) Math.floor(currentY);
					}
					// Add the marked tile
					tiles.add(GameScreen.getTile(markedX, markedY));
				}
			} else {
				// Check if we've changed floors AND we didn't intersect 
				// in the middle of two tiles last pass
				if(Math.floor(currentX) != oldFloor && (!middleFlag)) {
					// If so, add marked tile
					int markedX = (int) Math.floor(currentX) - xDir;
					int markedY = (int) Math.floor(currentY);
					tiles.add(GameScreen.getTile(markedX, markedY));
				}
				// Add new marked tile that we crossed into
				int markedX = (int) Math.floor(currentX);
				int markedY = (int) Math.floor(currentY);
				tiles.add(GameScreen.getTile(markedX, markedY));
				
				// Reset middle flag if it had been set to 'true' before
				middleFlag = false;
			}
			
			// Increment currentY and update floor
			oldFloor = Math.floor(currentX);
			currentY += (0.5 * yDir);
			currentY = LineDrawer.round(currentY, 1);
		}
		
		// Return all the tiles we marked
		return tiles;
	}
	
	// Rounds a double: Function by Jonik
	static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
