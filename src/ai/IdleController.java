package ai;

import java.util.Random;

import characters.GCharacter;

public class IdleController {

	public static void moveIdle(GCharacter npc) {
		switch(npc.getPatrolPattern()) {
			case STATIONARY: 
				// Do nothing
				return;
			case WANDER:
				// Sometimes randomly move in one of four directions
				Random r = new Random();
				// Gets a number 0 - 3: Determines if NPC should move or not
				int shouldMove = r.nextInt(4);
				
				// Most of the time, don't even try to move
				if(shouldMove != 0) {
					return;
				}
				
				// Gets 0 or 1
				int xOrY = r.nextInt(2);
				// Gets -1 or 1
				int posOrNeg = r.nextInt(2);
				posOrNeg = 1 - (2 * posOrNeg);
				
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
				break;
			case PATROL:
				// Patrol the NPC
				IdleController.patrolNPC(npc);
				break;
			case PATROL_CW:
				// Patrol the NPC CW
				IdleController.patrolCW(npc);
				break;
			case PATROL_CCW:
				// Patrol the NPC CCW
				IdleController.patrolCCW(npc);
				break;
			default: 
				// Do nothing
				break;
		}
		
	}
	
	// Does the movement logic for patrolling NPCs
	private static void patrolNPC(GCharacter npc) {
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
	
	// Does the movement logic for CW-patrolling NPCs
	private static void patrolCW(GCharacter npc) {
		// Get patrol movements
		int xPat = npc.getXPatrol();
		int yPat = npc.getYPatrol();
		
		// If Patrol movements aren't initialized, start moving down
		if(xPat == 0 && yPat == 0) {
			yPat = 1;
		}
		
		// Try to move in current direction
		if(!npc.moveCharacter(xPat, yPat)) { 
			// NPC was going right
			if(xPat == 1 && yPat == 0) {
				if(npc.moveCharacter(0, 1)) {
					xPat = 0;
					yPat = 1;
				} else if (npc.moveCharacter(0, -1)) {
					xPat = 0;
					yPat = -1;
				} else {
					xPat = -1;
					yPat = 0;
					npc.moveCharacter(xPat, yPat);
				}
			} 
			// NPC was going left
			else if(xPat == -1 && yPat == 0) {
				if(npc.moveCharacter(0, -1)) {
					xPat = 0;
					yPat = -1;
				} else if (npc.moveCharacter(0, 1)) {
					xPat = 0;
					yPat = 1;
				} else {
					xPat = 1;
					yPat = 0;
					npc.moveCharacter(xPat, yPat);
				}
			}
			// NPC was going down
			else if(xPat == 0 && yPat == 1) {
				if(npc.moveCharacter(-1, 0)) {
					xPat = -1;
					yPat = 0;
				} else if (npc.moveCharacter(1, 0)) {
					xPat = 1;
					yPat = 0;
				} else {
					xPat = 0;
					yPat = -1;
					npc.moveCharacter(xPat, yPat);
				}
			}
			// NPC was going up
			else if(xPat == 0 && yPat == -1) {
				if(npc.moveCharacter(1, 0)) {
					xPat = 1;
					yPat = 0;
				} else if (npc.moveCharacter(-1, 0)) {
					xPat = -1;
					yPat = 0;
				} else {
					xPat = 0;
					yPat = 1;
					npc.moveCharacter(xPat, yPat);
				}
			}
		}
		
		// Set the updated values for the patrol movements
		npc.setXPatrol(xPat);
		npc.setYPatrol(yPat);
	}
	
	// Does the movement logic for CCW-patrolling NPCs
	private static void patrolCCW(GCharacter npc) {
		// Get patrol movements
		int xPat = npc.getXPatrol();
		int yPat = npc.getYPatrol();
		
		// If Patrol movements aren't initialized, start moving down
		if(xPat == 0 && yPat == 0) {
			yPat = 1;
		}
		
		// Try to move in current direction
		if(!npc.moveCharacter(xPat, yPat)) { 
			// NPC was going right
			if(xPat == 1 && yPat == 0) {
				if(npc.moveCharacter(0, -1)) {
					xPat = 0;
					yPat = -1;
				} else if (npc.moveCharacter(0, 1)) {
					xPat = 0;
					yPat = 1;
				} else {
					xPat = -1;
					yPat = 0;
					npc.moveCharacter(xPat, yPat);
				}
			} 
			// NPC was going left
			else if(xPat == -1 && yPat == 0) {
				if(npc.moveCharacter(0, 1)) {
					xPat = 0;
					yPat = 1;
				} else if (npc.moveCharacter(0, -1)) {
					xPat = 0;
					yPat = -1;
				} else {
					xPat = 1;
					yPat = 0;
					npc.moveCharacter(xPat, yPat);
				}
			}
			// NPC was going down
			else if(xPat == 0 && yPat == 1) {
				if(npc.moveCharacter(1, 0)) {
					xPat = 1;
					yPat = 0;
				} else if (npc.moveCharacter(-1, 0)) {
					xPat = -1;
					yPat = 0;
				} else {
					xPat = 0;
					yPat = -1;
					npc.moveCharacter(xPat, yPat);
				}
			}
			// NPC was going up
			else if(xPat == 0 && yPat == -1) {
				if(npc.moveCharacter(-1, 0)) {
					xPat = -1;
					yPat = 0;
				} else if (npc.moveCharacter(1, 0)) {
					xPat = 1;
					yPat = 0;
				} else {
					xPat = 0;
					yPat = 1;
					npc.moveCharacter(xPat, yPat);
				}
			}
		}
		
		// Set the updated values for the patrol movements
		npc.setXPatrol(xPat);
		npc.setYPatrol(yPat);
	}
		
	private static int setNewDirection() {
		Random r = new Random();
		int output = 1 + (-2 * r.nextInt(2));
		return output;
	}
	
}
