package weapons;

import helpers.GPath;
import weapons.special.Injector;
import weapons.special.KingStaff;
import weapons.special.VenomGun;

// Class where all weapon definitions are stored
public class Armory {
	
	// Fist Weapons -----------------------------
	//
	
	// Bare fists
	public static final Fists bareFists = new Fists("Bare Fists", 1, 1, 0.05, 2.0, 1.0,
			"WEAPON (Fist): Slap em!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "bareFists.png"));
	
	// Padded Fists (Caestus)
	public static final Fists caestus = new Fists("Caestus", 1, 2, 0.00, 1.0, 2.5, 
			"WEAPON (Fist): Studded with iron rivets. Charge attack to dish out extra pain!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "caestus.png"));
	
	// Cactus Claws
	public static final Fists cactusClaws = new Fists("Cactus Claws", 2, 2, 0.00, 1.0, 2.5, 
			"WEAPON (Fist): Claws studded with cactus thorns. Gets consistent results.",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "cactusClaws.png"));

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
	public static final Shield woodenTarge = new Shield("Wooden Targe", 1, 2, 2, 0.15, 1.5, 1.0, 
			"WEAPON (Shield): [2 Block] Block some incoming damage when charging. This works even when offhanded!",
			GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "woodenTarge.png"));
	
	// Hammer Weapons ----------------------------
	//
	
	// Bone Club
	public static final Hammer boneClub = new Hammer("Bone Club", 2, 3, 0.05, 1.75, 1.5, 
			"WEAPON (Hammer): Club improvised from a sturdy bone. Charge attacks deal extra damage and ignore half of an enemy's armor!",
			GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png"));
	
	// Special Weapons ----------------------------
	//
	
	// King's Staff
	public static final KingStaff kingStaff = new KingStaff();
	
	// The Injector
	public static final Injector injector = new Injector();
	
	// Venom
	public static final VenomGun venomGun = new VenomGun();

}
