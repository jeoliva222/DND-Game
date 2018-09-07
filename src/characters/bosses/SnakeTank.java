package characters.bosses;

import java.awt.Dimension;

import ai.DumbFollow;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.GCharacter;
import characters.Player;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

// Class representing the Boss enemy of the Desert area (Tank form)
public class SnakeTank extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 3464578731628352317L;

	// Modifiers/Statistics
	
	private int MAX_HP = 20;
	
	private int ARMOR_VAL = 10;
	
	private int MIN_DMG = 3;
	private int MAX_DMG = 6;
	
	private double CRIT_CHANCE = 0.1;
	private double CRIT_MULT = 1.5;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP_CHAINGUN = 3;
	private static final int STATE_ATT_CHAINGUN = 4;
	private static final int STATE_PREP_NUKE = 5;
	private static final int STATE_ATT_NUKE = 6;
	private static final int STATE_POST_NUKE = 7;
	
	//----------------------------
	
	// Additional parameters
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SNAKETANK);
	private String stImage_base = "snaketank";
	
	private String stImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.SNAKETANK, "snaketank_dead.png");

	// Constructor
	public SnakeTank(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeTank.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Der Froschmörder";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.stImage_base;
		String hpPath = "";
		String statePath = "";
		
		if(this.currentHP > 0) {
			hpPath = "_full";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch(this.state) {
			case SnakeTank.STATE_IDLE:
			case SnakeTank.STATE_PURSUE:
				// No extra path
				break;
			case SnakeTank.STATE_ALERTED:
				statePath = "_ALERT";
				break;
			case SnakeTank.STATE_PREP_CHAINGUN:
				statePath = "_PREP_SHOOT";
				break;
			case SnakeTank.STATE_ATT_CHAINGUN:
				statePath = "_ATT_SHOOT";
				break;
			case SnakeTank.STATE_PREP_NUKE:
				statePath = "_PREP_SHOOT";
				break;
			case SnakeTank.STATE_ATT_NUKE:
				statePath = "_ATT_SHOOT";
				break;
			default:
				System.out.println
					(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return this.stImage_DEAD;
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		this.attackPlayer();
	}
	
	@Override
	public void onDeath() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH.wav"));
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
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Get relative location to player
		int distX = plrX - this.xPos;
		int distY = plrY - this.yPos;
		
		switch(this.state) {
			case SnakeTank.STATE_IDLE:
				// If next to the player or damaged, become active
				if(((Math.abs(distX) <= 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) <= 1)) ||
						this.currentHP != MAX_HP) {
					SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ALERT.wav"));
					this.state = SnakeTank.STATE_ALERTED;
				} else {
					// Do nothing
				}
				break;
			case SnakeTank.STATE_ALERTED:
				// Start to chase player
				this.state = SnakeTank.STATE_PURSUE;
				break;
			case SnakeTank.STATE_PURSUE:	
				break;
			case SnakeTank.STATE_PREP_CHAINGUN:
				break;
			case SnakeTank.STATE_ATT_CHAINGUN:
				break;
			case SnakeTank.STATE_PREP_NUKE:
				break;
			case SnakeTank.STATE_ATT_NUKE:
				break;
			case SnakeTank.STATE_POST_NUKE:
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
	
}
