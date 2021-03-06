package weapons.special;

import java.util.ArrayList;

import characters.GCharacter;
import characters.allies.Player;
import effects.BulletEffect;
import effects.ChargeIndicator;
import gui.GameScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;
import weapons.Weapon;

/**
 * Class representing the special weapon: 'Venom'
 * @author jeoliva
 */
public class VenomGun extends Weapon {
	
	// Serialization ID
	private static final long serialVersionUID = -4686925679964499893L;

	// Maximum bullets fired at once
	private final byte bulletsMax = 4;
	private final byte bulletsMin = 1;
	
	// Number of bullets to fire
	private int bulletsToShoot = bulletsMin;
	
	// Constructor
	public VenomGun() {
		super("Venom",
				"WEAPON (Special): Charge to toggle firing stance. Charge attack to unleash a hail of bullets. Consecutive hits = More bullets!",
				GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "venomGun.png"));
		
		// Set damage attributes
		this.minDmg = 1;
		this.maxDmg = 2;
		this.critChance = 0.05;
		this.critMult = 1.5;
		this.chargeMult = 1.0;
		this.attackExhaust = 0;
		this.chargeExhaust = 1;
	}
	
	@Override
	public boolean tryAttack(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		Player player = em.getPlayer();
		
		// Check if weapon is charged or not
		if (isCharged && player.checkEnergy(chargeExhaust)) { // CHARGED ------------------------------------------
			// Find all enemies that are on player's shot path
			ArrayList<GCharacter> inlineEnemies = new ArrayList<GCharacter>();
			
			// Coordinates of wall / End-of-screen
			byte wallX = -1;
			byte wallY = -1;
			
			// While we still have a open bullet path, check for NPCs to hit
			byte nextX = (byte) (player.getXPos() + dx);
			byte nextY = (byte) (player.getYPos() + dy);
			boolean isEndHit = false;
			while (!isEndHit) { // Begin While --------------------------------------
				// First check for NPCs to add
				for (GCharacter npc : em.getNPCManager().getCharacters()) {
					if (nextX == npc.getXPos() && nextY == npc.getYPos()) {
						inlineEnemies.add(npc);
					}
				}
				
				// Then check for walls or OOB
				try {
					isEndHit = MovableType.isWall(GameScreen.getTile(nextX, nextY).getTileType().getMovableType());
				} catch (ArrayIndexOutOfBoundsException e) {
					isEndHit = true;
				}
				
				if (isEndHit) {
					wallX = nextX;
					wallY = nextY;
				}
				
				// Then update the next coordinates to check
				nextX += dx;
				nextY += dy;
				
			} // END While Loop ---------------------------------------------
			
			// Play firing sound (louder the more bullets are fired)
			SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Fire.wav"), (-15f + (3f * bulletsToShoot)));
			
			// Then check for closest NPC in the line of enemies
			if (inlineEnemies.isEmpty()) {
				// If no shot hit, return true anyways to stay in readied stance but lower next bullet count
				
				// Mark tiles with effects until our first wall
				byte safetyCounter = 0;
				nextX = (byte) (player.getXPos() + dx);
				nextY = (byte) (player.getYPos() + dy);
				while (!(nextX == wallX && nextY == wallY) && (safetyCounter < 10)) {
					// Add bullet effect
					em.getEffectManager().addEffect(new BulletEffect(nextX, nextY, dx, dy));
					
					// Increment tracker coordinates
					nextX += dx;
					nextY += dy;
					safetyCounter += 1;
				}
				
				// Mark last tile with special impact effect
				em.getEffectManager().addEffect(new ChargeIndicator(nextX, nextY));
				
				// Decrease bullets fired with next attack (Capped at bulletsMin)
				if (bulletsToShoot > bulletsMin) {
					this.bulletsToShoot += -1;
				}
				
				// Exhaust the player more than normal
				player.exhaustPlayer(chargeExhaust * 3);
				
				// Return true to keep in stance
				return true;
			} else {
				// Sort in-line enemies by distance closest to player
				inlineEnemies = sortNPCs(inlineEnemies);
				
				// Initialize loop variables
				int[] damageNums = new int[bulletsToShoot];
				boolean isMultihit = false;
				int currentTarget = 0;
				String logString = "";
				
				// Coordinates for furtherest NPC we hit
				byte farNPCX = -1;
				byte farNPCY = -1;
				
				// Damage the closest NPC for each bullet fired
				for (int shotCount = 0; shotCount < bulletsToShoot; shotCount++) {
					
					// Keep track of coordinates
					farNPCX = (byte) inlineEnemies.get(currentTarget).getXPos();
					farNPCY = (byte) inlineEnemies.get(currentTarget).getYPos();
					
					// Calculate the bullet damage, then damage the closest NPC in our line of fire
					int dmg = calculateDamage(chargeMult, inlineEnemies.get(currentTarget));
					boolean isNpcAlive = inlineEnemies.get(currentTarget).damageCharacter(dmg);
					
					// Keep record of the damage done
					damageNums[shotCount] = dmg;
					
					// If our NPC died, log the current message and switch targets
					if (!isNpcAlive) { // DEAD --------------------------------------------
						// Finish current log message
						if (!isMultihit) {
							logString = logString + damageNums[shotCount];
						} else {
							logString = logString + ", and then " + damageNums[shotCount];
						}
						
						// Send out current log
						sendToLog("Player fired upon " + inlineEnemies.get(currentTarget).getName() + " and dealt "
								+ logString + " damage.", GColors.ATTACK, inlineEnemies.get(currentTarget));
						
						
						// Start building new log message
						logString = "";
						
						// Switch target, or stop firing if no more targets
						currentTarget += 1;
						if (currentTarget >= inlineEnemies.size()) {
							// No more targets left to hit, so break from the 'for' loop
							break;
						}
						
						// Reset multi-hit flag
						isMultihit = false;
						
					} else { // NOT DEAD ----------------------------------------------
						if (!isMultihit) {
							logString = logString + damageNums[shotCount];
						} else if (isMultihit && shotCount == (bulletsToShoot - 1)) {
							logString = logString + ", and then " + damageNums[shotCount];
						} else {
							logString = logString + ", " + damageNums[shotCount];
						}
						
						// Indicate we've hit current target with at least one bullet
						isMultihit = true;
					}
				}
				
				// Mark tiles with effects until our farthest target or first wall
				byte safetyCounter = 0;
				nextX = (byte) (player.getXPos() + dx);
				nextY = (byte) (player.getYPos() + dy);
				while (!(nextX == wallX && nextY == wallY) && !(nextX == farNPCX && nextY == farNPCY) && (safetyCounter < 10)) {
					// Add bullet effect
					em.getEffectManager().addEffect(new BulletEffect(nextX, nextY, dx, dy));
					
					// Increment tracker coordinates
					nextX += dx;
					nextY += dy;
					safetyCounter += 1;
				}
				
				// Mark last tile with special impact effect
				em.getEffectManager().addEffect(new ChargeIndicator(nextX, nextY));
				
				// Send remaining log message detailing damage done to enemy (If not blank)
				if (!logString.isEmpty()) {
					sendToLog("Player fired upon " + inlineEnemies.get(currentTarget).getName() + " and dealt "
							+ logString + " damage.", GColors.ATTACK, inlineEnemies.get(currentTarget));
				}
				
				// Increase bullets fired with next attack (Capped at bulletsMax)
				if (bulletsToShoot < bulletsMax) {
					this.bulletsToShoot += 1;
				}
				
				// Exhaust the player the normal amount
				player.exhaustPlayer(chargeExhaust);
				
				// Return true to indicate we hit a target
				return true;
			}
		} else { // NOT CHARGED ------------------------------------------------------------------
			// If not charged, check for immediately adjacent NPCs to punch
			for (GCharacter npc : em.getNPCManager().getCharacters()) {
				if ((player.getXPos() + dx) == npc.getXPos() && (player.getYPos() + dy) == npc.getYPos()) {
					// If not charged deal normal damage and attack normally
					int dmg = calculateDamage(npc);
					npc.damageCharacter(dmg);
					sendToLog("Player butted and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
					
					// Play hit sound and return true to indicate we successfully hit a target
					playSwingSound();
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
		if (!EntityManager.getInstance().getPlayer().isAlive()) {
			return;
		}
		
		if (isCharged) {
			// Do nothing
		} else {
			// Charge the equipped weapon
			this.isCharged = true;
			
			// Play reving sound
			SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Rev.wav"), -10f);
		}
	}
	
	// Override that also resets the number of bullets to shoot
	@Override
	public void resetWeapon() {
		super.resetWeapon();
		this.bulletsToShoot = bulletsMin;
	}
	
	// Sorts NPCs by distance closest to player
	private static ArrayList<GCharacter> sortNPCs(ArrayList<GCharacter> input) {
		// First check to see if list is one item long or smaller
		if (input.size() <= 1) {
			// If this is the case, it is already sorted
			return input;
		}
		
		// Get reference to EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Bubble sort by closest distance
		for (int i = 0; i < input.size(); i++) {
			for (int j = 0; j < (input.size() - (i + 1)); j++) {
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
				if (dist1 > dist2) {
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
