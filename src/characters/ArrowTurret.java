package characters;

import ai.PatrolPattern;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.Arrow;

public class ArrowTurret extends GCharacter {
	
	// Serialization ID
	private static final long serialVersionUID = 5808942277477379771L;
	
	// Modifiers/Statistics

	private int MAX_HP = 100;
	
	private int MIN_DMG = 0;
	private int MAX_DMG = 0;
	
	private double CRIT_CHANCE = 0.0;
	private double CRIT_MULT = 1.0;
	
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
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.ARROW_TURRET);
	private String atImage_base = "arrowturret";

	// Constructor
	public ArrowTurret(int startX, int startY, int shootX, int shootY, int interval, int offset) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		// Can't be damaged
		this.armor = 100;
		
		if(shootX == 0 && shootY == 0) {
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
		
		this.imagePath = this.getImage();
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
		switch(this.state) {
			case ArrowTurret.STATE_INACTIVE:
				return (this.imageDir + this.atImage_base + "_INACTIVE.png");
			case ArrowTurret.STATE_ACTIVE:
				if((this.shotCount - 1) >= this.shotMax) {
					return (this.imageDir + this.atImage_base + "_ATT.png");
				} else if(this.shotCount >= this.shotMax) {
					return (this.imageDir + this.atImage_base + "_PREP.png");
				} else {
					return (this.imageDir + this.atImage_base + "_INACTIVE.png");
				}
			default:
				System.out.println
					(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
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
		if(!this.isAlive() || !EntityManager.getInstance().getPlayer().isAlive()) {
			// Do nothing
			return;
		}
		
		// Depending on current state, act accordingly
		switch(this.state) {
			case ArrowTurret.STATE_INACTIVE:
				// Do nothing because it's inactive
				break;
			case ArrowTurret.STATE_ACTIVE:
				// Reset our counter
				if(this.shotCount > this.shotMax) {
					this.shotCount = 0;
				}
				
				// We're active, so shoot arrows periodically
				if(this.shotCount < this.shotMax) {
					// Do nothing, we're on cooldown
				} else {
					// Play arrow firing sound
					SoundPlayer.playWAV(GPath.createSoundPath("arrow_SHOT.wav"));
					
					// Fire an arrow in a predetermined direction
					EntityManager.getInstance().getProjectileManager()
						.addProjectile(new Arrow((this.xPos + this.shootX),
												(this.yPos + this.shootY),
												this.shootX,
												this.shootY, this));
				}
				
				// Increment our cooldown counter
				this.shotCount += 1;
				break;
			default:
				System.out.println(this.getName() + " couldn't identify its state!");
				break;
		}
	}

	@Override
	public void onDeath() {
		// Doesn't die, so nothing
	}
	
	// Makes the turret stop shooting
	public void setInactive() {
		this.state = ArrowTurret.STATE_INACTIVE;
	}

}
