package org.redrune.utility.game.repository.npc.spawn

import org.redrune.game.global.WorldTile
import org.redrune.utility.functions.Misc.FaceDirection

/**
 * @author Tyluur
 * @since 2019-04-29
 */
class NPCSpawn {
    /**
     * The id of the npc of this spawn
     */
    val npcId: Int

    /**
     * The location of the spawn
     */
    val tile: WorldTile

    /**
     * The direction the spawn is facing
     */
    val direction: FaceDirection

    /**
     * Constructs a new npc spawn
     *
     * @param npcId     The id of the spawn
     * @param tile      The location of the spawn
     * @param direction The direction of the spawn
     */
    constructor(npcId: Int, tile: WorldTile, direction: FaceDirection) : this(
        npcId,
        tile.x,
        tile.y,
        tile.plane,
        direction
    )

    /**
     * Constructs a new npc spawn
     *
     * @param npcId     The id of the spawn
     * @param x         The x coordinate of the tile of the spawn
     * @param y         The y coordinate of the tile of the spawn
     * @param z         The plane of the coordinate of the tile of the spawn
     * @param direction The direction of the spawn
     */
    constructor(npcId: Int, x: Int, y: Int, z: Int, direction: FaceDirection) {
        this.npcId = npcId
        tile = WorldTile(x, y, z)
        this.direction = direction
    }

    /**
     * Constructs a new npc spawn facing north
     *
     * @param npcId The id of the spawn
     * @param tile  The location of the spawn
     */
    constructor(npcId: Int, tile: WorldTile) : this(npcId, tile.x, tile.y, tile.plane, FaceDirection.NORTH)

    /**
     * Constructs a new npc spawn facing north
     *
     * @param npcId The id of the spawn
     * @param x     The x coordinate of the tile of the spawn
     * @param y     The y coordinate of the tile of the spawn
     * @param z     The plane of the coordinate of the tile of the spawn
     */
    constructor(npcId: Int, x: Int, y: Int, z: Int) {
        this.npcId = npcId
        tile = WorldTile(x, y, z)
        direction = FaceDirection.NORTH
    }

    override fun toString(): String {
        return "[npcId=$npcId, tile=$tile, direction=$direction]"
    }
}