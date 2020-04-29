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
import effects.WarningIndicator;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

// Class representing 'Hoptooth' enemy in-game
public class Hoptooth extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 7655232320739396671L;
	
	// Modifiers/Statistics

	private int MAX_HP = 20;
	
	private int MIN_DMG = 4;
	private int MAX_DMG = 6;
	
	private double CRIT_CHANCE = 0.1;
	private double CRIT_MULT = 1.5;
	
	private int ARMOR_VAL = 0;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP_SWING = 3;
	private static final int STATE_ATT_SWING = 4;
	private static final int STATE_PREP_CHOMP = 5;
	private static final int STATE_ATT_CHOMP = 6;
	
	//----------------------------
	
	// Additional parameters
	
	// Indicate which direction the NPC is launching its attack
	protected int markX = 0;
	protected int markY = 0;
	
	// Attack counter
	private int attCount = 0;
	
	// Number of turns needed to perform chomp attack
	private final int windupMax = 2;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.HOPTOOTH);
	private String htImage_base = "hoptooth";
	
	private String htImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.HOPTOOTH, "hoptooth_dead.png");
	private String htImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.HOPTOOTH, "hoptooth_dead.png");


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

		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch(this.state) {
		case Hoptooth.STATE_IDLE:
			statePath = "_IDLE";
			break;
		case Hoptooth.STATE_PURSUE:
			// No extra path
			break;
		case Hoptooth.STATE_ALERTED:
		case Hoptooth.STATE_PREP_CHOMP:
			if(this.attCount == 0) {
				statePath = "_PREP_CHOMP";
			} else {
				statePath = "_PREP_CHOMP2";
			}
			break;
		case Hoptooth.STATE_ATT_CHOMP:
			statePath = "_ATT_CHOMP";
			break;
		case Hoptooth.STATE_PREP_SWING:
			statePath = "_PREP_SWING";
			break;
		case Hoptooth.STATE_ATT_SWING:
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
		if(this.currentHP < -(this.maxHP / 2)) {
			return this.htImage_DEAD_CRIT;
		} else {
			return this.htImage_DEAD;
		}
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_WATER)));
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		this.attackPlayer();
	}
	
	@Override
	public void onDeath() {
		SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Death.wav"), 3f);
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
		
		// Relative movement direction (Initialize at 0)
		int dx = 0;
		int dy = 0;
		
		switch(this.state) {
			case Hoptooth.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Alert.wav"));
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
					
					// Choose a new melee attack and change states
					this.chooseMeleeAttack();
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
					
					// Decide if Hoptooth should attempt a running chomp prep
					// Attempt only 1/2 of the time
					int shouldAttack = new Random().nextInt(2);
					if((shouldAttack == 0) && (((Math.abs(distX) <= 1) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 1)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
						if(hasAttLOS) {
							this.markX = dx;
							this.markY = dy;
							
							SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Breath1.wav"), -5f);
							this.state = Hoptooth.STATE_PREP_CHOMP;
						}
					}
				}
				break;
			case Hoptooth.STATE_PREP_SWING:
				// Mark tile with damage indicator
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.markX, this.yPos + this.markY));
				
				// Attack in marked direction
				if((this.xPos + this.markX) == plrX && (this.yPos + this.markY) == plrY)
					this.playerInitiate();
				this.state = Hoptooth.STATE_ATT_SWING;
				break;
			case Hoptooth.STATE_ATT_SWING:
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
				
				// If immediately next to player after attack, cue up another attack
				if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					// Mark direction to attack next turn
					this.markX = dx;
					this.markY = dy;
					
					// Change state to attack again
					this.state = Hoptooth.STATE_PREP_SWING;
				} else {
					// Otherwise, go back to pursuing the player
					this.state = Hoptooth.STATE_PURSUE;
				}
				break;
			case Hoptooth.STATE_PREP_CHOMP:
				// Increment attack counter
				this.attCount += 1;
				
				// Only perform attack if we've done our full wind-up
				if(this.attCount == (this.windupMax - 1)) {
					// Mark tile with damage indicator
					EntityManager.getInstance().getEffectManager().addEffect(new WarningIndicator(this.xPos + this.markX, this.yPos + this.markY));
					
					// Play warning sound
					SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Breath2.wav"));
				} else if(this.attCount >= this.windupMax) {
					// Mark tile with damage indicator
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.markX, this.yPos + this.markY));
					
					// Play chomp sound
					SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Chomp.wav"));
					
					// Attack in marked direction
					if((this.xPos + this.markX) == plrX && (this.yPos + this.markY) == plrY) {
						this.chompPlayer();
					}

					// Reset attack counter and switch states
					this.attCount = 0;
					this.state = Hoptooth.STATE_ATT_CHOMP;
				}
				break;
			case Hoptooth.STATE_ATT_CHOMP:
				// Cooldown period for one turn
				this.state = Hoptooth.STATE_PURSUE;
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
	
	// Choose a random melee attack
	private void chooseMeleeAttack() {
		int whichAttack = new Random().nextInt(3);
		if(whichAttack == 0) {
			SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Breath1.wav"), -5f);
			this.state = Hoptooth.STATE_PREP_CHOMP;
		} else {
			this.state = Hoptooth.STATE_PREP_SWING;
		}
	}
	
	// Execute the player with a fatal chomp
	private void chompPlayer() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH_CRIT.wav"));
		LogScreen.log(this.getName() + " bit the player's head off.", GColors.DAMAGE);
		EntityManager.getInstance().getPlayer().damagePlayer(300);
	}
	
}
