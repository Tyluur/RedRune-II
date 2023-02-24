package org.redrune.game.content.entity.actor.player.controller.impl.activity.pvp

import org.redrune.game.global.WorldTile

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
enum class PvPZones(val bottomX: Int, val bottomY: Int, val topX: Int, val topY: Int, val safeZone: Boolean = false) {

    ARDOUGNE(2459, 3266, 2688, 3341),
    FALADOR(2936, 3312, 3065, 3395),
    GRAND_EXCHANGE_DANGER(3116, 3394, 3236, 3520),
    EDGEVILLE(3038, 3467, 3123, 3521),
    VARROCK(3173, 3382, 3330, 3520),
    DRAYNOR(3070, 3213, 3129, 3295),
    LUMBRIDGE(3138, 3138, 3269, 3330),

    // safe zones
    ARDOUGNE_WEST_BANK(2612, 3330, 2621, 3335, true),
    ARDOUGNE_EAST_BANK(2649, 3280, 2658, 3287, true),
    FALADOR_WEST_BANK(2943, 3368, 2947, 3373, true),
    FALADOR_WEST_BANK2(2948, 3368, 2949, 3369, true),
    FALADOR_EAST_BANK(3009, 3353, 3018, 3358, true),
    EDGEVILLE_BANK(3091, 3488, 3098, 3499, true),
    GRAND_EXCHANGE(3152, 3473, 3177, 3506, true),
    VARROCK_WEST_BANK(3179, 3432, 3194, 3446, true),
    VARROCK_EAST_BANK(3250, 3416, 3257, 3424, true),
    DRAYNOR_BANK(3088, 3240, 3097, 3246, true),
    LUMBRIDGE_BANK(3204, 3207, 3217, 3230, true),

    ;

    /**
     * Checks if the tile is inside the safe zone
     *
     * @param tile
     * The tile
     */
    open fun inside(tile: WorldTile): Boolean {
        return tile.withinArea(bottomX, bottomY, topX, topY)
    }

    companion object {

        val SAFE_ZONES = ArrayList<PvPZones>()

        val DANGEROUS_ZONES = ArrayList<PvPZones>()

        init {
            values().forEach { zone ->
                if (zone.safeZone) {
                    SAFE_ZONES += zone
                } else {
                    DANGEROUS_ZONES += zone
                }
            }
        }
    }

}