package weapons.special;

import java.util.ArrayList;

import characters.GCharacter;
import gui.GameScreen;
import helpers.GColors;
import helpers.GPath;
import managers.EntityManager;
import tiles.MovableType;
import weapons.Weapon;

public class VenomGun extends Weapon {
	
	// Serialization ID
	private static final long serialVersionUID = -4686925679964499893L;


	// Maximum bullets fired at once
	private final int bulletsMax = 4;
	private final int bulletsMin = 1;
	
	// Number of bullets to fire
	private int bulletsToShoot = this.bulletsMin;
	
	// Constructor
	public VenomGun() {
		super("Venom",
				"WEAPON (Special): Charge to toggle firing stance. Charge attack to unleash a hail of bullets. Consecutive hits = More bullets!",
				GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png"));
		
		// Set damage attributes
		this.minDmg = 1;
		this.maxDmg = 2;
		this.critChance = 0.05;
		this.critMult = 1.5;
		this.chargeMult = 1.0;
	}
	
	@Override
	public boolean tryAttack(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Check if weapon is charged or not
		if(this.isCharged) { // CHARGED ------------------------------------------
			// Grab potential targets that are in line with the character (X-wise or Y-wise)
			ArrayList<GCharacter> potentialTargets = new ArrayList<GCharacter>();
			for(GCharacter npc : em.getNPCManager().getCharacters()) {
				if((em.getPlayer().getXPos()) == npc.getXPos()
					|| (em.getPlayer().getYPos()) == npc.getYPos()) {
					potentialTargets.add(npc);
				}
			}
			
			// Find all enemies that are on player's shot path
			ArrayList<GCharacter> inlineEnemies = new ArrayList<GCharacter>();
			
			// While we still have a open bullet path, check for NPCs to hit
			int nextX = (em.getPlayer().getXPos() + dx);
			int nextY = (em.getPlayer().getYPos() + dy);
			boolean isEndHit = false;
			while(!isEndHit) { // Begin While --------------------------------------
				// First check for NPCs to add
				for(GCharacter npc : em.getNPCManager().getCharacters()) {
					if(nextX == npc.getXPos() && nextY == npc.getYPos()) {
						inlineEnemies.add(npc);
					}
				}
				
				// Mark tile with damage effect TODO
				//
				
				// Then check for walls or OOB
				try {
					isEndHit = GameScreen
							.getTile(nextX, nextY)
							.getTileType()
							.getMovableType() == MovableType.WALL;
				} catch (ArrayIndexOutOfBoundsException e) {
					isEndHit = true;
				}
				
				// Then update the next coordinates to check
				nextX += dx;
				nextY += dy;
				
			} // END While Loop ---------------------------------------------
			
			// Then check for closest NPC in the line of enemies
			if(inlineEnemies.isEmpty()) {
				// If no shot hit, return true anyways to stay in readied stance but lower next bullet count
				
				// Decrease bullets fired with next attack (Capped at bulletsMin)
				if(this.bulletsToShoot > this.bulletsMin) this.bulletsToShoot += -1;
				
				// Return true to keep in stance
				return true;
			} else {
				// Sort in-line enemies by distance closest to player
				inlineEnemies = this.sortNPCs(inlineEnemies);
				
				// Initialize loop variables
				int[] damageNums = new int[this.bulletsToShoot];
				boolean isMultihit = false;
				int currentTarget = 0;
				String logString = "";
				
				// Damage the closest NPC for each bullet fired
				for(int shotCount = 0; shotCount < this.bulletsToShoot; shotCount++) {
					// Calculate the bullet damage, then damage the closest NPC in our line of fire
					int dmg = this.calculateDamage(this.chargeMult, inlineEnemies.get(currentTarget));
					boolean didDie = inlineEnemies.get(currentTarget).damageCharacter(dmg);
					
					// Keep record of the damage done
					damageNums[shotCount] = dmg;
					
					// If our NPC died, log the current message and switch targets
					if(!didDie) { // DEAD --------------------------------------------
						// Finish current log message
						if(!isMultihit) {
							logString = logString + damageNums[shotCount];
						} else {
							logString = logString + ", and then " + damageNums[shotCount];
						}
						
						// Send out current log
						this.sendToLog("Player fired upon " + inlineEnemies.get(currentTarget).getName() + " and dealt "
									+ logString + " damage.", GColors.ATTACK, inlineEnemies.get(currentTarget));
						
						// Switch target, or stop firing if no more targets
						currentTarget += 1;
						if(currentTarget >= inlineEnemies.size()) {
							// Increase bullets fired with next attack (Capped at bulletsMax)
							if(this.bulletsToShoot < this.bulletsMax) this.bulletsToShoot += 1;
							
							// Return true to indicate we hit targets
							return true;
						}
						
						
						// Start building new log message
						logString = "";
						
						// Reset multihit flag
						isMultihit = false;
					} else { // NOT DEAD ----------------------------------------------
						if(!isMultihit) {
							logString = logString + damageNums[shotCount];
						} else if (isMultihit && shotCount == (this.bulletsToShoot - 1)) {
							logString = logString + ", and then " + damageNums[shotCount];
						} else {
							logString = logString + ", " + damageNums[shotCount];
						}
						
						// Indicate we've hit current target with at least one bullet
						isMultihit = true;
					}
				}
				
				// Send remaining log message detailing damage done to enemy (If not blank)
				if(!logString.equals("")) {
					this.sendToLog("Player fired upon " + inlineEnemies.get(currentTarget).getName() + " and dealt "
					+ logString + " damage.", GColors.ATTACK, inlineEnemies.get(currentTarget));
				}
				
//				// Add on-hit effect TODO REMOVE THIS
//				em.getEffectManager()
//					.addEffect(new ChargeIndicator(inlineEnemies.get(currentTarget).getXPos(), inlineEnemies.get(currentTarget).getYPos()));
				
				// Increase bullets fired with next attack (Capped at bulletsMax)
				if(this.bulletsToShoot < this.bulletsMax) this.bulletsToShoot += 1;
				
				// Return true to indicate we hit a target
				return true;
			}
			
		} else { // NOT CHARGED ------------------------------------------------------------------
			// If not charged, check for immediately adjacent NPCs to punch
			for(GCharacter npc : em.getNPCManager().getCharacters()) {
				if((em.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// If not charged deal normal damage and attack normally
					int dmg = this.calculateDamage(npc);
					npc.damageCharacter(dmg);
					this.sendToLog("Player butted and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
					
					// Play hit sound and return true to indicate we successfully hit a target
					this.playSwingSound();
					return true;
				}
			}
		}
		
		// Otherwise, return false to indicate we didn't hit anything
		return false;
	}
	
	// Override that toggles the charge on the weapon
	@Override
	public void chargeWeapon() {
		// If player is dead, don't do anything
		if(!EntityManager.getInstance().getPlayer().isAlive()) {
			return;
		}
		
		if(this.isCharged) {
			// Discharge weapon
			this.dischargeWeapon();
		} else {
			// Charge the equipped weapon and log it
			//LogScreen.log("Charged weapon...");
			this.isCharged = true;
			
			// Do your offhand weapon's action
			EntityManager.getInstance().getPlayer().getSheathedWeapon().doOffhand();
		}
	}

	@Override
	public void doOffhand() {
		// Prevent weapon from being charged while offhand
		this.dischargeWeapon();
	}
	
	// Override that also resets the number of bullets to shoot
	@Override
	public void dischargeWeapon() {
		super.dischargeWeapon();
		this.bulletsToShoot = this.bulletsMin;
	}
	
	// Sorts NPCs by distance closest to player
	private ArrayList<GCharacter> sortNPCs(ArrayList<GCharacter> input) {
		// First check to see if list is one item long or smaller
		if(input.size() <= 1) {
			// If this is the case, it is already sorted
			return input;
		}
		
		// Get reference to EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Bubble sort by closest distance
		for(int i = 0; i < input.size(); i++) {
			for(int j = 0; j < (input.size() - (i + 1)); j++) {
				// Get NPCs to compare
				GCharacter npc1 = input.get(j);
				GCharacter npc2 = input.get(j + 1);
				
				// Get relative distance of first NPC
				int dist1 = Math.abs(em.getPlayer().getXPos() - npc1.getXPos());
				dist1 += Math.abs(em.getPlayer().getYPos() - npc1.getYPos());
				
				// Get relative distance of second NPC
				int dist2 = Math.abs(em.getPlayer().getXPos() - npc2.getXPos());
				dist2 += Math.abs(em.getPlayer().getYPos() - npc2.getYPos());
				
				// Swap spots if NPC1 is closer to player
				if(dist1 > dist2) {
					GCharacter temp = npc2;
					npc2 = npc1;
					npc1 = temp;
					input.set(j, npc1);
					input.set(j + 1, npc2);
				}
			}
		} // END BUBBLE SORT ----------------------
		
		// Return the newly sorted array
		return input;
	}
	
}
