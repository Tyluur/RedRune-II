package org.redrune.game.content.entity.actor.player.controller.impl.activity.pvp

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.content.entity.actor.player.controller.Controller
import org.redrune.game.content.entity.actor.player.controller.impl.activity.pvp.PvPZones.Companion.SAFE_ZONES
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class PvPWorld : Controller() {

    override fun start() {
        logger.info { "Started the pvp world controller" }
    }

    fun getWildLevel(): Int {
        return 20
    }

    /**
     * Sends the amount of time left till we are safe
     *
     * @param time
     * The time
     */
    fun sendSafeTimeLeft(time: Int) {
        player.packets.sendHideIComponent(745, 6, true)
        player.packets.sendHideIComponent(745, 4, false)
        player.packets.sendHideIComponent(745, 5, false)
        player.packets.sendIComponentText(745, 5, "" + time)
    }

    fun updateWildLevel() {
        val lowest =
            if (player.skills.combatLevel - getWildLevel() < 3) 3 else player.skills.combatLevel - getWildLevel()
        val highest =
            if (player.skills.combatLevel + getWildLevel() > 138) 138 else player.skills.combatLevel + getWildLevel()
        if (player.interfaceManager.hasRezizableScreen()) {
            player.packets.sendIComponentText(
                746,
                17,
                "" + lowest + " - " + highest + "<br>" + player.attributes.getFormattedEarningPotential()
            )
        } else {
            player.packets.sendIComponentText(548, 10, "$lowest - $highest")
            player.packets.sendIComponentText(548, 11, player.attributes.formattedEarningPotential)
        }
        toggleSafeIcon(inBankSafe(player))
    }

    private fun toggleSafeIcon(show: Boolean) {
        player.packets.sendHideIComponent(745, 3, !show)
    }

    companion object {

        fun inBankSafe(player: Player): Boolean {
            for (zone in SAFE_ZONES) {
                if (zone.inside(player)) {
                    return true
                }
            }
            return false
        }

        private val logger = InlineLogger()
    }

}