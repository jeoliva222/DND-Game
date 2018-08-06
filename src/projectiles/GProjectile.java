package projectiles;

import java.util.Random;

import characters.GCharacter;
import gui.GameScreen;
import gui.GameTile;
import gui.LogScreen;
import helpers.GColors;
import managers.EntityManager;
import tiles.MovableType;

public abstract class GProjectile {
	
	// X and Y position
	protected int xPos, yPos;
	
	// Motion directions
	protected int dx, dy;
	
	// Minimum/maximum damage of the projectile
	protected int minDmg, maxDmg;
	
	// Critical chance and critical multiplier
	protected double critChance, critMult;
	
	// Name of the projectile
	protected String name;
	
	// Character who fired the projectile
	protected GCharacter owner;
	
	// Marks whether the projectile will pierce through walls and enemies
	protected boolean entityPiercing, wallPiercing;
	
	// Flag that sets the projectile for removal after it has made an impact
	protected boolean madeImpact = false;
	
	// Flag that triggers whether projectile has dealt damage while spawning already
	// Only needed for entity piercing projectiles
	protected boolean spawnDamaged = false;
	
	// Gets the character's current image
	abstract public String getImage();
	
	// Constructor
	public GProjectile(String name, int xPos, int yPos, int dx, int dy, GCharacter owner) {
		this.name = name;
		this.xPos = xPos;
		this.yPos = yPos;
		this.dx = dx;
		this.dy = dy;
		this.owner = owner;
	}
	
	// Performs a 'turn' for the projectile and sets a flag whether
	// the projectile made an impact this turn or not
	public void takeTurn() {
		this.madeImpact = this.updateProjectile();
		return;
	}
	
	// Updates the projectile's movement and checks for collisions
	private boolean updateProjectile() {	
		// If this the player is dead, don't do anything
		if(!EntityManager.getInstance().getPlayer().isAlive()) {
			// Do nothing
			return false;
		}
		
		// Check if any entities swapped positions with the projectile
		if(this.checkSwapCollisions()) {
			return true;
		}
		
		// Check for wall impacts
		GameTile newTile = null;
		try {
			newTile = GameScreen.getTile(this.xPos + dx, this.yPos + dy);
			if((!this.wallPiercing) && newTile.getTileType().getMovableType() == MovableType.WALL) {
				// Return true if we hit a wall to indicate impact
				return true;
			}
		} catch (IndexOutOfBoundsException e) {
			// Return true to indicate impact if heading out of bounds
			return true;
		}
		
		// If no initial impacts, move the projectile
		this.xPos += this.dx;
		this.yPos += this.dy;
		
		// Then check for player/npc collisions again at new position
		if(this.checkEntityCollisions()) {
			return true;
		}
		
		// If nothing special happened, return false to indicate
		// that the projectile will continue to persist
		return false;
	}
	
	// Checks for projectile collisions with the player and npcs
	public boolean checkEntityCollisions() {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Check if player has run into the projectile
		// Damage player and return true if yes
		if(this.xPos == em.getPlayer().getXPos() &&
				this.yPos == em.getPlayer().getYPos()) {
			int damage = this.calculateDamage();
			LogScreen.log(this.name+" hit player for "+Integer.toString(damage)+" damage.", GColors.DAMAGE);
			em.getPlayer().damagePlayer(damage);
			this.setSpawnDamaged(true);
			return (!this.entityPiercing);
		}
		
		// First check if any characters have run into the projectile
		for(GCharacter npc : em.getNPCManager().getCharacters()) {
			// If we impact a character, return true to indicate we've hit something
			if(this.xPos == npc.getXPos() && this.yPos == npc.getYPos()) {
				// If not the owner's enemy-type of the projectile, damage the character
				if(this.owner == null ||
						npc.getClass() != this.owner.getClass()) {
					int damage = this.calculateDamage();
					LogScreen.log(this.name+" impacted "+npc.getName()+" for "
							+Integer.toString(damage)+" damage.", GColors.ATTACK);
					npc.damageCharacter(damage);
				}
				this.setSpawnDamaged(true);
				return (!this.entityPiercing);
			}
		}
		
		// Otherwise, return false
		return false;
	}
	
	// Checks for entities swapping positions with the projectile
	public boolean checkSwapCollisions() {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Check if player moved into projectile's position
		if(this.xPos == em.getPlayer().getXPos() &&
				this.yPos == em.getPlayer().getYPos()) {
			// Check if projectile is moving to player's last position
			if((this.xPos + this.dx) == em.getPlayer().getLastX() &&
					(this.yPos + this.dy) == em.getPlayer().getLastY()) {
				// If it is, then damage the player and disappear if not piercing
				int damage = this.calculateDamage();
				LogScreen.log(this.name+" hit player for "+Integer.toString(damage)+" damage.", GColors.DAMAGE);
				em.getPlayer().damagePlayer(damage);
				return (!this.entityPiercing);
			}
		}
		
		// For each NPC, check if they've moved into the projectile's position
		for(GCharacter npc: em.getNPCManager().getCharacters()) {
			if(this.xPos == npc.getXPos() && this.yPos == npc.getYPos()) {
				// Check if the projectile swapped places with the NPC
				if((this.xPos + this.dx) == npc.getLastX() && 
						(this.yPos + this.dy) == npc.getLastY()) {
					// If not the owner's enemy-type of the projectile, damage the character
					if(this.owner == null ||
							npc.getClass() != this.owner.getClass()) {
						int damage = this.calculateDamage();
						LogScreen.log(this.name+" impacted "+npc.getName()+" for "
								+Integer.toString(damage)+" damage.", GColors.ATTACK);
						npc.damageCharacter(damage);
					}
					return (!this.entityPiercing);
				}
			}
		}
		return false;
	}
	
	// Calculate damage for the weapon with damage multiplier
	public int calculateDamage(double dmgMult) {
		Random r = new Random();
		int dmg;
		int newMin = (int) Math.floor(this.minDmg * dmgMult);
		int newMax = (int) Math.floor(this.maxDmg * dmgMult);
		
		// If the player gets lucky, they crit the enemy
		// Otherwise, calculate damage normally
		if(Math.random() < this.critChance) {
			dmg = (int) Math.floor(newMax * this.critMult);
		} else {
			dmg = r.nextInt((newMax - newMin) + 1) + newMin;
		}
		
		return dmg;
	}
	
	// Calculate damage with assumption of no damage multiplier
	public int calculateDamage() {
		return calculateDamage(1.0);
	}
	
	// Returns whether the projectile has made an impact yet
	public boolean hasImpacted() {
		return this.madeImpact;
	}
	
	// What the projectile does when it disappears
	public void onDeath() {
		// Do nothing by default
	}
	
	//-----------------------------
	// Getters and Setters
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
	public GCharacter getOwner() {
		return this.owner;
	}
	
	public boolean isWallPiercing() {
		return this.wallPiercing;
	}
	
	public boolean isEntityPiercing() {
		return this.entityPiercing;
	}
	
	public boolean hasSpawnDamaged() {
		return this.spawnDamaged;
	}
	
	public void setSpawnDamaged(boolean flag) {
		this.spawnDamaged = flag;
	}

}
