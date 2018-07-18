package tiles;

// Different types of operations a logic-oriented TileType can perform
public enum TriggerType {
	
	// Swaps wall and water tiles
	WALL_WATER,
	
	// Swaps wall and ground tiles
	WALL_GROUND,
	
	// Swaps ground and water tiles
	GROUND_WATER,
	
	// Spawns a set of enemies
	SPAWN_ENEMY,
	
	// Spawns a set of enemies and toggles tiles
	ENEMY_AND_TILE,
	
	// Spawns a set of projectiles
	SPAWN_PROJECTILE,
	
	// Plays a sound
	SOUND

}
