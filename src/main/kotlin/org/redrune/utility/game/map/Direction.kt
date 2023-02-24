package org.redrune.utility.game.map

import org.redrune.game.global.WorldTile
import org.redrune.game.global.map.region.RegionManager
import java.util.*

/**
 * Represents a direction.
 *
 * @author Emperor
 */
enum class Direction
/**
 * Constructs a new `Direction` `Object`.
 *
 * @param stepX     The x-offset to move a step.
 * @param stepY     The y-offset to move a step.
 * @param value     The direction value.
 * @param traversal The traversal flags.
 */(
    /**
     * The amounts of steps on the x-axis.
     */
    @JvmField val stepX: Int,
    /**
     * The amounts of steps on the y-axis.
     */
    @JvmField val stepY: Int,
    /**
     * The integer value.
     */
    private val value: Int,
    /**
     * The traversal flags.
     */
    vararg var traversal: Int
) {
    /**
     * The north-west direction.
     */
    NORTH_WEST(-1, 1, 7, 0x12c0108, 0x12c0120, 0x12c0138),

    /**
     * The north direction.
     */
    NORTH(0, 1, 0, 0x12c0120),

    /**
     * The north-east direction.
     */
    NORTH_EAST(1, 1, 4, 0x12c0180, 0x12c0120, 0x12c01e0),

    /**
     * The west direction.
     */
    WEST(-1, 0, 3, 0x12c0108),

    /**
     * The east direction.
     */
    EAST(1, 0, 1, 0x12c0180),

    /**
     * The south-west direction.
     */
    SOUTH_WEST(-1, -1, 6, 0x12c0108, 0x12c0102, 0x12c010e),

    /**
     * The south direction.
     */
    SOUTH(0, -1, 2, 0x12c0102),

    /**
     * The south-east direction.
     */
    SOUTH_EAST(1, -1, 5, 0x12c0180, 0x12c0102, 0x12c0183);
    /**
     * Gets the stepX.
     *
     * @return The stepX.
     */
    /**
     * Gets the stepY.
     *
     * @return The stepY.
     */

    /**
     * Gets the traversal.
     *
     * @return The traversal.
     */
    /**
     * Sets the traversal.
     *
     * @param traversal The traversal to set.
     */

    val opposite: Direction
        /**
         * Gets the opposite dir.
         *
         * @return the direction.
         */
        get() = Companion[toInteger() + 2 and 3]

    /**
     * Method used to go to clue the anme.
     *
     * @param direction the direction.
     * @return the name.
     */
    fun toName(direction: Direction): String {
        return direction.name.lowercase(Locale.getDefault())
    }

    /**
     * Method used to get the direction to an integer.
     *
     * @return the integer.
     */
    fun toInteger(): Int {
        return value
    }

    /**
     * Checks if traversal is permitted for this direction.
     *
     * @param l The location.
     * @return `True` if so.
     */
    fun canMove(l: WorldTile): Boolean {
        val flag = RegionManager.getMask(l.plane, l.x, l.y)
        for (f in traversal) {
            if (flag and f != 0) {
                return false
            }
        }
        return true
    }

    companion object {
        /**
         * Gets the direction.
         *
         * @param rotation The int value.
         * @return The direction.
         */
        @JvmStatic
        operator fun get(rotation: Int): Direction {
            for (dir in values()) {
                if (dir.value == rotation) {
                    return dir
                }
            }
            throw IllegalArgumentException("Invalid direction value - $rotation")
        }

        /**
         * Gets the walk point for a direction. <br></br> The point will be the offset to the location the node is facing.
         *
         * @param direction The direction.
         * @return The point.
         */
        fun getWalkPoint(direction: Direction): Point {
            return Point(direction.stepX, direction.stepY)
        }

        /**
         * Gets the direction.
         *
         * @param location The start location.
         * @param l        The end location.
         * @return The direction.
         */
        fun getDirection(location: WorldTile, l: WorldTile): Direction {
            return getDirection(l.x - location.x, l.y - location.y)
        }

        /**
         * Gets the direction for movement.
         *
         * @param diffX The difference between 2 x-coordinates.
         * @param diffY The difference between 2 y-coordinates.
         * @return The direction.
         */
        @JvmStatic
        fun getDirection(diffX: Int, diffY: Int): Direction {
            if (diffX < 0) {
                if (diffY < 0) {
                    return SOUTH_WEST
                } else if (diffY > 0) {
                    return NORTH_WEST
                }
                return WEST
            } else if (diffX > 0) {
                if (diffY < 0) {
                    return SOUTH_EAST
                } else if (diffY > 0) {
                    return NORTH_EAST
                }
                return EAST
            }
            return if (diffY < 0) {
                SOUTH
            } else NORTH
        }

        /**
         * Gets the direction for the given walking flag.
         *
         * @param walkingFlag The walking flag.
         * @param rotation    The rotation.
         * @return The direction, or null if the walk flag was 0.
         */
        @JvmStatic
        fun forWalkFlag(walkingFlag: Int, rotation: Int): Direction? {
            var walkingFlag = walkingFlag
            if (rotation != 0) {
                walkingFlag = (walkingFlag shl rotation and 0xf) + (walkingFlag shr 4 - rotation)
            }
            if (walkingFlag > 0) {
                if (walkingFlag and 0x8 == 0) {
                    return WEST
                }
                if (walkingFlag and 0x2 == 0) {
                    return EAST
                }
                if (walkingFlag and 0x4 == 0) {
                    return SOUTH
                }
                if (walkingFlag and 0x1 == 0) {
                    return NORTH
                }
            }
            return null
        }

        /**
         * Gets the most logical direction.
         *
         * @param location The start location.
         * @param l        The end location.
         * @return The most logical direction.
         */
        fun getLogicalDirection(location: WorldTile, l: WorldTile): Direction {
            val offsetX = Math.abs(l.x - location.x)
            val offsetY = Math.abs(l.y - location.y)
            if (offsetX > offsetY) {
                return if (l.x > location.x) {
                    EAST
                } else {
                    WEST
                }
            } else if (l.y < location.y) {
                return SOUTH
            }
            return NORTH
        }
    }
}