package projectiles;

import helpers.GPath;

public class MusicNote extends GProjectile {

	private static String imagePath = GPath.createImagePath(GPath.ENEMY, GPath.WATCHMAN, "music_proj.png");
	
	public MusicNote(int xPos, int yPos, int dx, int dy, Class<?> owner) {
		super("Music Note", xPos, yPos, dx, dy, owner);
		
		// Damage values
		this.minDmg = 1;
		this.maxDmg = 2;
		
		// Critical Values
		this.critChance = 0.15;
		this.critMult = 1.5;
		
		// Piercing values
		this.entityPiercing = false;
		this.wallPiercing = true;
	}

	@Override
	public String getImage() {
		return MusicNote.imagePath;
	}
	
}
