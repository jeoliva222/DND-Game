package ai;

/**
 * Enum detailing the different ways many common enemies can move when in their idle state
 * @author jeoliva
 */
public enum PatrolPattern {
	
	/**
	 *  Stands still while Idle
	 */
	STATIONARY,
	
	/**
	 * Occasionally shifts position when Idle
	 */
	WANDER,
	
	/**
	 * Patrols when Idle (Go straight until wall hit, then turn Clockwise or Counter-Clockwise randomly)
	 */
	PATROL,
	
	/**
	 * Same as PATROL, but always turn Clockwise if available
	 */
	PATROL_CW,
	
	/**
	 * Same as PATROL, but always turn Counter-Clockwise if available
	 */
	PATROL_CCW,
	
	/**
	 * Surface follows Clockwise along a wall
	 */
	SURFACE_CW,
	
	/**
	 * Surface follows Counter-Clockwise along a wall
	 */
	SURFACE_CCW
	
}
