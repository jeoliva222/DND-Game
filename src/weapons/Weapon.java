package weapons;

import java.awt.Color;
import java.util.Random;

import characters.GCharacter;
import gui.InventoryScreen;
import gui.LogScreen;
import gui.StatusScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import items.GItem;
import managers.EntityManager;

// Class that contains basic functionality for all weapons
public abstract class Weapon extends GItem {
	
	// Serialization ID
	private static final long serialVersionUID = 60739329057840072L;
	
	// Range of damage the weapon can deal
	public int minDmg, maxDmg;
	
	// Critical chance, critical damage multiplier, and charge damage multiplier
	public double critChance, critMult, chargeMult;
	
	// Whether or not the weapon is charged
	// Charge weapons by holding your position
	// Weapons do a special attack when charged
	public boolean isCharged = false;
	
	// Constructor
	public Weapon(String name, String desc, String imagePath) {
		super(name, desc, imagePath);
	}
	
	// Functionality dictating how weapons try to attack enemies
	abstract public boolean tryAttack(int dx, int dy);
	
	// Calculate damage for the weapon with damage multiplier
	public int calculateDamage(double dmgMult, GCharacter npc) {
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
		
		// Subtract armor value of target from damage
		dmg -= npc.getArmor();
		
		// Limit minimum damage at 0
		if(dmg < 0) {
			dmg = 0;
		}
		
		// Return damage value
		return dmg;
	}
	
	// Calculate damage with assumption of no damage multiplier
	public int calculateDamage(GCharacter npc) {
		return calculateDamage(1.0, npc);
	}
	
	// Charges the weapon
	public void chargeWeapon() {
		// If player is dead, don't do anything
		if(!EntityManager.getInstance().getPlayer().isAlive()) {
			return;
		}
		
		if(this.isCharged) {
			// Do nothing extra
		} else {
			// Charge the equipped weapon and log it
			//LogScreen.log("Charged weapon...");
			this.isCharged = true;
			
			// Do your offhand weapon's action
			EntityManager.getInstance().getPlayer().getSheathedWeapon().doOffhand();
		}
	}
	
	// Sets the charge of the weapon to false if it is currently charged
	public void dischargeWeapon() {
		if(this.isCharged) this.isCharged = false;
	}
	
	// Does an action when offhanded and charging
	public void doOffhand() {
		// Do nothing by default
	}
	
	public boolean use() {
		// Retrieve the currently used weapon
		Weapon oldWep = EntityManager.getInstance().getPlayer().getWeapon();
		
		// Discharge current weapon
		oldWep.dischargeWeapon();
		
		// Set player's new weapon
		EntityManager.getInstance().getPlayer().setWeapon(this);
		
		// Update status screen to show new weapon
		StatusScreen.updateWeapons();
		
		// Set inventory tile's item to be player's old weapon
		InventoryScreen.getTile(InventoryScreen.getXIndex(), InventoryScreen.getYIndex()).setItem(oldWep);
		
		// Log the resulting action
		LogScreen.log("Player swapped to "+this.name+".", GColors.ITEM);
		
		return true;
	}
	
	// Sends the attack message to the LogScreen if the object is an enemy (Not an Interactable)
	protected void sendToLog(String message, Color color, GCharacter npc) {
		if(!npc.getIfInteractable())	LogScreen.log(message, color);
	}
	
	// Plays a random swinging sound effect
	public void playSwingSound() {
		Random r = new Random();
		int whichSound = r.nextInt(3);
		if(whichSound == 0) {
			SoundPlayer.playWAV(GPath.createSoundPath("player_SWING.wav"));
		} else if (whichSound == 1) {
			SoundPlayer.playWAV(GPath.createSoundPath("player_SWING2.wav"));
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("player_SWING3.wav"));
		}
	}

}
