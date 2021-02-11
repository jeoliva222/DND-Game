package characters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import effects.DamageIndicator;
import gui.GameScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.SandwurmSpit;
import tiles.MovableType;
import tiles.TileType;

/**
 * Class that represents the Sand Wurm enemy 
 * @author jeoliva
 */
public class SandWurm extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 4338314430889513276L;
	
	// Modifiers/Statistics

	private static int MAX_HP = 10;
	
	private static int MIN_DMG = 2;
	private static int MAX_DMG = 5;
	
	private static double CRIT_CHANCE = 0.1;
	private static double CRIT_MULT = 1.4;
	
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
	
	// Times the Wurm was damaged since its last warp
	// Determines attack patterns
	protected int dmgCount = 0;
	
	// Direction of attack
	protected int markX = 0;
	protected int markY = 0;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SANDWURM);
	private static String swImage_base = "sandwurm";
	
	private static String swImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.SANDWURM, "sandwurm_dead.png");
	private static String swImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.SANDWURM, "sandwurm_dead.png");

	// Constructors
	public SandWurm(int startX, int startY) {
		this(startX, startY, PatrolPattern.STATIONARY);
	}
	
	public SandWurm(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SandWurm.STATE_IDLE;
		this.patrolPattern = patpat;
	}
	
	public String getName() {
		return "Sandwurm";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + swImage_base);
		String hpPath = "";
		String statePath = "";
		
		if (currentHP > (maxHP / 2)) {
			hpPath = "_full";
		} else if (currentHP > 0) {
			hpPath = "_fatal";
		} else {
			return GPath.NULL;
		}
		
		switch (state) {
			case SandWurm.STATE_WARP:
			case SandWurm.STATE_TUNNEL:
				// No image while tunneling
				return GPath.NULL;
			case SandWurm.STATE_IDLE:
				// No extra path
				break;
			case SandWurm.STATE_PREP_SPIT:
				statePath = "_PREP_SPIT";
				break;
			case SandWurm.STATE_BURROW:
				statePath = "_ALERT";
				break;
			case SandWurm.STATE_PREP:
				statePath = "_PREP_BITE";
				break;
			case SandWurm.STATE_ATT_SPIT:
				statePath = "_ATT_SPIT";
				break;
			case SandWurm.STATE_ATT:
				statePath = "_ATT_BITE";
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if (currentHP < -(maxHP / 2)) {
			return swImage_DEAD_CRIT;
		} else {
			return swImage_DEAD;
		}
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"), getXPos(), getYPos());
		attackPlayer();
	}
	
	@Override
	public void onDeath() {
		// Play death sound
		SoundPlayer.playWAV(GPath.createSoundPath("Sandwurm_DEATH.wav"), -3.0f, getXPos(), getYPos());
	}
	
	// Override that increments a damaged counter on hit
	// Damage counter indicates probability to warp away from player
	@Override
	public boolean damageCharacter(int damage) {
		boolean isAlive = super.damageCharacter(damage);
		this.dmgCount += 1;
		return isAlive;
	}
	
	// Override that resets a few extra parameters
	@Override
	public void resetParams() {
		this.dmgCount = 0;
		this.canFocus = true;
	}

	@Override
	public void takeTurn() {
		// Fetch the player for easy reference
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
		
		// Declare Randomizer
		Random r;
		
		switch (state) {
			case SandWurm.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("Sandwurm_ALERT.wav"), getXPos(), getYPos());
					this.state = SandWurm.STATE_BURROW;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case SandWurm.STATE_BURROW:
				// Take one turn to burrow into the ground -------
				
				// Disable focusing in on Sandwurm while tunneling/warping
				this.canFocus = false;
				
				// Based on damage taken, decide to warp away or not
				r = new Random();
				if ((dmgCount != 0) && ((dmgCount) >= (r.nextInt(4)))) {
					// Warp away from the player
					warpWurm(plrX, plrY);
					
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
				
				// Change state to prep if next to player
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					// Mark direction to attack next turn
					this.markX = dx;
					this.markY = dy;
					
					// Enable focusing in on Sandwurm
					this.canFocus = true;
					
					// Play dig sound
					playDigSound(0);
					
					// Change states
					this.state = SandWurm.STATE_PREP;
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
					
					// Recalculate distance after moving
					distX = (plrX - xPos);
					distY = (plrY - yPos);
					
					// Play dig sound (use player distance)
					playDigSound(Math.abs(distX) + Math.abs(distY));
					
					// Check if we're next to the player after moving
					// Attack if we are. Otherwise, do nothing
					if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
						// Relative movement direction (Initialize at 0)
						dx = 0;
						dy = 0;
						
						// Recalculate relative movement directions
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
						
						// Mark direction to attack next turn
						this.markX = dx;
						this.markY = dy;
						
						// Enable focusing in on Sandwurm
						this.canFocus = true;
						
						// Change states
						this.state = SandWurm.STATE_PREP;
					}
				}
				break;
			case SandWurm.STATE_PREP:	
				// Mark tile with damage indicator
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + markX, yPos + markY));
				
				// Play attack sound
				playAttackSound();
				
				// Attack in marked direction
				if ((xPos + markX) == plrX && (yPos + markY) == plrY) {
					playerInitiate();
				}
				this.state = SandWurm.STATE_ATT;
				break;
			case SandWurm.STATE_ATT:
				// Cooldown period after attack
				this.state = SandWurm.STATE_BURROW;
				break;
			case SandWurm.STATE_WARP:
				// Reset attack count and go to prep for spit attack 
				this.dmgCount = 0;
				
				// Enable focusing in on Sandwurm
				this.canFocus = true;
				
				this.state = SandWurm.STATE_PREP_SPIT;
				break;
			case SandWurm.STATE_PREP_SPIT:
				// Relative movement direction (Initialize at 0)
				dx = 0;
				dy = 0;
				
				// Recalculate relative movement directions
				if (Math.abs(distX) >= Math.abs(distY)) {
					// Make projectile go x-wise
					if (distX > 0) {
						dx = 1;
					} else if (distX < 0) {
						dx = -1;
					}
				} else {
					// Make projectile go y-wise
					if (distY > 0) {
						dy = 1;
					} else if (distY < 0) {
						dy = -1;
					}
				}
				
				// Spit at the player
				EntityManager.getInstance().getProjectileManager()
					.addProjectile(new SandwurmSpit((xPos + dx), (yPos + dy), dx, dy, getClass()));
				
				// Play spitting sound
				SoundPlayer.playWAV(GPath.createSoundPath("Sandwurm_SPIT.wav"), -5.0f, getXPos(), getYPos());
				
				// Change state to spit attack
				this.state = SandWurm.STATE_ATT_SPIT;
				break;
			case SandWurm.STATE_ATT_SPIT:
				// Cooldown for one turn after spitting
				this.state = SandWurm.STATE_BURROW;
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}

	}
	
	/**
	 * Warps to a randomized location around the destination coordinate
	 * @param destX Destination X-position
	 * @param destY Destination Y-position
	 * @return True if successfully warped | False if not
	 */
	private boolean warpWurm(int destX, int destY) {
		// Initialize list of populated coordinates
		ArrayList<Dimension> badCoords = new ArrayList<Dimension>();
		
		// Populate list of bad coordinates to warp to
		for (GCharacter npc : EntityManager.getInstance().getNPCManager().getCharacters()) {
			badCoords.add(new Dimension(npc.getXPos(), npc.getYPos()));
		}

		// Initialize Randomizer
		Random r = new Random();
		
		// Initialize potential warp coordinates
		Dimension[] warpSpots = new Dimension[] {
				new Dimension(destX + (3 + r.nextInt(2)), destY),
				new Dimension(destX - (3 + r.nextInt(2)), destY),
				new Dimension(destX, destY + (3 + r.nextInt(2))),
				new Dimension(destX, destY - (3 + r.nextInt(2)))
		};
		
		// Shuffle the potential warp coordinates
		for (int i = 3; i > 0; i--) {
			// Randomize swap index
			int shuffCoord = r.nextInt(i + 1);
			
			// Swap the spots
			Dimension temp = warpSpots[i];
			warpSpots[i] = warpSpots[shuffCoord];
			warpSpots[shuffCoord] = temp;
		}
		
		// Rotate through all coordinates
		for (Dimension coord : warpSpots) {
			
			// Define X and Y
			int newX = coord.width;
			int newY = coord.height;
			
			// Check for collisions with other NPCs
			boolean collisionFlag = false;
			for (Dimension dim : badCoords) {
				if ((newX) == dim.width && (newY) == dim.height) {
					// If we collided with an NPC's position, set a collision flag
					collisionFlag = true;
					break;
				}
			}
			
			// If we collided with another NPC, then don't move to this spot
			if (collisionFlag) {
				continue;
			}
			
			// Then check for barriers, out-of-bounds, and immovable spaces
			TileType tt;
			try {
				// Try to get the TileType of the GameTile
				tt = GameScreen.getTile(newX, newY).getTileType();
			} catch (IndexOutOfBoundsException e) {
				continue;
			}
			
			// If NPC can't move here, don't move to this spot
			if (!canMove(tt.getMovableType())) {
				continue;
			}
			
			// Finally, check for collision with the player
			Player player = EntityManager.getInstance().getPlayer();
			if ((newX) == player.getXPos() && (newY) == player.getYPos()) {
				continue;
			}
			
			// Otherwise, we can update the positions --------
			
			// Last position
			this.lastX = xPos;
			this.lastY = yPos;
			
			// New position
			this.xPos = newX;
			this.yPos = newY;
			
			return true;
		}
		
		// If none of the coordinates worked, return false
		return false;
	}
	
	/**
	 * Plays one of three digging sound variations.
	 * Incorporates its own set of distance-based volume logic to make the 
	 * digging exponentially louder as the enemy gets closer.
	 */
	private void playDigSound(int totalDistance) {
		float volume = -30.0f;
		if (totalDistance <= 1) {
			volume = -5.0f;
		} else if (totalDistance <= 2) {
			volume = -10.0f;
		} else if (totalDistance <= 4) {
			volume = -20.0f;
		}
			
		Random r = new Random();
		int whichSound = (r.nextInt(3) + 1);
		SoundPlayer.playWAV(GPath.createSoundPath("Dig" + whichSound +".wav"), volume, getXPos(), getYPos());
	}
	
	/**
	 * Plays one of two attack sound variations.
	 */
	private void playAttackSound() {
		boolean doGrunt = new Random().nextBoolean();
		SoundPlayer.playWAV(GPath.createSoundPath("Sandwurm_ATT" + (doGrunt ? "_GRUNT" : "") + ".wav"), -3.0f, getXPos(), getYPos());
	}
	
}
