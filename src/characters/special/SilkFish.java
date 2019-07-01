package characters.special;


import ai.DumbFollow;
import ai.IdleController;
import ai.IslandChecker;
import ai.PatrolPattern;
import characters.GCharacter;
import characters.Player;
import gui.GameScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import items.GPickup;
import items.special.SilkFishSkin;
import managers.EntityManager;
import tiles.MovableType;
import tiles.TileType;

public class SilkFish extends GCharacter {
	
	// Serialization ID
	private static final long serialVersionUID = 3913715402728712355L;

	// Modifiers/Statistics

	private int MAX_HP = 6;
	
	private int MIN_DMG = 0;
	private int MAX_DMG = 0;
	
	private double CRIT_CHANCE = 0.0;
	private double CRIT_MULT = 1.0;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_FLEE = 1;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BITESTER);
	private String btImage_base = "bitester";

	public SilkFish(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SilkFish.STATE_IDLE;
		this.patrolPattern = PatrolPattern.WANDER;
		
		this.imagePath = this.getImage();
	}
	
	public SilkFish(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SilkFish.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Silk Fish";
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
		case SilkFish.STATE_FLEE:
			statePath = "_ALERT";
			break;
		case SilkFish.STATE_IDLE:
			// No extra path
			break;
		default:
			System.out.println
				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
			return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		// No corpse image
		return GPath.NULL;
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.WATER);
	}

	@Override
	public void playerInitiate() {
		// Does not attack
	}

	@Override
	public void onDeath() {
		// Play death sound
		SoundPlayer.playWAV(GPath.createSoundPath("Bitester_DEATH_CRIT.wav"));
		
		// Drop the soft skin
		EntityManager.getInstance().getPickupManager().addPickup(new GPickup(this.xPos, this.yPos, new SilkFishSkin()));
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
		
		TileType tt = GameScreen
				.getTile(player.getXPos(), player.getYPos()).getTileType();
		
		switch(this.state) {
			case SilkFish.STATE_FLEE:	
				
				// If player hops out of water, stop fleeing
				if(tt.getMovableType() != MovableType.WATER) {
					this.state = SilkFish.STATE_IDLE;
					return;
				}
				
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
				
				// Blindly flee the player
				DumbFollow.blindPursue(distX, distY, -dx, -dy, this);

				break;
			case SilkFish.STATE_IDLE:
				// Do nothing, until player steps in same pool of water
				if(tt.getMovableType() == MovableType.WATER &&
						IslandChecker.virusStart(this.xPos, this.yPos, plrX, plrY, MovableType.WATER)) {
					// Alert and flee if in same pool
					SoundPlayer.playWAV(GPath.createSoundPath("Bitester_ALERT.wav"));
					this.state = SilkFish.STATE_FLEE;
					return;
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
