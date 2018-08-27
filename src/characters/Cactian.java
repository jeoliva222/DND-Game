package characters;

import java.awt.Dimension;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.Arrow;
import tiles.MovableType;

// Class representing the 'Cactian' enemy found in the Poacher's Desert area
public class Cactian extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -3627697737580452682L;

	// Modifiers/Statistics
	
	private int MAX_HP = 8;
	
	private int ARMOR_VAL = 1;
	
	private int MIN_DMG = 2;
	private int MAX_DMG = 4;
	
	private double CRIT_CHANCE = 0.1;
	private double CRIT_MULT = 1.5;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_AWAKEN = 2;
	private static final int STATE_PURSUE = 3;
	private static final int STATE_PREP = 4;
	private static final int STATE_ATT = 5;
	
	//----------------------------
	
	// Additional parameters
	
	// Indicate which direction the NPC is launching its attack
	protected int markX = 0;
	protected int markY = 0;
	
	protected boolean shouldAwaken = false;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE);
	private String bpImage_base = "beanpole";
	
	private String bpImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE, "beanpole_dead.png");
	private String bpImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE, "beanpole_dead_CRIT.png");

	// Constructors
	public Cactian(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Cactian.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.canFocus = false;
		
		this.imagePath = this.getImage();
	}
	
	public Cactian(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Cactian.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.canFocus = false;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Cactian";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.bpImage_base;
		String hpPath = "";
		String statePath = "";
		
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch(this.state) {
			case Cactian.STATE_IDLE:
				// TODO
				break;
			case Cactian.STATE_PURSUE:
				// No extra path
				break;
			case Cactian.STATE_ALERTED:
			case Cactian.STATE_AWAKEN:
			case Cactian.STATE_PREP:
				statePath = "_PREP";
				break;
			case Cactian.STATE_ATT:
				statePath = "_ATT";
				break;
			default:
				System.out.println
					(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if(this.currentHP < -(this.maxHP / 2)) {
			return this.bpImage_DEAD_CRIT;
		} else {
			return this.bpImage_DEAD;
		}
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
		if(this.currentHP < -(this.maxHP / 2)) {
			SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH_CRIT.wav"));
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH.wav"));
		}
	}
	
	// Override that resets a few extra parameters
	@Override
	public void returnToOrigin() {
		super.returnToOrigin();
		this.shouldAwaken = false;
		this.canFocus = false;
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
			case Cactian.STATE_IDLE:
				// If next to the player, then awaken
				if(((Math.abs(distX) <= 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) <= 1))) {
					SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ALERT.wav"));
					this.state = Cactian.STATE_ALERTED;
				} else if(this.shouldAwaken) {
					// Change state
					this.state = Cactian.STATE_AWAKEN;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case Cactian.STATE_ALERTED:
				// Rest for one turn, alerting all other Cactians on the screen
				EntityManager em = EntityManager.getInstance();
				for(GCharacter npc: em.getNPCManager().getCharacters()) {
					if(npc != this && npc instanceof Cactian) {
						// Set state to awaken
						Cactian ct = (Cactian) npc;
						ct.shouldAwaken = true;
					}
				}
				
				// Set to allow focusing
				this.canFocus = true;
				
				// Then, start to chase player
				this.state = Cactian.STATE_PURSUE;
				break;
			case Cactian.STATE_AWAKEN:
				
				// Set to allow focusing
				this.canFocus = true;
				
				// Rest for one turn
				// Then, start to chase player
				this.state = Cactian.STATE_PURSUE;
				break;
			case Cactian.STATE_PURSUE:	
				// Relative movement direction (Initialize at 0)
				int dx = 0;
				int dy = 0;
				
				// Calculate relative movement directions
				if(distX > 0) {
					dx = 1;
				} else if (distX < 0) {
					dx = -1;
				}
				
				if(distY > 0) {
					dy = 1;
				} else if (distY < 0) {
					dy = -1;
				}
				
				// Change state to prep if in a line and at least 4 tiles from player
				if(((Math.abs(distX) <= 4) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) <= 4))) {
					boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
					if(hasLOS) {
						// Mark direction to attack next turn
						this.markX = dx;
						this.markY = dy;
						
						// Change states to prepare projectile and don't move this turn
						this.state = Cactian.STATE_PREP;
						break;
					}
				}
				
				// Path-find to the player if we can
				Dimension nextStep = PathFinder.findPath(this.xPos, this.yPos, plrX, plrY, this);
				if(nextStep == null) {
					// Blindly pursue the target
					DumbFollow.blindPursue(distX, distY, dx, dy, this);
				} else {
					int changeX = nextStep.width - this.xPos;
					int changeY = nextStep.height - this.yPos;
					this.moveCharacter(changeX, changeY);
				}
				break;
			case Cactian.STATE_PREP:
				// Shoot projectile in player's direction
				// TODO
				EntityManager.getInstance().getProjectileManager()
				.addProjectile(new Arrow((this.xPos + this.markX),
										(this.yPos + this.markY),
										this.markX,
										this.markY, this));

				// Changes states
				this.state = Cactian.STATE_ATT;
				break;
			case Cactian.STATE_ATT:
				// Cooldown period for one turn
				this.state = Cactian.STATE_PURSUE;
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
	
}
