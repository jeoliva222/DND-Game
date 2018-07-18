package items;

import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;

public class LargeHealthPotion extends GItem {

	// Serialization ID
	private static final long serialVersionUID = -7416074636320766676L;
	
	
	private static final String imagePath = GPath.createImagePath(GPath.PICKUP, GPath.CONSUME, "largeHealth.png");
	
	// Constructor
	public LargeHealthPotion() {
		super("Large Health Potion",
				"Drink to heal to full health!",
				LargeHealthPotion.imagePath);
	}

	@Override
	public boolean use() {		
		// Heal the player to maximum health
		EntityManager.getPlayer().fullyHeal();
		
		// Log results
		LogScreen.log("Player chugged deeply and recovered to full health!", GColors.HEAL);
		
		// Play sound
		SoundPlayer.playWAV(GPath.createSoundPath("Potion_USE.wav"));
		
		return true;
	}
	
}
