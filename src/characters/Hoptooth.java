package characters;

import java.awt.Dimension;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import effects.DamageIndicator;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

public class Hoptooth extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 7655232320739396671L;
	
	// Modifiers/Statistics

	private int MAX_HP = 30;
	
	private int MIN_DMG = 6;
	private int MAX_DMG = 10;
	
	private double CRIT_CHANCE = 0.1;
	private double CRIT_MULT = 1.3;
	
	private int ARMOR_VAL = 1;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP = 3;
	private static final int STATE_ATT = 4;
	
	//----------------------------
	
	// Additional parameters
	
	// Indicate which direction the NPC is launching its attack
	protected int markX = 0;
	protected int markY = 0;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.HOPTOOTH);
	private String htImage_base = "hoptooth";
	
	private String htImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE, "beanpole_dead.png");
	private String htImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE, "beanpole_dead_CRIT.png");


	public Hoptooth(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Hoptooth.STATE_IDLE;
		this.patrolPattern = PatrolPattern.WANDER;
		
		this.imagePath = this.getImage();
	}
	
	public Hoptooth(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Hoptooth.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Hoptooth";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.htImage_base;
		String hpPath = "";
		String statePath = "";

		// TODO
		hpPath = "_full";
	
//		if(this.currentHP > (this.maxHP / 2)) {
//			hpPath = "_full";
//		} else if(this.currentHP > 0) {
//			hpPath = "_fatal";
//		} else {
//			hpPath = "_dead";
//			return (imgPath + hpPath + ".png");
//		}
//		
//		switch(this.state) {
//		case Hoptooth.STATE_IDLE:
//		case Hoptooth.STATE_PURSUE:
//			// No extra path
//			break;
//		case Hoptooth.STATE_ALERTED:
//		case Hoptooth.STATE_PREP:
//			statePath = "_PREP";
//			break;
//		case Hoptooth.STATE_ATT:
//			statePath = "_ATT";
//			break;
//		default:
//			System.out.println
//				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
//			return GPath.NULL;
//		}
	
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if(this.currentHP < -(this.maxHP / 2)) {
			return this.htImage_DEAD_CRIT;
		} else {
			return this.htImage_DEAD;
		}
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
		this.moveTypes.add(MovableType.WATER);
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
			case Hoptooth.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("Soul_Cry1.wav"));
					this.state = Hoptooth.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case Hoptooth.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = Hoptooth.STATE_PURSUE;
				break;
			case Hoptooth.STATE_PURSUE:	
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
				
				// Change state to prep if next to player
				if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					// Mark direction to attack next turn
					this.markX = dx;
					this.markY = dy;
					
					// Change states
					this.state = Hoptooth.STATE_PREP;
				} else {
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
				}
				break;
			case Hoptooth.STATE_PREP:
				// Mark tile with damage indicator
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.markX, this.yPos + this.markY));
				
				// Attack in marked direction
				if((this.xPos + this.markX) == plrX && (this.yPos + this.markY) == plrY)
					this.playerInitiate();
				this.state = Hoptooth.STATE_ATT;
				break;
			case Hoptooth.STATE_ATT:
				// Cooldown period for one turn
				this.state = Hoptooth.STATE_PURSUE;
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
	
}
