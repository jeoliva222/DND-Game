package ai;

import java.awt.Dimension;
import java.util.Random;

import characters.GCharacter;

// Static controller class that handles idle NPC movement patterns
public class IdleController {

	// Handles idle movement for NPC based off of their PatrolPattern
	public static void moveIdle(GCharacter npc) {
		switch(npc.getPatrolPattern()) {
			case STATIONARY: 
				// Do nothing
				return;
			case WANDER:
				IdleController.wanderCharacter(npc);
				break;
			case PATROL:
				// Patrol the NPC
				IdleController.patrolRandom(npc);
				break;
			case PATROL_CW:
				// Patrol the NPC CW
				IdleController.patrolStatic(npc, true);
				break;
			case PATROL_CCW:
				// Patrol the NPC CCW
				IdleController.patrolStatic(npc, false);
				break;
			case SURFACE_CW:
				// Surface follow CW
				IdleController.surfaceFollow(npc, true);
				break;
			case SURFACE_CCW:
				// Surface follow CCW
				IdleController.surfaceFollow(npc, false);
				break;
			default: 
				// Do nothing
				break;
		}
		
	}
	
	// Does the movement logic for a wandering NPC that sometimes fidgets in position
	private static void wanderCharacter(GCharacter npc) {
		// Sometimes randomly move in one of four directions
		Random r = new Random();
		// Gets a number 0 - 3: Determines if NPC should move or not
		int shouldMove = r.nextInt(4);
		
		// Most of the time, don't even try to move
		if(shouldMove != 0) {
			return;
		}
		
		// Only move 1/4 of the time
		
		// Gets 0 or 1
		int xOrY = r.nextInt(2);
		// Gets -1 or 1
		int posOrNeg = IdleController.setNewDirection();
		
		// Randomly move X-wise or Y-wise
		if(xOrY == 0) {
			//X-wise
			if(!npc.moveCharacter(posOrNeg, 0)) {
				npc.moveCharacter((-posOrNeg), 0);
			}
		} else {
			//Y-wise
			if(!npc.moveCharacter(0, posOrNeg)) {
				npc.moveCharacter(0, (-posOrNeg));
			}
		}
	}
	
	// Does the movement logic for patrolling NPCs who turn in random directions
	private static void patrolRandom(GCharacter npc) {
		// Get patrol movements
		int xPat = npc.getXPatrol();
		int yPat = npc.getYPatrol();
		
		// If Patrol movements aren't initialized, start moving down
		if(xPat == 0 && yPat == 0) {
			yPat = 1;
		}
		
		// Try to move in current direction
		if(!npc.moveCharacter(xPat, yPat)) {
			// Check if patrolling in X or Y direction
			if(Math.abs(xPat) > Math.abs(yPat)) {
				// X-direction
				// Choose new Y-wise direction to go in
				yPat = IdleController.setNewDirection();
				if(npc.moveCharacter(0, yPat)) {
					// If new Y-wise direction succeeds
					xPat = 0;
				} else if (npc.moveCharacter(0, -yPat)) {
					// If other Y-wise direction succeeds
					yPat = -yPat;
					xPat = 0;
				} else {
					// If neither succeed, finally try going backwards
					yPat = 0;
					xPat = -xPat;
					npc.moveCharacter(xPat, yPat);
				}
			} else {
				// Y-direction
				// Choose new X-wise direction to go in
				xPat = IdleController.setNewDirection();
				if(npc.moveCharacter(xPat, 0)) {
					// If new X-wise direction succeeds
					yPat = 0;
				} else if (npc.moveCharacter(-xPat, 0)) {
					// If other X-wise direction succeeds
					xPat = -xPat;
					yPat = 0;
				} else {
					// If neither succeed, finally try going backwards
					xPat = 0;
					yPat = -yPat;
					npc.moveCharacter(xPat, yPat);
				}
			}
		}
		
		// Set the updated values for the patrol movements
		npc.setXPatrol(xPat);
		npc.setYPatrol(yPat);
	}
	
	// Does the movement logic for patrolling NPCs who always rotate in the same direction
	private static void patrolStatic(GCharacter npc, boolean isCW) {
		// Get patrol movements
		int xPat = npc.getXPatrol();
		int yPat = npc.getYPatrol();
		
		// If Patrol movements aren't initialized, start moving down
		if(xPat == 0 && yPat == 0) {
			yPat = 1;
		}
		
		// Check if we can move in our current direction
		if(npc.moveCharacter(xPat, yPat)) {
			// We moved this turn, so we're done
			return;
		}
		
		// Check if we can rotate in our preferred direction
		Dimension rotateDirection = IdleController.getRotation(xPat, yPat, isCW);
		if(npc.moveCharacter(rotateDirection.width, rotateDirection.height)) {
			// If we rotated, we're done for the turn
			// Set new patrol direction before exiting function
			npc.setXPatrol(rotateDirection.width);
			npc.setYPatrol(rotateDirection.height);
			return;
		} else {
			// Check if we can rotate in our non-preferred direction if we're blocked on
			// our preferred direction
			if(npc.moveCharacter(-rotateDirection.width, -rotateDirection.height)) {
				// If we rotated in the opposite direction, we're done for the turn
				// Set new patrol direction before exiting function
				npc.setXPatrol(-rotateDirection.width);
				npc.setYPatrol(-rotateDirection.height);
				return;
			} else {
				// If nothing else works, then try to go backwards
				if(npc.moveCharacter(-xPat, -yPat)) {
					npc.setXPatrol(-xPat);
					npc.setYPatrol(-yPat);
					return;
				}
			}
		}

	}
	
	// Does the movement logic for NPCs following a surface
	private static void surfaceFollow(GCharacter npc, boolean isCW) {
		// Get patrol movements
		int xPat = npc.getXPatrol();
		int yPat = npc.getYPatrol();
		
		// If Patrol movements aren't initialized, start moving down
		if(xPat == 0 && yPat == 0) {
			yPat = 1;
		}
		
		// First check if we need to turn to follow our surface
		Dimension rotateDirection = IdleController.getRotation(xPat, yPat, isCW);
		if(npc.moveCharacter(rotateDirection.width, rotateDirection.height)) {
			// If we rotated, we're done for the turn
			// Set new patrol direction before exiting function
			npc.setXPatrol(rotateDirection.width);
			npc.setYPatrol(rotateDirection.height);
			return;
		}
		
		// If we can't turn, then we must still be following a wall
		// Try to move forwards
		if(npc.moveCharacter(xPat, yPat)) {
			// If we moved forward, we're done for the turn
			return;
		} else {
			// If we can't move forward or rotate in our foremost direction, then
			// try to turn in the opposite direction
			if(npc.moveCharacter(-rotateDirection.width, -rotateDirection.height)) {
				// If we rotated now, we're done for the turn
				// Set new patrol direction before exiting function
				npc.setXPatrol(-rotateDirection.width);
				npc.setYPatrol(-rotateDirection.height);
				return;
			} else {
				// If nothing else worked, go backwards from our starting direction
				if(npc.moveCharacter(-xPat, -yPat)) {
					npc.setXPatrol(-xPat);
					npc.setYPatrol(-yPat);
					return;
				}
			}
		}

	}
	
	// Gets the appropriate rotation directions
	private static Dimension getRotation(int xPat, int yPat, boolean isCW) {
		if(xPat == 1 && yPat == 0) {
			if(isCW) {
				return new Dimension(0, 1);
			} else {
				return new Dimension(0, -1);
			}
		} else if(xPat == -1 && yPat == 0) {
			if(isCW) {
				return new Dimension(0, -1);
			} else {
				return new Dimension(0, 1);
			}
		} else if(xPat == 0 && yPat == 1) {
			if(isCW) {
				return new Dimension(-1, 0);
			} else {
				return new Dimension(1, 0);
			}
		} else if(xPat == 0 && yPat == -1) {
			if(isCW) {
				return new Dimension(1, 0);
			} else {
				return new Dimension(-1, 0);
			}
		} else {
			// If none of those combinations, return (0, 0) output
			return new Dimension(0, 0);
		}
	}
	
	// Outputs a 1 or a -1 to signify new random direction
	private static int setNewDirection() {
		Random r = new Random();
		int output = 1 + (-2 * r.nextInt(2));
		return output;
	}
	
}
