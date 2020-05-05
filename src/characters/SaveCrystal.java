package characters;

import characters.allies.Player;
import buffs.Buff;
import gui.GameWindow;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;

/**
 * Class representing the Save Crystal entity,
 * which can be broken to save the player's game
 * @author jeoliva
 */
public class SaveCrystal extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -7013427747512968294L;
	
	// Modifiers/Statistics
	
	private static int MAX_HP = 1;
	
	//----------------------------

	public SaveCrystal(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = MAX_HP;
	}

	@Override
	public String getImage() {
		return GPath.createImagePath(GPath.ENEMY, GPath.SAVE_CRYSTAL, "crystal_full.png");
	}

	@Override
	public String getCorpseImage() {
		return GPath.createImagePath(GPath.ENEMY, GPath.SAVE_CRYSTAL, "crystal_dead.png");
	}

	@Override
	public String getName() {
		return "Save Crystal";
	}
	
	@Override
	public void addBuff(Buff debuff) {
		// Don't add the buff
		return;
	}

	@Override
	public void onDeath() {
		// Play break sound
		SoundPlayer.playWAV(GPath.createSoundPath("crystal_break.wav"));
		
		// Fetch reference to player
		Player player = EntityManager.getInstance().getPlayer();
		
		// Fetch player's maximum health
		int maxHP = player.getMaxHP();
		
		// Get healing amount
		int healing = (maxHP / 4);
		
		// Heal the player 25% of their max health
		player.healPlayer(healing, false);
		
		// Log results
		LogScreen.log("Player recovered " + Integer.toString(healing)
			+ " health from the save crystal.", GColors.HEAL);
		
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
