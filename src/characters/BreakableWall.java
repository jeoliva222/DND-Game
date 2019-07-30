package characters;

import debuffs.Buff;
import helpers.GPath;
import managers.EntityManager;

public class BreakableWall extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -6574224379241241678L;
	
	// Modifiers/Statistics

	private int MAX_HP = 6;
	
	private int MIN_DMG = 0;
	private int MAX_DMG = 0;
	
	private double CRIT_CHANCE = 0.0;
	private double CRIT_MULT = 1.0;
	
	public BreakableWall(int startX, int startY) {
		this(startX, startY, 10);
		this.maxHP = MAX_HP;
		this.currentHP = MAX_HP;
	}
	
	public BreakableWall(int startX, int startY, int maxHP) {
		super(startX, startY);
		
		this.maxHP = maxHP;
		this.currentHP = maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.canFocus = false;
	}
	
	@Override
	public String getImage() {
		String basePath = GPath.createImagePath(GPath.ENEMY, GPath.BREAKABLE_WALL);
		String entityPath = "_breakablewall";
		String hpPath;
		
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = null;
			return GPath.NULL;
		}
		
		// Breakable wall image changes based on which area we are in
		String areaPath = EntityManager.getInstance().getActiveArea().getTheme();
		areaPath = areaPath.substring(0, (areaPath.length() - 1));
		
		// Return full path
		return basePath + areaPath + entityPath + hpPath + ".png";
	}

	@Override
	public String getCorpseImage() {
		return GPath.NULL;
	}

	@Override
	public String getName() {
		return "Breakable Wall";
	}

	@Override
	public void playerInitiate() {
		// Nothing here
	}

	@Override
	public void takeTurn() {
		// Do nothing, you're a wall
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
