package characters;

import ai.IdleController;
import ai.IslandChecker;
import ai.PatrolPattern;
import buffs.Buff;
import characters.allies.Player;
import gui.GameScreen;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;
import tiles.TileType;

// Class representing unkillable underwater presence in the Sewer levels
public class ChagrinShadow extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -2991225437087878361L;
	
	// Modifiers/Statistics

	private int MAX_HP = 100;
	
	private int MIN_DMG = 300;
	private int MAX_DMG = 300;
	
	private double CRIT_CHANCE = 0.0;
	private double CRIT_MULT = 1.0;
	
	private int ARMOR_VAL = 100;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_PURSUE = 1;
	
	//----------------------------
	
	// Additional Behavior
	
	// Number of turns player has spent in water with the Shadow
	protected int chaseCount = 0;
	
	// Number of turns needed to perform killing move on player in water
	protected int chaseMax = 3;
	
	// Coordinates of pool the Shadow resides in
	protected int poolXPos;
	protected int poolYPos;
	
	//----------------------------

	public ChagrinShadow(int startX, int startY, int poolXPos, int poolYPos) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.armor = ARMOR_VAL;
		
		this.poolXPos = poolXPos;
		this.poolYPos = poolYPos;
		
		this.state = ChagrinShadow.STATE_IDLE;
		this.patrolPattern = PatrolPattern.WANDER;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Bitester";
	}
	
	@Override
	public String getImage() {
		// Never has an image
		return GPath.NULL;
	}
	
	public String getCorpseImage() {
		// Never has an image
		return GPath.NULL;
	}
	
	public void populateMoveTypes() {
		// Does not "move" in the typical sense
	}

	@Override
	public void playerInitiate() {
		// Play attack sound
		SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Chomp.wav"), -10);
		
		// Fetch reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		LogScreen.log("Player was dragged under...", GColors.DAMAGE);
		player.damagePlayer(300);
	}
	
	@Override
	public void addBuff(Buff debuff) {
		// Don't add the buff
		return;
	}

	@Override
	public void onDeath() {
		// Nothing
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
		
		TileType tt = GameScreen
				.getTile(player.getXPos(), player.getYPos()).getTileType();
		
		switch(this.state) {
			case ChagrinShadow.STATE_PURSUE:
				
				// If player hops out of water, lose interest
				if(tt.getMovableType() != MovableType.WATER) {
					this.chaseCount = 0;
					this.state = ChagrinShadow.STATE_IDLE;
					return;
				}
				
				// Increment chase count
				this.chaseCount += 1;
				
				if (chaseCount == 2) {
					SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Breath2.wav"), -15);
				}
				
				// If Shadow has maxed its chase count, attack the player
				if(this.chaseCount >= this.chaseMax) {
					this.chaseCount = 0;
					this.playerInitiate();
					return;
				}
				
				break;
			case ChagrinShadow.STATE_IDLE:
				// Do nothing, until player steps in same pool of water
				if(tt.getMovableType() == MovableType.WATER &&
						IslandChecker.virusStart(this.poolXPos, this.poolYPos, plrX, plrY, MovableType.WATER)) {
					// Alert and pursue if in same pool
					SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Breath1.wav"), -15);
					this.chaseCount += 1;
					this.state = ChagrinShadow.STATE_PURSUE;
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
