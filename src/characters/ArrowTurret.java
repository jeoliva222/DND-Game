package characters;

import ai.PatrolPattern;
import buffs.Buff;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.Arrow;

/**
 * Class that represents Arrow Turret obstacle
 * @author jeoliva
 */
public class ArrowTurret extends GCharacter {
	
	// Serialization ID
	private static final long serialVersionUID = 5808942277477379771L;
	
	// Modifiers/Statistics

	private static int MAX_HP = 100;
	
	private static int MIN_DMG = 3;
	private static int MAX_DMG = 3;
	
	private static double CRIT_CHANCE = 0.05;
	private static double CRIT_MULT = 1.7;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_ACTIVE = 0;
	private static final int STATE_INACTIVE = 1;
	
	//----------------------------
	
	// Additional Behavior
	
	// Direction that the turret will continually fire
	protected int shootX = 0;
	protected int shootY = 0;
	
	// Counter of turns in between shots
	protected int shotCount;
	protected int shotMax;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.ARROW_TURRET);
	private static String atImage_base = "arrowturret";

	// Constructor
	public ArrowTurret(int startX, int startY, int shootX, int shootY, int interval, int offset) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		// Can't be damaged
		this.armor = 100;
		
		if (shootX == 0 && shootY == 0) {
			this.shootX = 0;
			this.shootY = 1;
		} else {
			this.shootX = shootX;
			this.shootY = shootY;
		}
		
		this.shotMax = interval;
		this.shotCount = offset;
		
		this.state = ArrowTurret.STATE_ACTIVE;
		this.patrolPattern = PatrolPattern.STATIONARY;
	}
	
	// Constructor
	public ArrowTurret(int startX, int startY, int shootX, int shootY, int interval) {
		this(startX, startY, shootX, shootY, interval, 0);
	}
	
	@Override
	public String getName() {
		return "Arrow Turret";
	}

	@Override
	public String getImage() {
		switch (state) {
			case ArrowTurret.STATE_INACTIVE:
				return (imageDir + atImage_base + "_INACTIVE.png");
			case ArrowTurret.STATE_ACTIVE:
				if ((shotCount - 1) >= shotMax) {
					return (imageDir + atImage_base + "_ATT.png");
				} else if (shotCount >= shotMax) {
					return (imageDir + atImage_base + "_PREP.png");
				} else {
					return (imageDir + atImage_base + "_INACTIVE.png");
				}
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
	}

	@Override
	public String getCorpseImage() {
		return GPath.NULL;
	}

	@Override
	public void populateMoveTypes() {
		// No move types
	}
	
	@Override
	public void playerInitiate() {
		// Never initiates player
	}

	@Override
	public void takeTurn() {
		// If this is dead or the player is dead, don't do anything
		if (!isAlive() || !EntityManager.getInstance().getPlayer().isAlive()) {
			// Do nothing
			return;
		}
		
		// Depending on current state, act accordingly
		switch (state) {
			case ArrowTurret.STATE_INACTIVE:
				// Do nothing because it's inactive
				break;
			case ArrowTurret.STATE_ACTIVE:
				// Reset our counter
				if (shotCount > shotMax) {
					this.shotCount = 0;
				}
				
				// We're active, so shoot arrows periodically
				if (shotCount < shotMax) {
					// Do nothing, we're on cooldown
				} else {
					// Play arrow firing sound
					SoundPlayer.playWAV(GPath.createSoundPath("arrow_SHOT.wav"), getXPos(), getYPos());
					
					// Fire an arrow in a predetermined direction
					EntityManager.getInstance().getProjectileManager()
						.addProjectile(new Arrow((xPos + shootX),
												(yPos + shootY),
												shootX,
												shootY, getClass()));
				}
				
				// Increment our cooldown counter
				this.shotCount += 1;
				break;
			default:
				System.out.println(getName() + " couldn't identify its state! State = " + Integer.toString(state));
				break;
		}
	}

	@Override
	public void onDeath() {
		// Doesn't die, so nothing
	}
	
	@Override
	public void addBuff(Buff debuff) {
		// Don't add the buff
		return;
	}
	
	// Makes the turret stop shooting
	public void setInactive() {
		this.state = ArrowTurret.STATE_INACTIVE;
	}

}
