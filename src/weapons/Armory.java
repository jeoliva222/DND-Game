package weapons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import helpers.GPath;
import weapons.special.Injector;
import weapons.special.KingStaff;
import weapons.special.VenomGun;

/** 
 * Class where all weapon definitions are stored
 */
public class Armory {
	
	// Fist Weapons -----------------------------
	//
	
	// Bare fists
	public static final Fists bareFists = new Fists("Bare Fists", 1, 1, 0.00, 1.0, 2.0,
			"WEAPON (Fist): Slap'em!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "bareFists.png"));
	
	// Padded Fists (Caestus)
	public static final Fists caestus = new Fists("Caestus", 1, 2, 0.00, 1.0, 2.5, 
			"WEAPON (Fist): Studded with iron rivets. Charge attack to dish out extra pain!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "caestus.png"));
	
	// Cactus Claws
	public static final Fists cactusClaws = new Fists("Cactus Claws", 2, 2, 0.00, 1.0, 2.5, 
			"WEAPON (Fist): Claws studded with cactus thorns. Gets consistent results.",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "cactusClaws.png"));
	
	// Brick Pads
	public static final Fists brickPads = new Fists("Brick Pads", 2, 3, 0.00, 1.0, 2.5, 
			"WEAPON (Fist): Sturdy bricks with hand wraps. Packs a punch.",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "caestus.png"));

	// Sword Weapons ----------------------------
	//
	
	// Lowly Broken Sword
	public static final Sword brokenSword = new Sword("Broken Sword", 1, 2, 0.1, 2.0, 1.0, 
			"WEAPON (Sword): Equip this with 'Enter'! Charge attack to slash multiple targets.",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "brokenSword.png"));
	
	// Iron Sword
	public static final Sword ironSword = new Sword("Iron Sword", 1, 3, 0.1, 2.0, 1.0, 
			"WEAPON (Sword): A solid choice, although it could use sharpening.",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "ironSword.png"));
	
	// Rusted Sabre
	public static final Sword rustedSabre = new Sword("Rusted Sabre", 1, 4, 0.1, 1.8, 1.0, 
			"WEAPON (Sword): A partially rusted antique. The blade is quite sharp in a few places.",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "rustedSabre.png"));
	
	// Steel Sword
	public static final Sword steelSword = new Sword("Steel Sword", 1, 5, 0.1, 1.8, 1.0, 
			"WEAPON (Sword): A well-sharpened sturdy sword.",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "ironSword.png"));
	
	// Special 'D20' Sword
	public static final Sword luckSword = new Sword("D20 Sword", 1, 20, 0.01, 5.0, 1.0, 
			"WEAPON (Sword): A roll of the dice...",
			GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png"));
	
	// Spear Weapons -----------------------------
	//
	
	// Long Stick Spear
	public static final Spear longStick = new Spear("Long Stick", 1, 2, 0.15, 1.5, 1.0, 
			"WEAPON (Spear): A pokey stick. Charge attack for a ranged stab!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "longStick.png"));
	
	// Iron Spear
	public static final Spear ironSpear = new Spear("Iron Spear", 1, 3, 0.15, 1.4, 1.0, 
			"WEAPON (Spear): Keep them at bay with this!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "ironSpear.png"));
	
	// Ceremonial Spear
	public static final Spear ceremonialSpear = new Spear("Ceremonial Spear", 2, 3, 0.15, 1.75, 1.0, 
			"WEAPON (Spear): It feels wrong to use this.",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "ceremonialSpear.png"));
	
	// Fishing Hook Spear
	public static final Spear fishingHook = new Spear("Fishing Hook", 2, 4, 0.15, 1.5, 1.0, 
			"WEAPON (Spear): A brutal looking hook mounted at the end of a long pole.",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "ironSpear.png"));
	
	// Ministerial Staff Spear
	public static final Spear ministerSpear = new Spear("Ministerial Staff", 3, 6, 0.15, 1.5, 1.2, 
			"WEAPON (Spear): An odd eye watches at you from the top of this staff.",
			GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png"));
	
	// Dagger Weapons ------------------------------
	//
	
	// Glass Shard Dagger
	public static final Dagger glassShard = new Dagger("Glass Shard", 1, 2, 0.2, 2.5, 1.0, 
			"WEAPON (Dagger): A shard of glass from a broken mirror. Charge attack to shadowstep your opponents!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "glassShard.png"));
	
	// Spine Dagger
	public static final Dagger spineShiv = new Dagger("Spine Shiv", 1, 3, 0.2, 2.0, 1.0, 
			"WEAPON (Dagger): Knife formed from a sharpened bone. Charge attack to shadowstep your opponents!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "spineShiv.png"));
	
	// Box Cutter
	public static final Dagger boxCutter = new Dagger("Box Cutter", 2, 3, 0.2, 2.5, 1.0, 
			"WEAPON (Dagger): A weaponized office tool. It's surprisingly sharp.",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "glassShard.png"));
	
	// Crossbow Weapons ----------------------------
	//
	
	// Lowly Toy Crossbow
	public static final Crossbow toyCrossbow = new Crossbow("Toy Crossbow", 1, 1, 0.1, 2.0, 2.0, 
			"WEAPON (Crossbow): Charge attack to fire a bolt! Travels instantaneously.",
			GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png"));
	
	// Bow Weapons ---------------------------------
	//
	
	// Lowly Twig Bow
	public static final Bow twigBow = new Bow("Twig Bow", 1, 1, 0.05, 2.0, 3.0, 
			"WEAPON (Bow): Charge attack to fire an arrow!",
			GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png"));
	
	// Shield Weapons ------------------------------
	//
	
	// Wooden Targe
	public static final Shield woodenTarge = new Shield("Wooden Targe", 1, 2, 3, 0.15, 1.5, 1.0, 
			"WEAPON (Shield): [3 Block] Block some incoming damage when charging. This works even when offhanded!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "woodenTarge.png"));
	
	// Buckler
	public static final Shield buckler = new Shield("Buckler", 1, 3, 4, 0.15, 1.5, 1.0, 
			"WEAPON (Shield): [4 Block] Block some incoming damage when charging. This works even when offhanded!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "woodenTarge.png"));
	
	// Hammer Weapons ----------------------------
	//
	
	// Bone Club
	public static final Hammer boneClub = new Hammer("Bone Club", 2, 3, 0.05, 1.75, 1.0, 
			"WEAPON (Hammer): A makeshift club. Charge attacks hit all adjacents and partially ignore armor!",
			GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png"));
	
	// Special Weapons ----------------------------
	//
	
	// King's Staff
	public static final KingStaff kingStaff = new KingStaff();
	
	// The Injector
	public static final Injector injector = new Injector();
	
	// Venom
	public static final VenomGun venomGun = new VenomGun();
	
	
	// Weapon testing code -------------------------
	//
	public static void main(String[] args) {
		// Armor value for testing
		int armor = 0;
		
		// Fists
		dmgNums(Armory.bareFists, armor);
		dmgNums(Armory.caestus, armor);
		dmgNums(Armory.cactusClaws, armor);
		dmgNums(Armory.brickPads, armor);
		System.out.println("---------------");
		// Swords
		dmgNums(Armory.brokenSword, armor);
		dmgNums(Armory.ironSword, armor);
		dmgNums(Armory.rustedSabre, armor);
		dmgNums(Armory.steelSword, armor);
		System.out.println("---------------");
		// Spears
		dmgNums(Armory.longStick, armor);
		dmgNums(Armory.ironSpear, armor);
		dmgNums(Armory.ceremonialSpear, armor);
		dmgNums(Armory.fishingHook, armor);
		System.out.println("---------------");
		// Daggers
		dmgNums(Armory.glassShard, armor);
		dmgNums(Armory.spineShiv, armor);
		dmgNums(Armory.boxCutter, armor);
		System.out.println("---------------");
		// Maces
		dmgNums(Armory.boneClub, armor);
		System.out.println("---------------");
		// Shields
		dmgNums(Armory.woodenTarge, armor);
		dmgNums(Armory.buckler, armor);
		System.out.println("---------------");
		// Special
		dmgNums(Armory.kingStaff, armor);
		dmgNums(Armory.injector, armor);
		dmgNums(Armory.venomGun, armor);
	}
	
	private static void dmgNums(Weapon wep, int armor) {
		double max = wep.maxDmg - armor;
		double min = wep.minDmg;
		
		if (max < 0) {
			max = 0;
		}
		
		if (min > max) {
			min = max;
		}
		
		double nrmChance = 1.0 - wep.critChance;
		int critDmg = (int) (max * wep.critMult);
		
		double totalAvg = 0.0;
		
		totalAvg += nrmChance * ((min + max) / 2);
		totalAvg += wep.critChance * critDmg;
		
		totalAvg = round(totalAvg, 2);
		
		String spacer = "";
		
		for (int i = 0; i < (18 - wep.getName().length()); i++) {
			spacer += " ";
		}
		
		DecimalFormat df = new DecimalFormat("#.00");
		String formatDouble = df.format(totalAvg);
		if (formatDouble.startsWith(".")) {
			formatDouble = "0" + formatDouble;
		}
		
		System.out.println(wep.getName() + spacer + ": Average Damage = " +
				formatDouble + " | Crit Damage = " + critDmg);
	}
	
	public static double round(double value, int places) {
	    if (places < 0) {
	    	throw new IllegalArgumentException();
	    }

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
