package debuffs;

import gui.GameWindow;

/**
 * Debuff that affects the player by lowering their vision
 * @author joliva
 */
public class BlindDebuff extends Debuff {

	// Serialization ID
	private static final long serialVersionUID = 6491239491998947144L;
	
	// Description
	private static final String DESC = "A crippling blindness reduces your vision to 0";
	
	// Amount of vision the player used to have pre-blindness
	// Used to bump player's vision back to normal on deactivation
	private int oldVision = 0;

	public BlindDebuff(int duration) {
		super(BLINDNESS, DESC, duration);
	}

	@Override
	public void doTurnEffect() {
		// No on-turn effect
	}

	@Override
	public void activate() {
		// Player effect
		if(this.player != null) {
			this.oldVision = this.player.getVision();
			this.player.setVision(0);
			GameWindow.getScreen().refreshTiles();
		}
	}

	@Override
	public void deactivate() {
		// Player effect
		if(this.player != null) {
			this.player.setVision(this.oldVision);
			GameWindow.getScreen().refreshTiles();
		}
	}

}
