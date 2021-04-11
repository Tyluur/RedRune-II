package org.redrune.engine.tick.schedule.impl

import org.redrune.engine.tick.schedule.ScheduledTask
import org.redrune.game.global.World
import org.redrune.utility.constants.ColorConstants
import org.redrune.utility.constants.GameConstants
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur
 * @since 2019-05-22
 */
class InformationTabTick : ScheduledTask(10, -1) {
    override fun run() {
        World.playerStream().forEach { player ->
            val bldr = StringBuilder()

            bldr.append("<col=FF0000>" + GameConstants.SERVER_NAME + " <br><br>")
            bldr.append("Online: <col=" + ColorConstants.WHITE + ">").append(World.getPlayers().size).append("<br>")
            bldr.append("<br><col=FF0000>Player<br><br>")
            bldr.append("Rank: <col=" + ColorConstants.WHITE + ">")
                .append(Misc.formatPlayerNameForDisplay(player.dominantRight.formattedName)).append("<br>")

            // sends all the text in the stringbuilder
            player.packets.sendIComponentText(INTERFACE_ID, 16, bldr.toString())
        }
    }

    companion object {
        /**
         * The id of the interface used for this information
         */
        private const val INTERFACE_ID = 930
    }
}