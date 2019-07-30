package characters;

import debuffs.Buff;
import gui.LogScreen;
import helpers.GPath;

public class Signpost extends GCharacter {
	
	// Serialization ID
	private static final long serialVersionUID = -4512592243361683238L;

	// Modifiers/Statistics

	private int MAX_HP = 10;
	
	private int ARMOR_VAL = 100;
	
	private int MIN_DMG = 0;
	private int MAX_DMG = 0;
	
	private double CRIT_CHANCE = 0.0;
	private double CRIT_MULT = 1.0;
	
	
	//-----------------------
	
	// Message that reads when the player hits the signpost
	private String message = "";
	
	// Constructor
	public Signpost(int startX, int startY, String imageStr, String message) {
		super(startX, startY);
		
		// Set health and armor attributes
		this.maxHP = MAX_HP;
		this.currentHP = MAX_HP;
		this.armor = ARMOR_VAL;
		
		// Set damage attributes
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		// Can't focus on the signpost
		this.canFocus = false;
		
		// Signpost is an Interactable
		this.isInteractable = true;
		
		// Set the signpost image
		this.imagePath = imageStr;
		
		// Set the signpost message
		this.message = message;
	}
	
	@Override
	public String getImage() {
		// Return full path
		return GPath.createImagePath(GPath.ENEMY, GPath.SIGNPOST, this.imagePath);
	}

	@Override
	public boolean damageCharacter(int damage) {
		// Log the signpost's message to the Logscreen
		LogScreen.log("\"" + this.message + "\"");
		return this.isAlive();
	}
	
	@Override
	public String getCorpseImage() {
		return GPath.NULL;
	}

	@Override
	public String getName() {
		return "Readable Object";
	}

	@Override
	public void playerInitiate() {
		// Nothing here
	}

	@Override
	public void takeTurn() {
		// Do nothing, you're a signpost
	}
	
	@Override
	public void addBuff(Buff debuff) {
		// Don't add the buff
		return;
	}

	@Override
	public void onDeath() {
		// Do nothing
	}

	@Override
	public void populateMoveTypes() {
		// None exist
	}

}
