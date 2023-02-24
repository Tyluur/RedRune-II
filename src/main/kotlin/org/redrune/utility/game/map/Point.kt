package org.redrune.utility.game.map

/**
 * Represents a point.
 *
 * @author Emperor
 */
class Point
@JvmOverloads constructor(
    /**
     * The x-coordinate.
     */
    @JvmField val x: Int,

    /**
     * The y-coordinate.
     */
    @JvmField val y: Int,

    /**
     * The direction for the next point.
     */
    val direction: Direction? = null,

    /**
     * The difference x between previous and current point.
     */
    val diffX: Int = 0,

    /**
     * The difference y between previous and current point.
     */
    val diffY: Int = 0,
) {
    /**
     * If we can't run during this point.
     */
    var isRunDisabled = false

    /**
     * Constructs a new `Point` `Object`.
     *
     * @param x           The x-coordinate.
     * @param y           The y-coordinate.
     * @param direction   The direction.
     * @param diffX       The difference x between previous and current point.
     * @param diffY       The difference y between previous and current point.
     * @param runDisabled If running is disabled for this walking point.
     */
    constructor(x: Int, y: Int, direction: Direction?, diffX: Int, diffY: Int, runDisabled: Boolean) : this(
        x,
        y,
        direction,
        diffX,
        diffY
    ) {
        isRunDisabled = runDisabled
    }
}