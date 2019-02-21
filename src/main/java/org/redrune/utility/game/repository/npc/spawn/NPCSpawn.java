package org.redrune.utility.game.repository.npc.spawn;

import org.redrune.game.global.WorldTile;
import org.redrune.utility.functions.Misc.FaceDirection;
import lombok.Getter;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since May 15, 2015
 */
public class NPCSpawn {
	
	/**
	 * The id of the npc of this spawn
	 */
	@Getter
	private final int npcId;
	
	/**
	 * The tile of the spawn
	 */
	@Getter
	private final WorldTile tile;
	
	/**
	 * The direction the spawn is facing
	 */
	@Getter
	private final FaceDirection direction;
	
	/**
	 * Constructs a new npc spawn
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param tile
	 * 		The tile of the spawn
	 * @param direction
	 * 		The direction of the spawn
	 */
	public NPCSpawn(int npcId, WorldTile tile, FaceDirection direction) {
		this.npcId = npcId;
		this.tile = tile;
		this.direction = direction;
	}
	
	/**
	 * Constructs a new npc spawn facing north
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param tile
	 * 		The tile of the spawn
	 */
	public NPCSpawn(int npcId, WorldTile tile) {
		this.npcId = npcId;
		this.tile = tile;
		this.direction = FaceDirection.NORTH;
	}
	
	/**
	 * Constructs a new npc spawn
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param x
	 * 		The x coordinate of the tile of the spawn
	 * @param y
	 * 		The y coordinate of the tile of the spawn
	 * @param z
	 * 		The plane of the coordinate of the tile of the spawn
	 * @param direction
	 * 		The direction of the spawn
	 */
	public NPCSpawn(int npcId, int x, int y, int z, FaceDirection direction) {
		this.npcId = npcId;
		this.tile = new WorldTile(x, y, z);
		this.direction = direction;
	}
	
	/**
	 * Constructs a new npc spawn facing north
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param x
	 * 		The x coordinate of the tile of the spawn
	 * @param y
	 * 		The y coordinate of the tile of the spawn
	 * @param z
	 * 		The plane of the coordinate of the tile of the spawn
	 */
	public NPCSpawn(int npcId, int x, int y, int z) {
		this.npcId = npcId;
		this.tile = new WorldTile(x, y, z);
		this.direction = FaceDirection.NORTH;
	}
	
	
}
