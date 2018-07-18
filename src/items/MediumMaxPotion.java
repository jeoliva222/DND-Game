package items;

import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;

public class MediumMaxPotion extends GItem {

	// Serialization ID
	private static final long serialVersionUID = -6917049863849864272L;
	
	private static final String imagePath = GPath.createImagePath(GPath.PICKUP, GPath.CONSUME, "mediumMaxing.png");
	
	// Constructor
	public MediumMaxPotion() {
		super("Medium Maxing Potion",
				"Drink to increase your maximum health.",
				MediumMaxPotion.imagePath);
	}

	@Override
	public boolean use() {
		// Increase the player's max health
		EntityManager.getPlayer().increaseMaxHP(4);
		
		// Log results
		LogScreen.log("Player drank and felt somewhat tougher!", GColors.HEAL);
		
		// Play sound
		SoundPlayer.playWAV(GPath.createSoundPath("Potion_USE.wav"));
		
		return true;
	}
}
