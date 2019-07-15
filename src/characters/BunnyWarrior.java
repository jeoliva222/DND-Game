package characters;

import java.awt.Dimension;
import java.util.Random;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import effects.DamageIndicator;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

// Class that defines the Bunny Warrior enemy in-game
public class BunnyWarrior extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 7502969959532923677L;
	
	// Modifiers/Statistics

	private int MAX_HP = 8;
	
	private int MIN_DMG = 3;
	private int MAX_DMG = 4;
	
	private double CRIT_CHANCE = 0.15;
	private double CRIT_MULT = 1.25;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP_STAB = 3;
	private static final int STATE_ATT_STAB = 4;
	private static final int STATE_PREP_SWIPE = 5;
	private static final int STATE_ATT_SWIPE = 6;
	
	//----------------------------
	// Additional Behavior
	
	// Direction the screen will be marked up for damage indicators
	private int xMarkDir = 0;
	private int yMarkDir = 0;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BWARRIOR);
	private String bwImage_base = "bunnywarrior";
	
	private String bwImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BWARRIOR, this.bwImage_base+"_dead.png");

	// Constructor
	public BunnyWarrior(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = BunnyWarrior.STATE_IDLE;
		this.patrolPattern = PatrolPattern.PATROL;
		
		this.imagePath = this.getImage();
	}
	
	public BunnyWarrior(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = BunnyWarrior.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Bunny Warrior";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.bwImage_base;
		String hpPath = "";
		String statePath = "";
		
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return GPath.NULL;
		}
		
		switch(this.state) {
		case BunnyWarrior.STATE_IDLE:
		case BunnyWarrior.STATE_PURSUE:
			// No extra path
			break;
		case BunnyWarrior.STATE_PREP_STAB:
			statePath = "_PREP_STAB";
			break;
		case BunnyWarrior.STATE_ALERTED:
		case BunnyWarrior.STATE_ATT_STAB:
			statePath = "_ALERT";
			break;
		case BunnyWarrior.STATE_PREP_SWIPE:
			statePath = "_PREP_SWING";
			break;
		case BunnyWarrior.STATE_ATT_SWIPE:
			statePath = "_ATT_SWING";
			break;
		default:
			System.out.println
				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
			return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return this.bwImage_DEAD;
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
		// Randomly play one of two sounds
		Random r = new Random();
		int whichSound = r.nextInt(2);
		if(whichSound == 0) {
			SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_DEATH.wav"));
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_DEATH2.wav"));
		}
	}

	@Override
	public void takeTurn() {
		// Fetch reference to the player
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
			case BunnyWarrior.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					Random r = new Random();
					int whichSound = r.nextInt(2);
					if(whichSound == 0) {
						SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_ALERT.wav"));
					} else {
						SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_ALERT2.wav"));
					}
					this.state = BunnyWarrior.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case BunnyWarrior.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = BunnyWarrior.STATE_PURSUE;
				break;
			case BunnyWarrior.STATE_PURSUE:	
				// Relative movement direction (Initialize at 0)
				int dx = 0;
				int dy = 0;
				
				// Calculate relative movement directions
				// X-movement
				if(distX > 0) {
					dx = 1;
				} else if (distX < 0) {
					dx = -1;
				}
				// Y-movement
				if(distY > 0) {
					dy = 1;
				} else if (distY < 0) {
					dy = -1;
				}
				
				Random r = new Random();
				// Change state to prepare a stab/swipe 100% of the time if next to player
				if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					this.xMarkDir = dx;
					this.yMarkDir = dy;
					
					// Decide whether to swipe or stab
					int swipeOrStab = r.nextInt(2);
					if(swipeOrStab == 0) {
						this.state = BunnyWarrior.STATE_PREP_STAB;
					} else {
						this.state = BunnyWarrior.STATE_PREP_SWIPE;
					}
					
					// Else, try to move x-wise or y-wise closer to the player
					// based on which dimension you are further distance from them
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
					
					// Recalculate relative location to player
					distX = plrX - this.xPos;
					distY = plrY - this.yPos;
					
					// Relative movement direction (Initialize at 0)
					dx = 0;
					dy = 0;
					
					// Recalculate relative movement directions
					// X-movement
					if(distX > 0) {
						dx = 1;
					} else if (distX < 0) {
						dx = -1;
					}
					// Y-movement
					if(distY > 0) {
						dy = 1;
					} else if (distY < 0) {
						dy = -1;
					}
					
					// Decide if bunny should attempt long-ranged stab prep
					// Punishes running away and eager approaches
					// Attempt only 1/4 of the time
					int shouldAttack = r.nextInt(4);
					if((shouldAttack == 0) && (((Math.abs(distX) <= 3) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 3)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
						if(hasAttLOS) {
							this.xMarkDir = dx;
							this.yMarkDir = dy;
							this.state = BunnyWarrior.STATE_PREP_STAB;
						}
					}
				}
				break;
			case BunnyWarrior.STATE_PREP_STAB:
				// Mark tiles with damage indicators
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir));
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + (this.xMarkDir*2), this.yPos + (this.yMarkDir*2)));
				
				// Play sound
				SoundPlayer.playWAV(GPath.createSoundPath("whip_ATT.wav"));
				
				// Attack if next to player
				if((plrX == this.xPos + this.xMarkDir && plrY == this.yPos + this.yMarkDir) ||
						(plrX == this.xPos + (this.xMarkDir*2) && plrY == this.yPos + (this.yMarkDir*2))) {
					this.playerInitiate();
				}
				
				this.state = BunnyWarrior.STATE_ATT_STAB;
				break;
			case BunnyWarrior.STATE_ATT_STAB:
				// Cooldown period for one turn
				this.state = BunnyWarrior.STATE_PURSUE;
				break;
			case BunnyWarrior.STATE_PREP_SWIPE:
				// Use direction from player to mark squares
				SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
				if(Math.abs(this.xMarkDir) > Math.abs(this.yMarkDir)) {
					// Player to left/right
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + 1));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos - 1));
					
					// Attack player if in affected space
					if((plrX == this.xPos + this.xMarkDir) &&
							(plrY == this.yPos || plrY == this.yPos - 1 || plrY == this.yPos + 1)) {
						this.playerInitiate();
					}
				} else {
					// Player above/below
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos, this.yPos + this.yMarkDir));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + 1, this.yPos + this.yMarkDir));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos - 1, this.yPos + this.yMarkDir));
					
					// Attack player if in affected space
					if((plrY == this.yPos + this.yMarkDir) &&
							(plrX == this.xPos || plrX == this.xPos - 1 || plrX == this.xPos + 1)) {
						this.playerInitiate();
					}
				}
				
				// Change state
				this.state = BunnyWarrior.STATE_ATT_SWIPE;
				break;
			case BunnyWarrior.STATE_ATT_SWIPE:
				// Cooldown period for one turn
				this.state = BunnyWarrior.STATE_PURSUE;
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
}
