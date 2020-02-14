package managers;

import java.util.ArrayList;

import gui.GameScreen;
import gui.GameTile;
import projectiles.GProjectile;
import tiles.MovableType;

// Manager that holds list of currently active projectiles
public class ProjectileManager {
	
	// List of projectiles
	private ArrayList<GProjectile> projectileList = new ArrayList<GProjectile>();
	
	// List of projectiles that are pending addition to the game
	private ArrayList<GProjectile> pendingList = new ArrayList<GProjectile>();
	
	// Constructor with pre-defined projectiles
	public ProjectileManager(ArrayList<GProjectile> projectiles) {
		this.projectileList = projectiles;
	}
	
	// Blank constructor
	public ProjectileManager() {
	}
	
	// Add an effect to the manager
	public void addProjectile(GProjectile proj) {
		// If we collide with an entity, then return
		if(this.checkCollisions(proj)) {
			return;
		}
		
		this.pendingList.add(proj);
	}
	
	// Checks if a effect exists within the manager
	// Returns true if found and removed
	// Returns false if not found
	public boolean removeProjectile(GProjectile proj) {
		if(this.projectileList.contains(proj)) {
			this.projectileList.remove(proj);
			return true;
		} else {
			return false;
		}
	}
	
	// Clears all projectiles from list
	public void removeAll() {
		this.projectileList.clear();
	}
	
	// Shift all pending projectiles over to the actual in-game list
	public void movePending() {
		// Add all pending projectiles to the actual list
		for(GProjectile proj: this.pendingList) {
			// If we collide with an entity, then don't add pending
			if(this.checkCollisions(proj)) {
				// Do nothing
			} else {
				this.projectileList.add(proj);
			}
		}
		
		// Clear the pending list
		this.pendingList.clear();
	}
	
	private boolean checkCollisions(GProjectile proj) {
		// If we collide with an entity, then return
		if((!proj.hasSpawnDamaged())) {
			if(proj.checkEntityCollisions()) {
				return true;
			}
		}
		
		// Check for wall impacts on spawn
		GameTile newTile = null;
		try {
			newTile = GameScreen.getTile(proj.getXPos(), proj.getYPos());
			if((!proj.isWallPiercing()) && MovableType.isWall(newTile.getTileType().getMovableType())) {
				// Return if we spawned in a wall
				return true;
			}
		} catch (IndexOutOfBoundsException e) {
			// Return to indicate spawned OOB
			return true;
		}
		
		// Else, we did not collide
		return false;
	}
	
	//------------------------
	// Getters and setters
	
	public ArrayList<GProjectile> getProjectiles() {
		return this.projectileList;
	}

}
