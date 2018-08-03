package items;

import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;

// Class for small max-health increasing potion
public class SmallMaxPotion extends GItem {

	// Serialization ID
	private static final long serialVersionUID = -8972302280479041563L;
	
	private static final String imagePath = GPath.createImagePath(GPath.PICKUP, GPath.CONSUME, "smallMaxing.png");
	
	// Constructor
	public SmallMaxPotion() {
		super("Small Maxing Potion",
				"Drink to increase your maximum health by a bit.",
				SmallMaxPotion.imagePath);
	}

	@Override
	public boolean use() {
		// Increase the player's max health
		EntityManager.getInstance().getPlayer().increaseMaxHP(2);
		
		// Log results
		LogScreen.log("Player drank and felt a bit tougher!", GColors.HEAL);
		
		// Play sound
		SoundPlayer.playWAV(GPath.createSoundPath("Potion_USE.wav"));
		
		return true;
	}
}
