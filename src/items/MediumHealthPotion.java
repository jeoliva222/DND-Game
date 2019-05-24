package items;

import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;

public class MediumHealthPotion extends GItem {

	// Serialization ID
	private static final long serialVersionUID = 6307191897149065141L;
	
	private static final String imagePath = GPath.createImagePath(GPath.PICKUP, GPath.CONSUME, "mediumHealth.png");
	
	// Constructor
	public MediumHealthPotion() {
		super("Medium Health Potion",
				"Drink to heal up to half your maximum health.",
				MediumHealthPotion.imagePath, 4);
	}

	@Override
	public boolean use() {
		// Fetch player's maximum health
		int maxHP = EntityManager.getInstance().getPlayer().getMaxHP();
		
		// Get healing amount
		int healing = maxHP / 2;
		
		// Heal the player
		EntityManager.getInstance().getPlayer().healPlayer(healing, false);
		
		// Log result
		LogScreen.log("Player chugged and recovered "+Integer.toString(healing)
			+" health.", GColors.HEAL);
		
		// Play sound
		SoundPlayer.playWAV(GPath.createSoundPath("Potion_USE.wav"));
		
		return true;
	}
	
}
