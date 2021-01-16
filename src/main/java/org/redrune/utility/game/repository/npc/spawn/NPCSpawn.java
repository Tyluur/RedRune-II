package org.redrune.utility.game.repository.npc.spawn;


import org.redrune.game.global.WorldTile;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.functions.Misc.FaceDirection;

/**
 * @author Tyluur
 * @since 2019-04-29
 */
public class NPCSpawn {
	
	/**
	 * The id of the npc of this spawn
	 */

	private final int npcId;
	
	/**
	 * The location of the spawn
	 */

	private final WorldTile tile;
	
	/**
	 * The direction the spawn is facing
	 */

	private final Misc.FaceDirection direction;
	
	/**
	 * Constructs a new npc spawn
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param tile
	 * 		The location of the spawn
	 * @param direction
	 * 		The direction of the spawn
	 */
	public NPCSpawn(int npcId, WorldTile tile, FaceDirection direction) {
		this(npcId, tile.getX(), tile.getY(), tile.getPlane(), direction);
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
	 * @param tile
	 * 		The location of the spawn
	 */
	public NPCSpawn(int npcId, WorldTile tile) {
		this(npcId, tile.getX(), tile.getY(), tile.getPlane(), FaceDirection.NORTH);
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
	
	@Override
	public String toString() {
		return "[npcId=" + npcId + ", tile=" + tile + ", direction=" + direction + "]";
	}

	public int getNpcId() {
		return npcId;
	}

	public WorldTile getTile() {
		return tile;
	}

	public FaceDirection getDirection() {
		return direction;
	}
}
