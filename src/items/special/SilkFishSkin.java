package items.special;

import characters.GCharacter;
import characters.special.SkinAltar;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import items.GItem;
import managers.EntityManager;

public class SilkFishSkin extends GItem {

	// Serialization ID
	private static final long serialVersionUID = -4711121752022658482L;
	
	private static final String imagePath = GPath.createImagePath(GPath.ENEMY, GPath.SILKFISH, "silkfish_dead.png");
	
	// Constructor
	public SilkFishSkin() {
		super("Soft Skin",
				"Beautiful skin, cold to the touch. A worthy offering.",
				imagePath, 1);
		
		// We don't want this to be discarded
		this.isDiscardable = false;
	}
	
	@Override
	public boolean use() {
		// Get player's coordinates on the screen
		int plrX = EntityManager.getInstance().getPlayer().getXPos();
		int plrY = EntityManager.getInstance().getPlayer().getYPos();
		
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		boolean usedSkin = false;
		for(GCharacter npc : em.getNPCManager().getCharacters()) {
			// If not the altar, then keep looking
			if (!(npc instanceof SkinAltar)) {
				continue;
			}
			
			SkinAltar altar = (SkinAltar) npc;
			
			// Get relative location to player
			int distX = plrX - npc.getXPos();
			int distY = plrY - npc.getYPos();
			
			if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
					((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
				usedSkin = true;
				altar.placeSkin();
				SoundPlayer.playWAV(GPath.createSoundPath("SmallButton_PRESS.wav"));
				LogScreen.log("You placed the skin...", GColors.ITEM);
				break;
			}
		}
		
		// Return the success/failure of using the skin
		return usedSkin;
	}
	
}
