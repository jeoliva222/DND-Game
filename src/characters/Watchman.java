package characters;

import java.awt.Dimension;
import java.util.ArrayList;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import effects.MusicEffect;
import gui.GameScreen;
import gui.GameTile;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.ExtraTile;
import tiles.MovableType;
import tiles.TileType;

// Class represents the 'Watchman' enemy in-game
public class Watchman extends GCharacter {

	// Modifiers/Statistics
	
	// Serialization ID
	private static final long serialVersionUID = -7747701036774455549L;

	private int MAX_HP = 6;
	
	private int MIN_DMG = 1;
	private int MAX_DMG = 2;
	
	private double CRIT_CHANCE = 0.15;
	private double CRIT_MULT = 1.5;
	
	private int ARMOR_VAL = 1;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP = 3;
	private static final int STATE_ATT = 4;
	
	//----------------------------
	
	// Additional Info
	
	// Variables used to control attack windup length
	private int windupCount = 0;
	private final int windupMax = 2;
	
	// Variables used to control attack direction
	protected int xMarkDir = 0;
	protected int yMarkDir = 0;
	
	// Variables used to spawn enemies and tiles
	protected ArrayList<GCharacter> npcList;
	protected ArrayList<ExtraTile> tileList;
	
	// Flag indicated whether we've spawned our entities once already
	protected boolean soundedAlarm = false;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.WATCHMAN);
	private String wmImage_base = "watchman";
	
	private String wmImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.WATCHMAN, "watchman_dead.png");
	
	public Watchman(int startX, int startY, ArrayList<GCharacter> npcList, ArrayList<ExtraTile> tileList) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Watchman.STATE_IDLE;
		this.patrolPattern = PatrolPattern.PATROL;
		
		this.npcList = npcList;
		this.tileList = tileList;
		
		this.imagePath = this.getImage();
	}
	
	public Watchman(int startX, int startY) {
		this(startX, startY, new ArrayList<GCharacter>(), new ArrayList<ExtraTile>());
	}
	
	public Watchman(int startX, int startY, ArrayList<GCharacter> npcList, ArrayList<ExtraTile> tileList, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Watchman.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.npcList = npcList;
		this.tileList = tileList;
		
		this.imagePath = this.getImage();
	}
	
	public Watchman(int startX, int startY, PatrolPattern patpat) {
		this(startX, startY, new ArrayList<GCharacter>(), new ArrayList<ExtraTile>(), patpat);
	}

	@Override
	public String getName() {
		return "Watchman";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.wmImage_base;
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
			case Watchman.STATE_IDLE:
				// No extra path
				break;
			case Watchman.STATE_PURSUE:
				statePath = "_PURSUE";
				break;
			case Watchman.STATE_PREP:
				statePath = "_PREP";
				break;
			case Watchman.STATE_ALERTED:
			case Watchman.STATE_ATT:
				statePath = "_ALERT";
				break;
			default:
				System.out.println
					(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
				return GPath.NULL;
		}
		
		// Return the full path
		return (imgPath + hpPath + statePath + ".png");
	}

	@Override
	public String getCorpseImage() {
		return this.wmImage_DEAD;
	}

	@Override
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
	}
	
	@Override
	public void playerInitiate() {
		this.attackPlayer();
	}

	@Override
	public void onDeath() {
		// Play death sound
		SoundPlayer.playWAV(GPath.createSoundPath("Watchman_DEATH.wav"));
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
			case Watchman.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					if(!this.soundedAlarm) {
						// For every NPC in the list, spawn it
						for(GCharacter npc: this.npcList) {
							EntityManager.getInstance().getNPCManager().addPendingCharacter(npc);
						}
						
						for(ExtraTile et: this.tileList) {
							// Get the tile and old tile type
							GameTile tile = GameScreen.getTile(et.xPos, et.yPos);
							TileType oldType = tile.getTileType();
							
							// Exchange the two types
							tile.setTileType(et.tile);
							et.tile = oldType;
						}
						
						// Flag that we've already sounded our alarm once
						this.soundedAlarm = true;
					}
					
					// Play alert sound and change state to pursue player
					SoundPlayer.playWAV(GPath.createSoundPath("Watchman_ALERT.wav"));
					this.state = Watchman.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case Watchman.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = Watchman.STATE_PURSUE;
				break;
			case Watchman.STATE_PURSUE:	
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
				if(((Math.abs(distX) <= 2) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) <= 2))) {
					this.xMarkDir = dx;
					this.yMarkDir = dy;
					this.state = Watchman.STATE_PREP;
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
			case Watchman.STATE_PREP:
				// Retrieve instance of EntityManager
				EntityManager em = EntityManager.getInstance();
				
				// Increment windup count and check to see if we've wound up enough
				this.windupCount += 1;
				if(this.windupCount >= this.windupMax) {
					// Use direction from player to mark squares
					if(Math.abs(this.xMarkDir) > Math.abs(this.yMarkDir)) {
						// Player to left/right
						em.getEffectManager().addEffect(new MusicEffect(this.xPos + this.xMarkDir, this.yPos));
						em.getEffectManager().addEffect(new MusicEffect(this.xPos + this.xMarkDir, this.yPos + 1));
						em.getEffectManager().addEffect(new MusicEffect(this.xPos + this.xMarkDir, this.yPos - 1));
						em.getEffectManager().addEffect(new MusicEffect(this.xPos + (this.xMarkDir*2), this.yPos));
						em.getEffectManager().addEffect(new MusicEffect(this.xPos + (this.xMarkDir*2), this.yPos + 1));
						em.getEffectManager().addEffect(new MusicEffect(this.xPos + (this.xMarkDir*2), this.yPos - 1));
						
						// Attack player if in affected space
						if(((plrX == this.xPos + this.xMarkDir) || (plrX == this.xPos + (this.xMarkDir*2))) &&
								(plrY == this.yPos || plrY == this.yPos - 1 || plrY == this.yPos + 1)) {
							this.playerInitiate();
						}
					} else {
						// Player above/below
						em.getEffectManager().addEffect(new MusicEffect(this.xPos, this.yPos + this.yMarkDir));
						em.getEffectManager().addEffect(new MusicEffect(this.xPos + 1, this.yPos + this.yMarkDir));
						em.getEffectManager().addEffect(new MusicEffect(this.xPos - 1, this.yPos + this.yMarkDir));
						em.getEffectManager().addEffect(new MusicEffect(this.xPos, this.yPos + (this.yMarkDir*2)));
						em.getEffectManager().addEffect(new MusicEffect(this.xPos + 1, this.yPos + (this.yMarkDir*2)));
						em.getEffectManager().addEffect(new MusicEffect(this.xPos - 1, this.yPos + (this.yMarkDir*2)));
						
						// Attack player if in affected space
						if(((plrY == this.yPos + this.yMarkDir) || (plrY == this.yPos + (this.yMarkDir*2))) &&
								(plrX == this.xPos || plrX == this.xPos - 1 || plrX == this.xPos + 1)) {
							this.playerInitiate();
						}
					}
					SoundPlayer.playWAV(GPath.createSoundPath("Watchman_ATTACK.wav"));
					this.state = Watchman.STATE_ATT;
					this.windupCount = 0;
					
				} else {
					// Continue preparing the attack
				}
				break;
			case Watchman.STATE_ATT:
				// Cooldown period for one turn
				this.state = Watchman.STATE_PURSUE;
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
	}

}
