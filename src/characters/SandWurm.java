package characters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import effects.DamageIndicator;
import gui.GameScreen;
import gui.InfoScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.MusicNote;
import tiles.Ground;
import tiles.MovableType;
import tiles.TileType;

public class SandWurm extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 4338314430889513276L;
	
	// Modifiers/Statistics

	private int MAX_HP = 10;
	
	private int MIN_DMG = 2;
	private int MAX_DMG = 5;
	
	private double CRIT_CHANCE = 0.1;
	private double CRIT_MULT = 1.4;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_BURROW = 1;
	private static final int STATE_TUNNEL = 2;
	private static final int STATE_PREP = 3;
	private static final int STATE_ATT = 4;
	private static final int STATE_WARP = 5;
	private static final int STATE_PREP_SPIT = 6;
	private static final int STATE_ATT_SPIT = 7;
	
	//----------------------------
	
	// Additional parameters
	
	// Count for determining attack patterns
	protected int attCount = 0;
	
	// Times the Wurm was damaged since its last warp
	protected int dmgCount = 0;
	
	// Direction of attack
	protected int markX = 0;
	protected int markY = 0;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE);
	private String swImage_base = "beanpole";
	
	private String swImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE, "beanpole_dead.png");
	private String swImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE, "beanpole_dead_CRIT.png");

	// Constructors
	public SandWurm(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SandWurm.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.imagePath = this.getImage();
	}
	
	public SandWurm(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SandWurm.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Sandwurm";
	}
	
	@Override
	public String getImage() {
		// TODO : Needs custom sprites
		String imgPath = this.imageDir + this.swImage_base;
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
		case SandWurm.STATE_WARP:
		case SandWurm.STATE_TUNNEL:
			// No image while tunneling
			return GPath.NULL;
		case SandWurm.STATE_IDLE:
			// No extra path
			break;
		case SandWurm.STATE_PREP_SPIT:
		case SandWurm.STATE_BURROW:
		case SandWurm.STATE_PREP:
			statePath = "_PREP";
			break;
		case SandWurm.STATE_ATT_SPIT:
		case SandWurm.STATE_ATT:
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
		if(this.currentHP < -(this.maxHP)) {
			return this.swImage_DEAD_CRIT;
		} else {
			return this.swImage_DEAD;
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
		// TODO : Do nothing for now
	}
	
	// Override that increments a damaged counter on hit
	@Override
	public boolean damageCharacter(int damage) {
		this.currentHP = this.currentHP - damage;
		InfoScreen.setNPCFocus(this);
		this.attCount += 1;
		return this.isAlive();
	}
	
	@Override
	// Moves the character: Returns false if blocked, Returns true if moved successfully
	public boolean moveCharacter(int dx, int dy) {
		
		// Check on collisions for other characters 
		for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
			if((this.xPos + dx) == npc.xPos && (this.yPos + dy) == npc.yPos) {
				return false;
			}
		}
		
		// Then check for barriers, out-of-bounds, and immovable spaces
		TileType tt;
		try {
			// Try to get the TileType of the GameTile
			tt = GameScreen.getTile(this.xPos + dx, this.yPos + dy).getTileType();
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		
		// If NPC can't move here, return without moving
		if(!(tt instanceof Ground)) {
			return false;
		}
		
		// Otherwise, we can update the positions
		
		// Last position
		this.lastX = this.xPos;
		this.lastY = this.yPos;
		
		// New position
		this.xPos += dx;
		this.yPos += dy;
		
		return true;
	}

	@Override
	public void takeTurn() {
		// Fetch the player for easy reference
		Player player = EntityManager.getPlayer();
		
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
		
		// Initialize Randomizer
		Random r;
		
		switch(this.state) {
			case SandWurm.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("beep_ALERT.wav"));
					this.state = SandWurm.STATE_BURROW;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case SandWurm.STATE_BURROW:
				// Take one turn to burrow into the ground -------
				
				// Based on damage taken, decide to warp away or not
				r = new Random();
				if((this.attCount != 0) && ((this.attCount) >= (r.nextInt(4)))) {
					System.out.println("Warping away");
					
					// Warp away from the player
					this.warpWurm(plrX, plrY);
					
					this.state = SandWurm.STATE_WARP;
				} else {
					this.state = SandWurm.STATE_TUNNEL;
				}
				break;
			case SandWurm.STATE_TUNNEL:
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
					this.state = SandWurm.STATE_PREP;
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
					
					// Recalculate distance after moving
					distX = plrX - this.xPos;
					distY = plrY - this.yPos;
					
					// Check if we're next to the player after moving
					// Attack if we are. Otherwise, do nothing
					if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
						// Relative movement direction (Initialize at 0)
						dx = 0;
						dy = 0;
						
						// Recalculate relative movement directions
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
						
						// Mark direction to attack next turn
						this.markX = dx;
						this.markY = dy;
						
						// Change states
						this.state = SandWurm.STATE_PREP;
					}
				}
				break;
			case SandWurm.STATE_PREP:	
				// Mark tile with damage indicator
				EntityManager.getEffectManager().addEffect(new DamageIndicator(this.xPos + this.markX, this.yPos + this.markY));
				
				// Attack in marked direction
				if((this.xPos + this.markX) == plrX && (this.yPos + this.markY) == plrY)
					this.playerInitiate();
				this.state = SandWurm.STATE_ATT;
				break;
			case SandWurm.STATE_ATT:
				// Cooldown period after attack
				this.state = SandWurm.STATE_BURROW;
				break;
			case SandWurm.STATE_WARP:
				// Reset attack count and go to prep for spit attack 
				this.attCount = 0;
				this.state = SandWurm.STATE_PREP_SPIT;
				break;
			case SandWurm.STATE_PREP_SPIT:
				// Relative movement direction (Initialize at 0)
				dx = 0;
				dy = 0;
				
				// Recalculate relative movement directions
				if(Math.abs(distX) >= Math.abs(distY)) {
					// Make projectile go x-wise
					if(distX > 0) {
						dx = 1;
					} else if (distX < 0) {
						dx = -1;
					}
				} else {
					// Make projectile go y-wise
					if(distY > 0) {
						dy = 1;
					} else if (distY < 0) {
						dy = -1;
					}
				}
				
				// Spit at the player
				EntityManager.getProjectileManager()
				.addProjectile(new MusicNote((this.xPos + dx),
										(this.yPos + dy),
										dx,
										dy, this));
				
				// Change state to spit attack
				this.state = SandWurm.STATE_ATT_SPIT;
				break;
			case SandWurm.STATE_ATT_SPIT:
				// Cooldown for one turn after spitting
				this.state = SandWurm.STATE_BURROW;
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}

	}
	
	private boolean warpWurm(int plrX, int plrY) {
		// Initialize list of populated coordinates
		ArrayList<Dimension> badCoords = new ArrayList<Dimension>();
		
		// Populate list of bad coordinates to warp to
		for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
			badCoords.add(new Dimension(npc.getXPos(), npc.getYPos()));
		}

		// Initialize Randomizer
		Random r = new Random();
		
		// Initialize potential warp coordinates
		Dimension[] warpSpots = new Dimension[] {
				new Dimension(plrX + (3 + r.nextInt(2)), plrY),
				new Dimension(plrX - (3 + r.nextInt(2)), plrY),
				new Dimension(plrX, plrY + (3 + r.nextInt(2))),
				new Dimension(plrX, plrY - (3 + r.nextInt(2)))
		};
		
		// Shuffle the potential warp coordinates
		for(int i = 3; i > 0; i--) {
			// Randomize swap index
			int shuffCoord = r.nextInt(i + 1);
			
			// Swap the spots
			Dimension temp = warpSpots[i];
			warpSpots[i] = warpSpots[shuffCoord];
			warpSpots[shuffCoord] = temp;
		}
		
		// Rotate through all coordinates
		for(Dimension coord : warpSpots) {
			
			// Define X and Y
			int newX = coord.width;
			int newY = coord.height;
			
			// Then check for barriers, out-of-bounds, and immovable spaces
			TileType tt;
			try {
				// Try to get the TileType of the GameTile
				tt = GameScreen.getTile(newX, newY).getTileType();
			} catch (IndexOutOfBoundsException e) {
				continue;
			}
			
			// If NPC can't move here, return without moving
			if(!(tt instanceof Ground)) {
				continue;
			}
			
			// Check for collisions with other NPCs
			for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
				if((newX) == npc.xPos && (newY) == npc.yPos) {
					continue;
				}
			}
			
			// Finally, check for collision with the player
			Player player = EntityManager.getPlayer();
			if((newX) == player.getXPos() && (newY) == player.getYPos()) {
				continue;
			}
			
			// Otherwise, we can update the positions --------
			
			// Last position
			this.lastX = this.xPos;
			this.lastY = this.yPos;
			
			// New position
			this.xPos = newX;
			this.yPos = newY;
			
			return true;
		}
		
		// If none of the coordinates worked, return false
		return false;
	}
	
}
