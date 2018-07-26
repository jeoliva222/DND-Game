package managers;

import java.util.ArrayList;

import characters.Corpse;
import characters.GCharacter;
import characters.Player;
import effects.GEffect;
import gui.GameScreen;
import gui.GameTile;
import items.GPickup;
import levels.MapArea;
import projectiles.GProjectile;

public class EntityManager {

	// The player the user controls
	private static Player player;
	
	// The list of dead characters
	private static CorpseManager corpseManager;
	
	// List of currently alive NPCs/enemies
	private static NPCManager npcManager;
	
	// List of active visual effects
	private static EffectManager fxManager;
	
	// List of active projectiles
	private static ProjectileManager projManager;
	
	// List of active pickups
	private static PickupManager pickupManager;
	
	// Active area that is loaded in
	private static MapArea activeArea;
	
	///TODO Temporary code
	// Blank Constructor for the EntityManager
	public EntityManager() {
		EntityManager.player = new Player();
		EntityManager.corpseManager = new CorpseManager();
		EntityManager.npcManager = new NPCManager();
		EntityManager.fxManager = new EffectManager();
		EntityManager.projManager = new ProjectileManager();
		EntityManager.pickupManager = new PickupManager();
	}
	
	// Constructor created with pre-existing entities
	public EntityManager(Player player, ArrayList<Corpse> corpses, ArrayList<GCharacter> npcs,
			ArrayList<GProjectile> projectiles, ArrayList<GPickup> pickups) {
		EntityManager.player = player;
		EntityManager.corpseManager = new CorpseManager(corpses);
		EntityManager.npcManager = new NPCManager(npcs);
		EntityManager.fxManager = new EffectManager();
		EntityManager.projManager = new ProjectileManager(projectiles);
		EntityManager.pickupManager = new PickupManager(pickups);
	}
	
	// Adds everything that was pending addition to the game
	public void addAllPending() {
		EntityManager.npcManager.movePending();
		EntityManager.projManager.movePending();
	}
	
	// Cleans up effects that have lasted their duration
	public void effectCleaner() {
		// Add all effects that have persisted long enough to the discard pile
		ArrayList<GEffect> hearse = new ArrayList<GEffect>();
		for(GEffect fx: EntityManager.fxManager.getEffects()) {
			if(fx.persist()) {
				hearse.add(fx);
			}
		}
		
		// Then remove all discarded effects
		for(GEffect fx: hearse) {
			// Find the tile and clear the visual of the effect
			GameTile updateTile;
			try {
				updateTile = GameScreen.getTile(fx.getXPos(), fx.getYPos());
				updateTile.clearEffect(fx);
				updateTile.repaint();
			} catch (IndexOutOfBoundsException e) {
				// Do nothing
			}
			// Remove the effect from the manager
			EntityManager.fxManager.removeEffect(fx);
		}
	}
	
	// Transfers dead characters from NPCManager to CorpseManager as corpses
	public void corpseCleaner() {
		// First add the dead characters to a reference list
		ArrayList<GCharacter> hearse = new ArrayList<GCharacter>();
		for(GCharacter gchar: EntityManager.npcManager.getCharacters()) {
			if(!gchar.isAlive()) {
				EntityManager.corpseManager.addCorpse(new Corpse(gchar));
				hearse.add(gchar);
				
				// Also make sure to refresh the tile to clear the dead entity
				///**** NOT SURE WHY THIS EXISTS?
				//GameScreen.getTile(gchar.getXPos(), gchar.getYPos()).clearFG();
			}
		}
		
		// Do their on death functionality, then remove them from the EntityManager
		for(GCharacter gchar: hearse) {
			// Find the tile and clear the visual of the character
			GameTile updateTile;
			try {
				updateTile = GameScreen.getTile(gchar.getXPos(), gchar.getYPos());
				updateTile.clearFG();
			} catch (IndexOutOfBoundsException e) {
				// Do nothing
			}
			// Do the on death functionality and remove from reference list
			gchar.onDeath();
			EntityManager.npcManager.removeCharacter(gchar);
		}
	}
	
	// Removes projectiles that have made an impact from the reference list
	///**** STILL HAS TO-DO STUFF
	public void projectileCleaner() {
		// Remove projectiles that have made an impact with something
		ArrayList<GProjectile> hearse = new ArrayList<GProjectile>();
		for(GProjectile proj: EntityManager.projManager.getProjectiles()) {
			if(proj.hasImpacted()) {
				// Add to pending-removal list
				hearse.add(proj);
			}
		}
		
		// Do their on death functionality, then remove them from the EntityManager
		for(GProjectile proj: hearse) {
			// Find the tile and clear the visual of the character
			GameTile updateTile;
			try {
				updateTile = GameScreen.getTile(proj.getXPos(), proj.getYPos());
				updateTile.clearProj(proj);
			} catch (IndexOutOfBoundsException e) {
				// Do nothing
			}
			// Do the on death functionality and remove from reference list
			///******* ADD ON DEATH FUNCTION
			//proj.onDeath();
			EntityManager.projManager.removeProjectile(proj);
		}
		
		///*** MOVED TO addAllPending Function
		// Finally, add pending projectiles into the game
		//EntityManager.projManager.movePending();
	}
	
	// Clears all the entities from the managers (minus the player)
	public static void removeEverything() {
		EntityManager.getCorpseManager().removeAll();
		EntityManager.getEffectManager().removeAll();
		EntityManager.getNPCManager().removeAll();
		EntityManager.getProjectileManager().removeAll();
		EntityManager.getPickupManager().removeAll();
	}
	
	// Does all the different management functions
	public void manageAll() {
		this.addAllPending();
		this.corpseCleaner();
		this.effectCleaner();
		this.projectileCleaner();
	}
	
	
	//-----------------------
	// Getters and Setters
	
	public static Player getPlayer() {
		return EntityManager.player;
	}
	
	public static void setPlayer(Player newPlayer) {
		EntityManager.player = null;
		EntityManager.player = newPlayer;
	}
	
	public static CorpseManager getCorpseManager() {
		return EntityManager.corpseManager;
	}
	
	public static NPCManager getNPCManager() {
		return EntityManager.npcManager;
	}
	
	public static EffectManager getEffectManager() {
		return EntityManager.fxManager;
	}
	
	public static ProjectileManager getProjectileManager() {
		return EntityManager.projManager;
	}
	
	public static PickupManager getPickupManager() {
		return EntityManager.pickupManager;
	}
	
	public static MapArea getActiveArea() {
		return EntityManager.activeArea;
	}
	
	public static void setActiveArea(MapArea newArea) {
		EntityManager.activeArea = newArea;
	}
	
}
