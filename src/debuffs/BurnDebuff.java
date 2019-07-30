package debuffs;

import gui.GameScreen;
import gui.LogScreen;
import helpers.GColors;
import tiles.MovableType;
import tiles.TileType;

/**
 * Debuff that affects the characters by damaging them each turn.
 * @author joliva
 */
public class BurnDebuff extends Buff {
	
	// Serialization ID
	private static final long serialVersionUID = 7916587426629061605L;

	// Description
	private static final String DESC = "Your flesh is being charred to a crisp, resulting in damage each turn.";
	
	// Burn damage done each turn (Default is 1 damage per turn)
	private int burnDamage = 1;

	public BurnDebuff(int duration) {
		super(BURNING, DESC, duration);
		this.isDebuff = true;
	}
	
	public BurnDebuff(int duration, int burnDamage) {
		super(BURNING, DESC, duration);
		this.burnDamage = burnDamage;
		this.isDebuff = true;
	}

	@Override
	public void doTurnEffect() {
		// Player effect
		if(this.player != null) {
			// Fetch player tile to check for water
			TileType tt = null;
			try {
				// Try to get the TileType of the GameTile
				tt = GameScreen.getTile(this.player.getXPos(), this.player.getYPos()).getTileType();
			} catch (IndexOutOfBoundsException e) {
				// Do nothing
			}
			
			if(tt != null && tt.getMovableType() == MovableType.WATER) {
				// Snuff the flames and deal no damage
				this.duration = 0;
			} else {
				// Otherwise, burn the player
				this.player.damagePlayer(this.burnDamage);
				LogScreen.log(("Player burnt for " + this.burnDamage + " damage."), GColors.DAMAGE);
			}
		}
		
		// Character effect
		if(this.npc != null) {
			// Fetch NPC tile to check for water
			TileType tt = null;
			try {
				// Try to get the TileType of the GameTile
				tt = GameScreen.getTile(this.npc.getXPos(), this.npc.getYPos()).getTileType();
			} catch (IndexOutOfBoundsException e) {
				// Do nothing
			}
			
			if(tt != null && tt.getMovableType() == MovableType.WATER) {
				// Snuff the flames and deal no damage
				this.duration = 0;
			} else {
				// Otherwise, burn the NPC
				this.npc.damageCharacter(this.burnDamage);
				LogScreen.log((this.npc.getName() + " burnt for " + this.burnDamage + " damage."), GColors.ATTACK);
			}
		}
	}

	@Override
	public void activate() {
		// Nothing on activate
		
	}

	@Override
	public void deactivate() {
		// Nothing on deactivate
	}

}
