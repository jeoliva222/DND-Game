package tiles;

public class MovableType {
	
	// Bit definitions
	public static final Short GROUND = 1; 		// 00000001
	public static final Short ALT_GROUND = 2; 	// 00000010
	public static final Short WATER = 4; 		// 00000100
	public static final Short ALT_WATER = 8; 	// 00001000
	public static final Short ACID = 16; 		// 00010000
	public static final Short WALL = 32; 		// 00100000
	public static final Short ALT_WALL = 64; 	// 01000000
	public static final Short PIT = 128; 		// 10000000
	
	/**
	 * Returns True if a source and target have move type compatibility. Returns False if no
	 * compatibility exists.
	 * @param srcType Short denoting move types of source
	 * @param targetType Short denoting move types of target
	 * @return boolean True if compatibility, False otherwise
	 */
	public static boolean canMove(short srcType, short targetType) {
		return ((srcType & targetType) > 0);
	}
	
	public static boolean isGround(short srcType) {
		short ground = (short) (GROUND + ALT_GROUND);
		return ((srcType & ground) > 0);
	}
	
	public static boolean isWater(short srcType) {
		short water = (short) (WATER + ALT_WATER);
		return ((srcType & water) > 0);
	}
	
	public static boolean isWall(short srcType) {
		short walls = (short) (WALL + ALT_WALL);
		return ((srcType & walls) > 0);
	}
	
}
