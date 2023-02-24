package org.redrune.utility.game.repository.`object`.door

/**
 * Represents a door.
 *
 * @author Emperor
 * @author Tyluur<itstyluur@icloud.com>
 */
class Door
    (
    /**
     * The door's object id.
     */
    val id: Int,
) {

    /**
     * The door's replace object id.
     */
    @JvmField
    var replaceId = 0

    /**
     * If the player should automaticly walk through it.
     */
    val isAutoWalk = false

    override fun toString(): String {
        return "Door{" + "id=" + id + ", replaceId=" + replaceId + ", autoWalk=" + isAutoWalk + '}'
    }
}