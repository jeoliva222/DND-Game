package characters;

import java.awt.Dimension;

import ai.DumbFollow;
import ai.IdleController;
import ai.IslandChecker;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import debuffs.RootDebuff;
import gui.GameScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;
import tiles.TileType;

// Class representing Vish enemy found in the Sewers area
public class Vish extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -4999201724388843481L;
	
	// Modifiers/Statistics

	private int MAX_HP = 6;
	
	private int MIN_DMG = 2;
	private int MAX_DMG = 3;
	
	private double CRIT_CHANCE = 0.1;
	private double CRIT_MULT = 1.4;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_PURSUE = 1;
	private static final int STATE_GO_HOME = 2;
	
	//----------------------------
	
	// Additional Behavior
	protected int chaseCount = 0;
	protected int chaseMax = 2;
	
	// Aggro radius
	private final int aggroDist = 3;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BITESTER);
	private String btImage_base = "bitester";
	
	private String btImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BITESTER, "bitester_dead.png");
	private String btImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.BITESTER, "bitester_dead_CRIT.png");



	public Vish(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Vish.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.imagePath = this.getImage();
	}
	
	public Vish(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Vish.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Vish";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.btImage_base;
		String hpPath = "";
		String statePath = "";
		
		// Add path modifier based on current health level
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		// Add path modifier based on current AI state
		switch(this.state) {
		case Vish.STATE_PURSUE:
			statePath = "_ALERT";
			break;
		case Vish.STATE_GO_HOME:
		case Vish.STATE_IDLE:
			// TODO - Add hidden image
			return GPath.NULL;
		default:
			System.out.println
				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
			return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if(this.currentHP < -(this.maxHP / 2)) {
			return this.btImage_DEAD_CRIT;
		} else {
			return this.btImage_DEAD;
		}
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.WATER);
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Bitester_ATTACK.wav"));
		EntityManager.getInstance().getPlayer().addBuff(new RootDebuff(1));
		this.attackPlayer();
	}

	@Override
	public void onDeath() {
		if(this.currentHP < -(this.maxHP / 2)) {
			SoundPlayer.playWAV(GPath.createSoundPath("Bitester_DEATH_CRIT.wav"));
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("Bitester_DEATH.wav"));
		}
	}
	
	// Override that resets a few extra parameters
	@Override
	public void resetParams() {
		this.chaseCount = 0;
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
		
		// Total tile distance from player
		int totalDist = (Math.abs(distX) + Math.abs(distY));
		
		// Relative movement direction (Initialize at 0)
		int dx = 0;
		int dy = 0;
		
		TileType tt = GameScreen
				.getTile(player.getXPos(), player.getYPos()).getTileType();
		
		switch(this.state) {
			case Vish.STATE_PURSUE:	
				
				// If Vish has fully lost interest, then return to idle
				if(this.chaseCount >= this.chaseMax) {
					this.chaseCount = 0;
					this.state = Vish.STATE_GO_HOME;
					return;
				}
				
				// If player hops out of water, start to lose interest
				if(tt.getMovableType() != MovableType.WATER) {
					this.chaseCount++;
				} else {
					this.chaseCount = 0;
				}
				
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
				
				// Attack if next to player and they're in water
				if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					if(tt.getMovableType() == MovableType.WATER) {
						this.playerInitiate();
					}
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
			case Vish.STATE_GO_HOME:
				
				// Check if player has re-entered the water
				if(tt.getMovableType() == MovableType.WATER && (totalDist <= this.aggroDist) &&
						IslandChecker.virusStart(this.xPos, this.yPos, plrX, plrY, MovableType.WATER)) {
					// Alert and pursue if in same pool
					SoundPlayer.playWAV(GPath.createSoundPath("Bitester_ALERT.wav"));
					this.state = Vish.STATE_PURSUE;
					break;
				}
					
				// Get relative location to position of Vish's origin position
				int originDistX = this.xOrigin - this.xPos;
				int originDistY = this.yOrigin - this.yPos;
				
				// Calculate relative movement directions to get to corpse
				if(originDistX > 0) {
					dx = 1;
				} else if (originDistX < 0) {
					dx = -1;
				}
				
				if(originDistY > 0) {
					dy = 1;
				} else if (originDistY < 0) {
					dy = -1;
				}
				
				// If Vish is back at its origin, it can go idle again
				if(((Math.abs(originDistX) == 0) && (Math.abs(originDistY) == 0))) {
					// Go idle again
					this.state = Vish.STATE_IDLE;
				} else {
					// Path-find to our origin if possible
					Dimension nextStep = PathFinder.findPath(this.xPos, this.yPos, plrX, plrY, this);
					if(nextStep == null) {
						// Blindly pursue the origin if path-finding not possible
						DumbFollow.blindPursue(originDistX, originDistY, dx, dy, this);
					} else {
						int changeX = nextStep.width - this.xPos;
						int changeY = nextStep.height - this.yPos;
						this.moveCharacter(changeX, changeY);
					}
				}
				break;
			case Vish.STATE_IDLE:
				// Do nothing, until player steps in same pool of water
				if(tt.getMovableType() == MovableType.WATER && (totalDist <= this.aggroDist) &&
						IslandChecker.virusStart(this.xPos, this.yPos, plrX, plrY, MovableType.WATER)) {
					// Alert and pursue if in same pool
					SoundPlayer.playWAV(GPath.createSoundPath("Bitester_ALERT.wav"));
					this.state = Vish.STATE_PURSUE;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
	
}
