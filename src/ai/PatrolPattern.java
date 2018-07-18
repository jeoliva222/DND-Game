package ai;

public enum PatrolPattern {
	
	// Stands still while Idle
	STATIONARY,
	
	// Occasionally shifts position when Idle
	WANDER,
	
	// Patrols when Idle (Go straight until hit wall, then turn)
	PATROL,
	
	// Same as PATROL, but always turn Clockwise if available
	PATROL_CW,
	
	// Same as PATROL, but always turn Counter-Clockwise if available
	PATROL_CCW

}
