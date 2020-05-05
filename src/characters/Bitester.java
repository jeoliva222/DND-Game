package characters;

import java.awt.Dimension;

import ai.DumbFollow;
import ai.IdleController;
import ai.IslandChecker;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import gui.GameScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;
import tiles.TileType;

/**
 * Class that represents Bitester enemy
 * @author jeoliva
 */
public class Bitester extends GCharacter {
	
	// Serialization ID
	private static final long serialVersionUID = 4152985925566994657L;
	
	// Modifiers/Statistics

	private static int MAX_HP = 4;
	
	private static int MIN_DMG = 2;
	private static int MAX_DMG = 2;
	
	private static double CRIT_CHANCE = 0.3;
	private static double CRIT_MULT = 2.0;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_PURSUE = 1;
	
	//----------------------------
	
	// Additional Behavior
	protected int chaseCount = 0;
	protected int chaseMax = 2;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BITESTER);
	private static String btImage_base = "bitester";
	
	private static String btImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BITESTER, "bitester_dead.png");
	private static String btImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.BITESTER, "bitester_dead_CRIT.png");
	

	public Bitester(int startX, int startY) {
		this(startX, startY, PatrolPattern.WANDER);
	}
	
	public Bitester(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Bitester.STATE_IDLE;
		this.patrolPattern = patpat;
	}
	
	public String getName() {
		return "Bitester";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + btImage_base);
		String hpPath = "";
		String statePath = "";
		
		// Add path modifier based on current health level
		if (currentHP > (maxHP / 2)) {
			hpPath = "_full";
		} else if (currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		// Add path modifier based on current AI state
		switch (state) {
			case Bitester.STATE_PURSUE:
				statePath = "_ALERT";
				break;
			case Bitester.STATE_IDLE:
				// No extra path
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if (currentHP < -(maxHP / 2)) {
			return btImage_DEAD_CRIT;
		} else {
			return btImage_DEAD;
		}
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_WATER)));
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Bitester_ATTACK.wav"));
		attackPlayer();
	}

	@Override
	public void onDeath() {
		if (currentHP < -(maxHP / 2)) {
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
		
		// Get tile type the player is standing on
		TileType tt = GameScreen.getTile(player.getXPos(), player.getYPos()).getTileType();
		
		switch (state) {
			case Bitester.STATE_PURSUE:	
				// If Bitester has fully lost interest, then return to idle
				if (chaseCount >= chaseMax) {
					this.chaseCount = 0;
					this.state = Bitester.STATE_IDLE;
					return;
				}
				
				// If player hops out of water, start to lose interest
				if (!MovableType.isWater(tt.getMovableType())) {
					this.chaseCount++;
				} else {
					this.chaseCount = 0;
				}
				
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
				
				// Attack if next to player and they're in water
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					if (MovableType.isWater(tt.getMovableType())) {
						playerInitiate();
					}
				} else {
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
				}
				break;
			case Bitester.STATE_IDLE:
				// Do nothing, until player steps in same pool of water
				if ((MovableType.isWater(tt.getMovableType())) &&
						IslandChecker.virusStart(xPos, yPos, plrX, plrY, MovableType.WATER)) {
					// Alert and pursue if in same pool
					SoundPlayer.playWAV(GPath.createSoundPath("Bitester_ALERT.wav"));
					this.state = Bitester.STATE_PURSUE;
					return;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}
			
	}

}
