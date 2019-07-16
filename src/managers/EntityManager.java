package managers;

import java.util.ArrayList;

import characters.Corpse;
import characters.GCharacter;
import characters.allies.Player;
import effects.GEffect;
import gui.GameScreen;
import gui.GameTile;
import items.GPickup;
import levels.MapArea;
import levels.MapLevel;
import projectiles.GProjectile;

public class EntityManager {

	// Singleton instance of the EntityManager
	private static EntityManager INSTANCE;
	
	// The player the user controls
	private Player player;
	
	// The list of dead characters
	private CorpseManager corpseManager;
	
	// List of currently alive NPCs/enemies
	private NPCManager npcManager;
	
	// List of active visual effects
	private EffectManager fxManager;
	
	// List of active projectiles
	private ProjectileManager projManager;
	
	// List of active pickups
	private PickupManager pickupManager;
	
	// Active area that is loaded in
	private MapArea activeArea;
	
	// Blank Constructor for the EntityManager
	private EntityManager() {
		this.player = new Player();
		this.corpseManager = new CorpseManager();
		this.npcManager = new NPCManager();
		this.fxManager = new EffectManager();
		this.projManager = new ProjectileManager();
		this.pickupManager = new PickupManager();
	}
	
	// Constructor created with pre-existing entities
	private EntityManager(Player player, ArrayList<Corpse> corpses, ArrayList<GCharacter> npcs,
			ArrayList<GProjectile> projectiles, ArrayList<GPickup> pickups) {
		this.player = player;
		this.corpseManager = new CorpseManager(corpses);
		this.npcManager = new NPCManager(npcs);
		this.fxManager = new EffectManager();
		this.projManager = new ProjectileManager(projectiles);
		this.pickupManager = new PickupManager(pickups);
	}
	
	// Adds everything that was pending addition to the game
	public void addAllPending() {
		this.npcManager.movePending();
		this.projManager.movePending();
	}
	
	// Cleans up effects that have lasted their duration
	public void effectCleaner() {
		// Add all effects that have persisted long enough to the discard pile
		ArrayList<GEffect> hearse = new ArrayList<GEffect>();
		for(GEffect fx: this.fxManager.getEffects()) {
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
			this.fxManager.removeEffect(fx);
		}
	}
	
	// Transfers dead characters from NPCManager to CorpseManager as corpses
	public void corpseCleaner() {
		// First add the dead characters to a reference list
		ArrayList<GCharacter> hearse = new ArrayList<GCharacter>();
		for(GCharacter gchar: this.npcManager.getCharacters()) {
			if(!gchar.isAlive()) {
				this.corpseManager.addCorpse(new Corpse(gchar));
				hearse.add(gchar);
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
			this.npcManager.removeCharacter(gchar);
		}
	}
	
	// Removes projectiles that have made an impact from the reference list
	public void projectileCleaner() {
		// Remove projectiles that have made an impact with something
		ArrayList<GProjectile> hearse = new ArrayList<GProjectile>();
		for(GProjectile proj: this.projManager.getProjectiles()) {
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
			proj.onDeath();
			this.projManager.removeProjectile(proj);
		}
	}
	
	// Clears all the entities from the managers (minus the player)
	public void removeEverything() {
		this.getCorpseManager().removeAll();
		this.getEffectManager().removeAll();
		this.getNPCManager().removeAll();
		this.getProjectileManager().removeAll();
		this.getPickupManager().removeAll();
	}
	
	// Does all the different management functions
	public void manageAll() {
		this.addAllPending();
		this.corpseCleaner();
		this.effectCleaner();
		this.projectileCleaner();
	}
	
	// Returns whether the current level or area is dark
	public boolean isDark() {
		return (this.activeArea.showDark() ||
				this.getCurrentLevel().showDark());
	}
	
	//-----------------------
	// Getters and Setters
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void setPlayer(Player newPlayer) {
		this.player = null;
		this.player = newPlayer;
	}
	
	public CorpseManager getCorpseManager() {
		return this.corpseManager;
	}
	
	public NPCManager getNPCManager() {
		return this.npcManager;
	}
	
	public EffectManager getEffectManager() {
		return this.fxManager;
	}
	
	public ProjectileManager getProjectileManager() {
		return this.projManager;
	}
	
	public PickupManager getPickupManager() {
		return this.pickupManager;
	}
	
	public MapLevel getCurrentLevel() {
		return this.activeArea.getLevel(this.player.getLevelX(), this.player.getLevelY());
	}
	
	public MapArea getActiveArea() {
		return this.activeArea;
	}
	
	public void setActiveArea(MapArea newArea) {
		this.activeArea = newArea;
	}
	
	// Singleton Instance retriever
	public static EntityManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EntityManager();
		}
		
		return INSTANCE;
	}
	
}
