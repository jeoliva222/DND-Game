package items;

import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;

public class LargeMaxPotion extends GItem {

	// Serialization ID
	private static final long serialVersionUID = 372401037959888973L;
	
	private static final String imagePath = GPath.createImagePath(GPath.PICKUP, GPath.CONSUME, "largeMaxing.png");
	
	// Constructor
	public LargeMaxPotion() {
		super("Large Maxing Potion",
				"Drink to increase your maximum health by a lot.",
				LargeMaxPotion.imagePath, 4);
	}

	@Override
	public boolean use() {
		// Increase the player's max health
		EntityManager.getInstance().getPlayer().increaseMaxHP(3);
		
		// Log results
		LogScreen.log("Player drank and felt a lot tougher!", GColors.HEAL);
		
		// Play sound
		SoundPlayer.playWAV(GPath.createSoundPath("Potion_USE.wav"));
		
		return true;
	}
}
