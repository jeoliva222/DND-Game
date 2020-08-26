package characters;

import java.awt.Dimension;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.CactianNeedle;
import tiles.MovableType;

/**
 * Class representing the Cactian enemy found in the Poacher's Desert area
 * @author jeoliva
 */
public class Cactian extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -3627697737580452682L;

	// Modifiers/Statistics
	
	private static int MAX_HP = 8;
	
	private static int ARMOR_VAL = 1;
	
	private static int MIN_DMG = 3;
	private static int MAX_DMG = 4;
	
	private static double CRIT_CHANCE = 0.1;
	private static double CRIT_MULT = 1.5;
	
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
	
	// Determines if Cactian should awake this turn
	protected boolean shouldAwaken = false;
	
	// Determines what "step" the Cactian is on in their walk cycle
	protected boolean whichStep = false;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.CACTIAN);
	private static String ctImage_base = "cactian";
	
	private static String ctImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.CACTIAN, "cactian_dead.png");

	// Constructors
	public Cactian(int startX, int startY) {
		this(startX, startY, PatrolPattern.STATIONARY);
	}
	
	public Cactian(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Cactian.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.canFocus = false;
	}
	
	public String getName() {
		return "Cactian";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + ctImage_base);
		String hpPath = "";
		String statePath = "";
		
		if (currentHP > (maxHP / 2)) {
			hpPath = "_full";
		} else if (currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch (state) {
			case Cactian.STATE_IDLE:
				// No extra path
				break;
			case Cactian.STATE_PURSUE:
				if (whichStep) {
					statePath = "_PURSUE_STEP1";
				} else {
					statePath = "_PURSUE_STEP2";
				}
				break;
			case Cactian.STATE_ALERTED:
			case Cactian.STATE_AWAKEN:
				statePath = "_ALERT";
				break;
			case Cactian.STATE_PREP:
				statePath = "_PREP_SHOOT";
				break;
			case Cactian.STATE_ATT:
				statePath = "_ATT_SHOOT";
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return ctImage_DEAD;
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_GROUND)));
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"), getXPos(), getYPos());
		attackPlayer();
	}
	
	@Override
	public void onDeath() {
		SoundPlayer.playWAV(GPath.createSoundPath("Cactian_DEATH.wav"), 2f, getXPos(), getYPos());
	}
	
	// Override that resets a few extra parameters
	@Override
	public void resetParams() {
		this.shouldAwaken = false;
		this.canFocus = false;
	}

	@Override
	public void takeTurn() {
		// Get reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if (!isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Get relative location to player
		int distX = (plrX - xPos);
		int distY = (plrY - yPos);
		
		switch (state) {
			case Cactian.STATE_IDLE:
				// If next to the player or damaged, awaken and alert other Cactians on this screen
				if (((Math.abs(distX) <= 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) <= 1)) ||
						currentHP != MAX_HP) {
					SoundPlayer.playWAV(GPath.createSoundPath("Cactian_ALERT.wav"), 3f, getXPos(), getYPos());
					this.state = Cactian.STATE_ALERTED;
				} else if (shouldAwaken) {
					// Change state
					SoundPlayer.playWAV(GPath.createSoundPath("Cactian_ALERT.wav"), 3f, getXPos(), getYPos());
					this.state = Cactian.STATE_AWAKEN;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case Cactian.STATE_ALERTED:
				// Rest for one turn, alerting all other Cactians on the screen
				EntityManager em = EntityManager.getInstance();
				for (GCharacter npc: em.getNPCManager().getCharacters()) {
					if (npc != this && npc instanceof Cactian) {
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
				if (distX > 0) {
					dx = 1;
				} else if (distX < 0) {
					dx = -1;
				}
				
				if (distY > 0) {
					dy = 1;
				} else if (distY < 0) {
					dy = -1;
				}
				
				// Change state to prep if in a line and at least 4 tiles from player
				if (((Math.abs(distX) <= 4) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) <= 4))) {
					boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
					if (hasLOS) {
						// Mark direction to attack next turn
						this.markX = dx;
						this.markY = dy;
						
						// Change states to prepare projectile and don't move this turn
						this.state = Cactian.STATE_PREP;
						break;
					}
				}
				
				// Path-find to the player if we can
				Dimension nextStep = PathFinder.findPath(xPos, yPos, plrX, plrY, this);
				if (nextStep == null) {
					// Blindly pursue the target
					DumbFollow.blindPursue(distX, distY, dx, dy, this);
				} else {
					int changeX = (nextStep.width - xPos);
					int changeY = (nextStep.height - yPos);
					moveCharacter(changeX, changeY);
				}
				
				// Alternate step pattern
				this.whichStep = !(whichStep);
				break;
			case Cactian.STATE_PREP:
				// Shoot projectile in player's direction
				EntityManager.getInstance().getProjectileManager()
					.addProjectile(new CactianNeedle((xPos + markX),
										(yPos + markY),
										markX,
										markY, getClass()));
				
				// Play arrow shot sound
				SoundPlayer.playWAV(GPath.createSoundPath("Cactian_TOSS.wav"), getXPos(), getYPos());

				// Changes states
				this.state = Cactian.STATE_ATT;
				break;
			case Cactian.STATE_ATT:
				// Cooldown period for one turn
				this.state = Cactian.STATE_PURSUE;
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}
			
	}
	
}
