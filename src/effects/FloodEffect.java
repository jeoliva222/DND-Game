package effects;

import helpers.GPath;

public class FloodEffect extends GEffect {

	private static String baseImage = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON, "water");
	
	public FloodEffect(int xPos, int yPos, int dx, boolean isEnd) {
		super(xPos, yPos, GPath.NULL);
		String dirPath = "";
		String endPath = "";
		
		// Decide end path
		if(isEnd) {
			endPath = "_END";
		} else {
			endPath = "_BODY";
		}
		
		// Decide direction path
		if(dx > 0) {
			dirPath = "_RIGHT.png";
		} else {
			dirPath = "_LEFT.png";
		}
		
		this.filepath = (baseImage + endPath + dirPath);
	}
	
	public FloodEffect(int xPos, int yPos, int dx, boolean isEnd, int countDown) {
		super(xPos, yPos, GPath.NULL, countDown);
		String dirPath = "";
		String endPath = "";
		
		// Decide end path
		if(isEnd) {
			endPath = "_END";
		} else {
			endPath = "_BODY";
		}
		
		// Decide direction path
		if(dx > 0) {
			dirPath = "_LEFT.png";
		} else {
			dirPath = "_RIGHT.png";
		}
		
		this.filepath = (baseImage + endPath + dirPath);
	}
	
	
}
