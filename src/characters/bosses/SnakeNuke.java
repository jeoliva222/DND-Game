package characters.bosses;

import java.util.Random;

import ai.PatrolPattern;
import characters.GCharacter;
import characters.Player;
import effects.DamageIndicator;
import gui.GameScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

public class SnakeNuke extends GCharacter {

	// Modifiers/Statistics
	
	private int MAX_HP = 10;
	
	private int ARMOR_VAL = 100;
	
	private int MIN_DMG = 10;
	private int MAX_DMG = 10;
	
	private double CRIT_CHANCE = 0.0;
	private double CRIT_MULT = 1.0;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_HIDDEN = 0;
	private static final int STATE_FLIGHT = 1;
	private static final int STATE_BOOM = 2;
	
	//----------------------------
	
	// Additional parameters
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SNAKETANK);
	private String nkImage_base = "nuke";
	
	// Constructor
	public SnakeNuke(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeNuke.STATE_HIDDEN;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.canFocus = false;
		
		this.imagePath = this.getImage();
	}

	public String getName() {
		return "Reflectable Nuke";
	}
	
	@Override
	public String getImage() {
		
		// TEMP TODO
		//return this.imageDir + this.nkImage_base + "_full.png";
		return GPath.NULL;
		
//		String imgPath = this.imageDir + this.stImage_base;
//		String hpPath = "";
//		String statePath = "";
//		if(this.currentHP > 0) {
//			hpPath = "_full";
//		} else {
//			hpPath = "_dead";
//			return (imgPath + hpPath + ".png");
//		}
		
//		switch(this.state) {
//			case SnakeTank.STATE_IDLE:
//			case SnakeTank.STATE_PURSUE:
//				// No extra path
//				break;
//			case SnakeTank.STATE_ALERTED:
//				statePath = "_ALERT";
//				break;
//			case SnakeTank.STATE_PREP_CHAINGUN:
//				statePath = "_PREP_SHOOT";
//				break;
//			case SnakeTank.STATE_ATT_CHAINGUN:
//				statePath = "_ATT_SHOOT";
//				break;
//			case SnakeTank.STATE_PREP_NUKE:
//				statePath = "_PREP_SHOOT";
//				break;
//			case SnakeTank.STATE_ATT_NUKE:
//				statePath = "_ATT_SHOOT";
//				break;
//			default:
//				System.out.println
//					(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
//				return GPath.NULL;
//		}
//		
//		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return GPath.NULL;
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
		this.moveTypes.add(MovableType.AIR);
		this.moveTypes.add(MovableType.WATER);
		this.moveTypes.add(MovableType.ACID);
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		this.attackPlayer();
	}
	
	@Override
	public void onDeath() {
		// Do nothing
	}

	@Override
	public void takeTurn() {
		// Get reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if(!this.isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		switch(this.state) {
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
	
	
}
