package characters;

import gui.GameWindow;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;

// 'Checkpoint' enemy. Break to save the game.
public class SaveCrystal extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -7013427747512968294L;

	public SaveCrystal(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = 1;
		this.currentHP = 1;
	}

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return GPath.createImagePath(GPath.ENEMY, GPath.SAVE_CRYSTAL, "crystal_full.png");
	}

	@Override
	public String getCorpseImage() {
		// TODO Auto-generated method stub
		return GPath.createImagePath(GPath.ENEMY, GPath.SAVE_CRYSTAL, "crystal_dead.png");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Save Crystal";
	}

	@Override
	public void onDeath() {
		// Play sound
		SoundPlayer.playWAV(GPath.createSoundPath("crystal_break.wav"));
		
		// Fetch player's maximum health
		int maxHP = EntityManager.getInstance().getPlayer().getMaxHP();
		
		// Get healing amount
		int healing = maxHP / 4;
		
		// Heal the player 25% of their max health
		EntityManager.getInstance().getPlayer().healPlayer(healing, false);
		
		// Log results
		LogScreen.log("Player recovered "+Integer.toString(healing)
			+" health from the save crystal.", GColors.HEAL);
		
		// Trigger a flag to save the game
		GameWindow.shouldSave = true;
	}
	
	@Override
	public void playerInitiate() {
		// Doesn't initiate player
	}

	@Override
	public void takeTurn() {
		// Does nothing
	}

	@Override
	public void populateMoveTypes() {
		// Can't move
	}

}
