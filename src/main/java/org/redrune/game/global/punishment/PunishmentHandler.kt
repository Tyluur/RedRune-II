package org.redrune.game.global.punishment

import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import org.redrune.game.global.punishment.PunishmentRepository.findPunishments
import org.redrune.utility.constants.ColorConstants
import org.redrune.utility.functions.Misc
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/17/2017
 */
object PunishmentHandler {
    /**
     * Adds a punishment
     *
     * @param player
     * The player punishing
     * @param name
     * The punished users name
     * @param type
     * The type of punishment
     * @param hours
     * The duration of the punishment
     */
    fun addPunishment(player: Player, name: String, hours: Int, type: PunishmentType) {
        val time =
            if (hours == 0) Long.MAX_VALUE else System.currentTimeMillis() + TimeUnit.HOURS.toMillis(hours.toLong())

        val message = String.format(
            "[<col=%s>Attempting to %s %s for%s</col>.]",
            ColorConstants.BLUE,
            type.name.lowercase(Locale.getDefault()),
            name,
            if (hours == 0) "ever" else " " + Misc.format(hours) + " hours"
        )
        player.packets.sendMessage(message)
        addPunishment(Punishment(player.username, name, type, time))
    }

    /**
     * Requests the removal of a punishment
     *
     * @param player
     * The player removing the punishment
     * @param name
     * The punished users name
     * @param type
     * The type of punishment
     */
    fun removePunishment(player: Player, name: String?, type: PunishmentType) {
        // the message for a request
        val message = String.format(
            "[<col=%s>Attempting to remove punishment '%s' from %s</col>.]",
            ColorConstants.BLUE,
            type.name.lowercase(Locale.getDefault()),
            name
        )
        player.packets.sendMessage(message)
        val target = World.getPlayer(name)
        val punishments = findPunishments(name!!, target, type)
        println(punishments)
        println(target)
        punishments.forEach { it ->
            deletePunishment(it)
        }
    }

    /**
     * Gets the message for the punishment
     *
     * @param punishment
     * The punishment
     * @param add
     * If the punishment was added or removed
     * @param success
     * if the punishment was successful or failed
     */
    @JvmStatic
    fun getMessage(punishment: Punishment, add: Boolean, success: Boolean): String {
        return "[" + (if (success) "Successfully" else "Failed to") + " " + (if (add) "add" + (if (!success) "" else "ed") + "" else "remove" + if (!success) "" else "d") + " punishment '" + punishment.type.name + "' " + (if (add) "to" else "from") + " '" + punishment.punished + "'.]"
    }

    /**
     * Handles the addition of a punishment
     *
     * @param punishment
     * The punishment to ad
     * @return `True` If we could add a punishment, meaning there was a player in the world by that name.
     */
    fun addPunishment(punishment: Punishment): Boolean {
        // as long as the punishment as added to the list successfully [ban/mute]
        val player = World.getPlayer(punishment.punished)
        return player != null && punishment.type.add(player, punishment)
    }

    /**
     * Handles the deletion of a punishment
     *
     * @param punishment
     * The punishment to delete
     */
    fun deletePunishment(punishment: Punishment): Boolean {
        val player = World.getPlayer(punishment.punished)
        return when (punishment.type) {
            PunishmentType.PLAYER_MUTE -> punishment.type.remove(player, punishment)
            PunishmentType.ADDRESS_MUTE -> punishment.type.remove(player, punishment)
            PunishmentType.PLAYER_BAN, PunishmentType.ADDRESS_BAN -> punishment.type.remove(null, punishment)
        }
        return false
    }
}