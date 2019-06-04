package characters;

import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.Arrow;

// Class representing 'Elite Arrow Turret' enemy in-game
// Elite Arrow Turrets fire in all four cardinal directions every other time they shoot
public class EliteArrowTurret extends ArrowTurret {
	
	// Serialization ID
	private static final long serialVersionUID = -8809448516995451284L;
	
	// State indicators
	
	private static final int STATE_ACTIVE = 0;
	private static final int STATE_INACTIVE = 1;
	
	//----------------------------
	
	// Other variables
	
	// Marks whether turret should do alt-fire this turn
	private boolean doAltFire = false;
	
	// Separate variable to mark whether turret should always alternate fire
	private boolean alwaysAlt = false;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.ARROW_TURRET);
	private String atImage_base = "elite_arrowturret";

	public EliteArrowTurret(int startX, int startY, int shootX, int shootY, int interval) {
		super(startX, startY, shootX, shootY, interval);
	}
	
	public EliteArrowTurret(int startX, int startY, int shootX, int shootY, int interval, boolean alwaysAlt) {
		super(startX, startY, shootX, shootY, interval);
		this.alwaysAlt = alwaysAlt;
	}
	
	public EliteArrowTurret(int startX, int startY, int shootX, int shootY, int interval, int offset) {
		super(startX, startY, shootX, shootY, interval, offset);
	}
	
	public EliteArrowTurret(int startX, int startY, int shootX, int shootY, int interval, int offset, boolean alwaysAlt) {
		super(startX, startY, shootX, shootY, interval, offset);
		this.alwaysAlt = alwaysAlt;
	}
	
	@Override
	public String getName() {
		return "Elite Arrow Turret";
	}

	@Override
	public String getImage() {
		switch(this.state) {
			case EliteArrowTurret.STATE_INACTIVE:
				return (this.imageDir + this.atImage_base + "_INACTIVE.png");
			case EliteArrowTurret.STATE_ACTIVE:
				if((this.shotCount - 1) >= this.shotMax) {
					return (this.imageDir + this.atImage_base + "_ATT.png");
				} else if(this.shotCount >= this.shotMax) {
					return (this.imageDir + this.atImage_base + "_PREP.png");
				} else {
					return (this.imageDir + this.atImage_base + "_INACTIVE.png");
				}
			default:
				System.out.println
					(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
				return GPath.NULL;
		}
	}
	
	@Override
	public void takeTurn() {
		// If this is dead or the player is dead, don't do anything
		if(!this.isAlive() || !EntityManager.getInstance().getPlayer().isAlive()) {
			// Do nothing
			return;
		}
		
		// Depending on current state, act accordingly
		switch(this.state) {
			case EliteArrowTurret.STATE_INACTIVE:
				// Do nothing because it's inactive
				break;
			case EliteArrowTurret.STATE_ACTIVE:
				// Reset our counter
				if(this.shotCount > this.shotMax) {
					this.shotCount = 0;
				}
				
				// We're active, so shoot arrows periodically
				if(this.shotCount < this.shotMax) {
					// Do nothing, we're on cooldown
				} else {
					// Play arrow firing sound
					SoundPlayer.playWAV(GPath.createSoundPath("arrow_SHOT.wav"));
					
					if(this.doAltFire || this.alwaysAlt) {
						// Fire arrows in all four cardinal directions
						EntityManager.getInstance().getProjectileManager()
							.addProjectile(new Arrow((this.xPos + 1),
												(this.yPos),
												1,
												0, this.getClass()));
						EntityManager.getInstance().getProjectileManager()
							.addProjectile(new Arrow((this.xPos - 1),
											(this.yPos),
											-1,
											0, this.getClass()));
						EntityManager.getInstance().getProjectileManager()
							.addProjectile(new Arrow((this.xPos),
											(this.yPos + 1),
											0,
											1, this.getClass()));
						EntityManager.getInstance().getProjectileManager()
							.addProjectile(new Arrow((this.xPos),
											(this.yPos - 1),
											0,
											-1, this.getClass()));
						
						// Don't do alternate fire next time we shoot
						this.doAltFire = false;
					} else {
						// Fire an arrow in a predetermined direction
						EntityManager.getInstance().getProjectileManager()
							.addProjectile(new Arrow((this.xPos + this.shootX),
													(this.yPos + this.shootY),
													this.shootX,
													this.shootY, this.getClass()));
						
						// Do alternate fire next time we shoot
						this.doAltFire = true;
					}
				}
				
				// Increment our cooldown counter
				this.shotCount += 1;
				break;
			default:
				System.out.println(this.getName() + " couldn't identify its state!");
				break;
		}
	}
	
	// Makes the turret stop shooting
	public void setInactive() {
		this.state = EliteArrowTurret.STATE_INACTIVE;
	}
	
}
