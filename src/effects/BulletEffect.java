package effects;

import helpers.GPath;

public class BulletEffect extends GEffect {

	private static String basePath = GPath.createImagePath(GPath.ENEMY, GPath.SNAKETANK, "bullets");
	
	public BulletEffect(int xPos, int yPos, int dx, int dy) {
		super(xPos, yPos, basePath);
		
		if (dx == 0 && dy == 1) {
			this.filepath = (basePath + "_DOWN.png");
		} else if (dx == 0 && dy == -1) {
			this.filepath = (basePath + "_UP.png");
		} else if (dx == 1 && dy == 0) {
			this.filepath = (basePath + "_RIGHT.png");
		} else {
			this.filepath = (basePath + "_LEFT.png");
		}
	}
	
	public BulletEffect(int xPos, int yPos, int dx, int dy, int countDown) {
		super(xPos, yPos, basePath, countDown);
		
		if (dx == 0 && dy == 1) {
			this.filepath = (basePath + "_DOWN.png");
		} else if (dx == 0 && dy == -1) {
			this.filepath = (basePath + "_UP.png");
		} else if (dx == 1 && dy == 0) {
			this.filepath = (basePath + "_RIGHT.png");
		} else {
			this.filepath = (basePath + "_LEFT.png");
		}
	}
	
	
}
