package characters.special;

import buffs.Buff;
import characters.GCharacter;
import gui.GameScreen;
import gui.LogScreen;
import helpers.GPath;
import tiles.AltGround;

/**
 * Class representing special Skin Altar entity which
 * opens the Museum area entrance upon usage of the Soft Skin item
 * @author jeoliva
 */
public class SkinAltar extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 1510534031287530649L;

	// Modifiers/Statistics

	private static int MAX_HP = 10;
	
	private static int ARMOR_VAL = 100;
	
	private static int MIN_DMG = 0;
	private static int MAX_DMG = 0;
	
	private static double CRIT_CHANCE = 0.0;
	private static double CRIT_MULT = 1.0;
	
	//-----------------------
	
	// Message that reads when the player hits the altar
	private String message = "Offer up precious skin";
	
	// Flag indicating whether skin was placed or not
	private boolean skinPlaced = false;
	
	//-----------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SIGNPOST);
	private static String imageBase = "skin_altar";
	
	// Constructor
	public SkinAltar(int startX, int startY) {
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
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + imageBase);
		
		// Check if skin is placed or not
		if (skinPlaced) {
			imgPath += "_on";
		} else {
			imgPath += "_off";
		}
		
		return (imgPath + ".png");
	}
	
	@Override
	public void addBuff(Buff debuff) {
		// Don't add the buff
		return;
	}

	@Override
	public boolean damageCharacter(int damage) {
		// Log the signpost's message to the Log Screen
		LogScreen.log("\"" + message + "\"");
		return isAlive();
	}
	
	// Used by player when skin item is placed on altar
	public void placeSkin() {
		if (!skinPlaced) {
			this.skinPlaced = true;
			this.message = "The museum awaits...";
			
			// Open the Museum
			GameScreen.getTile(2, 9).setTileType(new AltGround());
			GameScreen.getTile(3, 9).setTileType(new AltGround());
			GameScreen.getTile(4, 9).setTileType(new AltGround());
		}
	}
	
	@Override
	public String getCorpseImage() {
		return GPath.NULL;
	}

	@Override
	public String getName() {
		return "Skin Altar";
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
	public void onDeath() {
		// Do nothing
	}

	@Override
	public void populateMoveTypes() {
		// None exist
	}
	
}
