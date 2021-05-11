package game.content.entity.actor.player.controller.impl.activity.pvp

import com.github.michaelbull.logging.InlineLogger
import game.GameFlags
import game.content.entity.actor.player.controller.Controller
import game.content.entity.actor.player.controller.impl.activity.Wilderness
import game.content.entity.actor.player.controller.impl.activity.pvp.PvPZones.Companion.SAFE_ZONES
import game.entity.actor.Actor
import game.entity.actor.npc.NPC
import game.entity.actor.player.Player
import game.global.WorldTile
import java.util.concurrent.TimeUnit
import kotlin.math.abs


/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class PvPWorld : Controller() {

    /**
     * The amount of ticks spent in the wild
     */
    private var dangerousTicks = 0

    /**
     * The time we will be considered safe at
     */
    private var timeAtSafe: Long = -1

    /**
     * If we are counting down from leaving a pvp zone while in combat
     */
    private var countingDown = false

    /**
     * If we landed in a safe area
     */
    private var arrivedSafely = false

    override fun start() {
        if (!GameFlags.pvpWorld) {
            forceClose()
            return
        }
        Wilderness.checkBoosts(player)
        showSkull()
        moved()
    }

    override fun login(): Boolean {
        start()
        updateWildLevel()
        moved()
        return super.login()
    }

    override fun logout(): Boolean {
        return false
    }

    override fun keepCombating(target: Actor): Boolean {
        if (target is NPC) {
            return true
        }
        if (!canAttack(target)) {
            return false
        }
        if (target.attackedBy !== player && player.attackedBy !== target) {
            player.attributes.setWildernessSkull()
        }
        return true
    }

    override fun canAttack(target: Actor): Boolean {
        if (target is Player) {
            val p2 = target
            if (player.attributes.isCanPvp && !p2.attributes.isCanPvp) {
                player.packets.sendMessage("That player is not in the wilderness.")
                return false
            }
            if (!canHit(target)) {
                player.packets.sendMessage("You must travel deeper into the wilderness to attack that player.")
                return false
            }
        }
        return true
    }

    override fun canHit(target: Actor): Boolean {
        if (target is NPC) {
            return true
        }
        val p2 = target as Player
        return abs(player.skills.combatLevel - p2.skills.combatLevel) <= getWildLevel()
    }

    override fun process() {
        incrementTicksAndUpdate()
        if (timeAtSafe > System.currentTimeMillis()) {
            sendSafeTimeLeft(
                TimeUnit.MILLISECONDS.toSeconds(timeAtSafe - System.currentTimeMillis())
                    .toInt()
            )
        }
        if (countingDown && timeAtSafe < System.currentTimeMillis()) {
            val isAtWild: Boolean = isAtPvpArea(player)
            val isAtWildSafe: Boolean =
                inBankSafe(player)
            if (!isAtWild || isAtWildSafe) {
                removeIcon(force = true, skullOnly = true)
                toggleSafeIcon(inBankSafe(player))
                timeAtSafe = -1
                countingDown = false
                arrivedSafely = true
            }
        }
    }

    private fun incrementTicksAndUpdate() {
        if (!inBankSafe(player)) {
            dangerousTicks++
            if (dangerousTicks == 100) {
                dangerousTicks = 0
                player.attributes.increaseEarningPotential(1.0)
                updateWildLevel()
            }
        }
    }

    fun getWildLevel(): Int {
        return 20
    }

    override fun moved() {
        val insidePvpArea: Boolean = isAtPvpArea(player)
        val insideSafeArea: Boolean = inBankSafe(player)
        val waitingForSafe: Boolean = timeAtSafe > System.currentTimeMillis()
        if (insidePvpArea && !insideSafeArea) {
            if (waitingForSafe) {
                timeAtSafe = -1
                countingDown = false
                removeIcon(false, true)
            }
            arrivedSafely = false
            player.attributes.isCanPvp = true
            showSkull()
            player.appearance.generateAppearanceData()
        } else if (insideSafeArea) {
            if (!waitingForSafe) {
                if (inCombatRecently() && !arrivedSafely) {
                    timeAtSafe = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)
                    countingDown = true
                    sendSafeTimeLeft(
                        TimeUnit.MILLISECONDS.toSeconds(timeAtSafe - System.currentTimeMillis())
                            .toInt()
                    )
                } else {
                    removeIcon(true, true)
                    toggleSafeIcon(inBankSafe(player))
                }
            } else {
                sendSafeTimeLeft(
                    TimeUnit.MILLISECONDS.toSeconds(timeAtSafe - System.currentTimeMillis())
                        .toInt()
                )
            }
        } else if (!insidePvpArea && !insideSafeArea) {
            player.attributes.isCanPvp = false
            removeIcon(true, false)
            removeController()
        }
    }

    fun showSkull() {
        updateWildLevel()
        // till safe
        player.packets.sendHideIComponent(745, 4, true)
        player.packets.sendHideIComponent(745, 5, true)
        player.packets.sendIComponentText(745, 5, "")

        // dangerous skull
        player.packets.sendHideIComponent(745, 6, false)
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
                "" + lowest + " - " + highest + "<br>" + player.attributes.formattedEarningPotential
            )
        } else {
            player.packets.sendIComponentText(548, 10, "$lowest - $highest")
            player.packets.sendIComponentText(548, 11, player.attributes.formattedEarningPotential)
        }
        toggleSafeIcon(inBankSafe(player))
    }


    /**
     * This method handles the removing of the on screen wilderness interfaces.
     *
     * @param force     If we should force the removal of the interfaces and the text.
     * @param skullOnly If we should only remove the skull on the screen, and leave the level text.
     */
    fun removeIcon(force: Boolean, skullOnly: Boolean) {
        if (force) {
            player.attributes.isCanPvp = false
            if (!skullOnly) {
                player.packets.sendIComponentText(
                    if (player.interfaceManager.hasRezizableScreen()) 746 else 548,
                    if (player.interfaceManager.hasRezizableScreen()) 17 else 10,
                    ""
                )
                player.packets.sendIComponentText(
                    if (player.interfaceManager.hasRezizableScreen()) 746 else 548,
                    if (player.interfaceManager.hasRezizableScreen()) 17 else 11,
                    ""
                )
            }
            // till safe
            player.packets.sendHideIComponent(745, 4, true)
            player.packets.sendHideIComponent(745, 5, true)
            player.packets.sendIComponentText(745, 5, "")
            player.packets.sendHideIComponent(745, 6, true)
            player.appearance.generateAppearanceData()
            player.equipment.refresh()
        }
    }

    private fun toggleSafeIcon(show: Boolean) {
        player.packets.sendHideIComponent(745, 3, !show)
    }

    private fun inCombatRecently(): Boolean {
        val lastTimeCombatted: Long = player.getTemporaryAttribute("last_time_combatted", -1L)
        return if (lastTimeCombatted == -1L) {
            player.isUnderCombat
        } else {
            val toSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastTimeCombatted)
            toSeconds < 10
        }
    }

    companion object {

        /**
         * If the tile is at the wilderness
         *
         * @param tile The tile
         */
        fun isAtPvpArea(tile: WorldTile): Boolean {
            for (zone in PvPZones.DANGEROUS_ZONES) {
                if (zone.inside(tile)) {
                    return true
                }
            }
            return false
        }

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