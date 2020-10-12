package ai;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import characters.GCharacter;
import gui.GameScreen;
import helpers.DefaultHashMap;
import managers.EntityManager;
import tiles.MovableType;
import tiles.TileType;

/**
 * Class that implements A* Pathing for Enemy AI
 * @author jeoliva
 */
public class PathFinder {

	/**
	 * Performs the A* algorithm to find the next step in the most efficient path to the destination
	 * @param startX Starting X-position
	 * @param startY Starting Y-position
	 * @param endX Destination X-position
	 * @param endY Destination Y-position
	 * @param npc Character that is pathing
	 * @return Dimension of next coordinates to move to
	 */
	public static Dimension findPath(int startX, int startY, int endX, int endY, GCharacter npc) {
		// Define the start and goal positions as dimensions
		Dimension start = new Dimension(startX, startY);
		Dimension goal = new Dimension(endX, endY);
		
		// Define the closed and open sets
		HashSet<Dimension> closedSet = new HashSet<Dimension>();
		HashSet<Dimension> openSet = new HashSet<Dimension>();
		
		// Add starting node to open set
		openSet.add(start);
		
		// For each node, which node it can be most effectively reached from
		HashMap<Dimension, Dimension> cameFrom = new HashMap<Dimension, Dimension>();
		
		// For each node, the cost of getting from the start to that node
		// Default value of '1000' to indicate infinity
		DefaultHashMap<Dimension, Integer> gScore = new DefaultHashMap<Dimension, Integer>(1000);
		
		// GScore for start node is 0
		gScore.put(start, 0);
		
		// For each node, the total cost of getting from the start node to the
		// goal by passing that node.
		DefaultHashMap<Dimension, Integer> fScore = new DefaultHashMap<Dimension, Integer>(1000);
		
		// For the start node, the score is purely heuristic
		fScore.put(start, getEstimate(start, goal));
		
		// Iterate through each 
		while (!openSet.isEmpty()) {
			// Get lowest fScore coordinate in open set
			Dimension current = getLowestScore(openSet, fScore);
			
			// If the current node is our goal, reconstruct the path
			// and return true
			if (current.width == goal.width && current.height == goal.height) {
				// Calculate first move and return it
				return getFirstMove(cameFrom, current, start);
			}
			
			// Remove coordinate from open set and add to closed set
			openSet.remove(current);
			closedSet.add(current);
			
			// Fetch the neighbors of the coordinate
			HashSet<Dimension> neighbors = new HashSet<Dimension>();
			
			neighbors.add(new Dimension(current.width + 1, current.height));
			neighbors.add(new Dimension(current.width - 1, current.height));
			neighbors.add(new Dimension(current.width, current.height + 1));
			neighbors.add(new Dimension(current.width, current.height - 1));
			
			// Iterate through all neighboring coordinates
			for (Dimension neighbor: neighbors) {
				if (closedSet.contains(neighbor)) {
					// If node was already checked, do nothing
				} else {
					if (!isSpaceOpen(neighbor, npc)) {
						// If space is not free to move in,
						// add coordinate to closed set
						closedSet.add(neighbor);
					} else {
						// If the neighbor is not in open set, add it
						if (!openSet.contains(neighbor)) {
							openSet.add(neighbor);
						}
						
						// Fetch tentative new score
						Integer tentativeGScore = gScore.get(current) + 1;
						
						// If tentative score is not better than current score, do nothing
						if (tentativeGScore >= gScore.get(neighbor)) {
							// Do nothing
						} else {
							// Otherwise, we have a new best path. Record it!
							cameFrom.put(neighbor, current);
							gScore.put(neighbor, tentativeGScore);
							fScore.put(neighbor, (tentativeGScore + getEstimate(neighbor, goal)));
						}
					}
				}
			} // End of 'For' loop through Neighbor Set
		} // End of 'While' Loop through Open Set
		
		// If no path found, return failure
		return null;
	}
	
	/**
	 * Returns heuristic estimate of distance from one coordinate to another
	 * @param start Start coordinates
	 * @param goal Destination coordinates
	 * @return Heuristic estimate of distance
	 */
	private static Integer getEstimate(Dimension start, Dimension goal) {
		// Initialize output
		Integer output = 0;
		
		// Add difference in X and Y positions
		output += Math.abs(start.width - goal.width);
		output += Math.abs(start.height - goal.height);
		
		// If no distance to goal, return very low result to prioritize
		// goal node
		if (output == 0) {
			return -1000;
		}
		
		// Return resulting estimate
		return output;
	}
	
	/**
	 * Gets the lowest scoring coordinate in the set using the specified score map
	 * @param openSet Current set of checked coordinates
	 * @param scoreMap Score map correlating coordinates with distance scores
	 * @return Dimension coordinate with the lowest score
	 */
	private static Dimension getLowestScore(HashSet<Dimension> openSet, DefaultHashMap<Dimension, Integer> scoreMap) {
		// Initialize variables
		Random r = new Random();
		Dimension currentLow = new Dimension(0, 0);
		Integer lowestScore = 2000;
		
		// Iterate through open set, getting lowest score
		for (Dimension node: openSet) {
			Integer nodeScore = scoreMap.get(node);
			if (nodeScore > lowestScore) {
				// Do nothing if greater than lowest score
			} else if (lowestScore.equals(nodeScore)) {
				// If scores are equal, flip a coin and take one
				if (r.nextBoolean()) {
					currentLow = node;
				}
			} else if (nodeScore < lowestScore) {
				// If score is lower, take the score
				currentLow = node;
				lowestScore = scoreMap.get(node);
			}
		}
		
		// Return the lowest score found
		return currentLow;
	}
	
	/**
	 * Checks if coordinate can be moved into by a character
	 * @param coord Coordinate to check if space is movable
	 * @param npc Character to check if space is movable
	 * @return True if the specified NPC can move into the space | False if not
	 */
	private static boolean isSpaceOpen(Dimension coord, GCharacter npc) {
		try {
			// Fetch the TileType of the coordinate's tile
			TileType tt = GameScreen.getTile(coord.width, coord.height).getTileType();
			
			// If the NPC can't move into it, return false
			if (!MovableType.canMove(npc.getMoveTypes(), tt.getMovableType())) {
				return false;
			}
			
			// Check on collisions for other characters 
			for (GCharacter otherNPC : EntityManager.getInstance().getNPCManager().getCharacters()) {
				if (coord.width == otherNPC.getXPos() && coord.height == otherNPC.getYPos()) {
					return false;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// If coordinate is OOB, return false
			return false;
		}
		
		// If we failed none of the checks, return true
		return true;
	}
	
	/**
	 * Gets the first move on the best path to the destination
	 * @param cameFrom Map of movements from coordinate to coordinate
	 * @param current Current coordinate
	 * @param start Starting coordinate to trace back to
	 * @return Dimension coordinate indicating the first move away from the starting coordinate
	 */
	private static Dimension getFirstMove(HashMap<Dimension, Dimension> cameFrom, Dimension current, Dimension start) {
		Dimension firstMove = current;
		Dimension nextSpot;
		
		// Loop while we can trace our path backwards
		while (cameFrom.keySet().contains(firstMove)) {
			// Grab next location backwards
			nextSpot = cameFrom.get(firstMove);
			
			// If the next spot is the start location,
			// then break from the loop and return the first spot we move to.
			if (nextSpot == start) {
				break;
			}
			
			// Otherwise, update the coordinate to the next location backwards
			firstMove = nextSpot;
		}
		
		// Return the first move in the correct path
		return firstMove;
	}
	
}
