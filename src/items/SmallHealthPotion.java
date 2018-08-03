package items;

import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;

// Class for a small health potion
public class SmallHealthPotion extends GItem {

	// Serialization ID
	private static final long serialVersionUID = 7732362576847960769L;
	
	private static final String imagePath = GPath.createImagePath(GPath.PICKUP, GPath.CONSUME, "smallHealth.png");
	
	// Constructor
	public SmallHealthPotion() {
		super("Small Health Potion",
				"Drink to heal up to a quarter of your maximum health.",
				SmallHealthPotion.imagePath);
	}

	@Override
	public boolean use() {
		// Fetch player's maximum health
		int maxHP = EntityManager.getInstance().getPlayer().getMaxHP();
		
		// Get healing amount
		int healing = maxHP / 4;
		
		// Heal the player
		EntityManager.getInstance().getPlayer().healPlayer(healing, false);
		
		// Log results
		LogScreen.log("Player drank and recovered "+Integer.toString(healing)
			+" health.", GColors.HEAL);
		
		// Play sound
		SoundPlayer.playWAV(GPath.createSoundPath("Potion_USE.wav"));
		
		return true;
	}

}
