package org.redrune.game.global.punishment

import com.github.michaelbull.logging.InlineLogger
import com.google.gson.reflect.TypeToken
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.file.JsonFileManager
import org.redrune.utility.functions.GsonFunctions
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.entity.actor.player.JacksonFactory.fromFile
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.stream.Collectors

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/13/2017
 */
object PunishmentRepository {
    /**
     * The punishments that exist in the world
     */
    private val PUNISHMENTS: MutableList<Punishment> = ArrayList()

    /**
     * The location of all item characteristics
     */
    private const val PUNISHMENTS_FILE_LOCATION = "./data/saves/punishments.json"
    /**
     * Gets the queue of punishments awaiting addition
     */
    /**
     * The queue of punishments awaiting addition
     */
    val queue: Queue<Punishment> = ConcurrentLinkedQueue()

    /**
     * Loads all punishments
     */
    fun loadAll() {
        var punishments = punishmentsFromFile
        if (punishments == null) {
            punishments = ArrayList()
        }
        PUNISHMENTS.addAll(punishments)
        logger.info { ("Loaded " + PUNISHMENTS.size + " punishments from file " + File(PUNISHMENTS_FILE_LOCATION).path) }
    }

    /**
     * Adds a punishment to the queue awaiting addition
     */
    @JvmStatic
    fun addToQueue(punishment: Punishment): Boolean {
        return queue.add(punishment)
    }

    /**
     * Adds a punishment to the collection of punishments [.PUNISHMENTS]
     *
     * @param save
     * If we should save to the file
     */
    @JvmStatic
    fun add(punishment: Punishment, save: Boolean) {
        val added = PUNISHMENTS.add(punishment)
        if (save) {
            JsonFileManager.save<List<Punishment>>(PUNISHMENTS, PUNISHMENTS_FILE_LOCATION)
        }
        punishment.notify(true, added)
        if (added && punishment.additionEvent != null) {
            punishment.additionEvent.run()
        }
    }

    /**
     * Deletes a punishment and updates the file if requested
     */
    @JvmStatic
    fun delete(punishment: Punishment, save: Boolean): Boolean {
        val removed = PUNISHMENTS.removeIf { p: Punishment -> p == punishment }
        if (save) {
            JsonFileManager.save<List<Punishment>>(PUNISHMENTS, PUNISHMENTS_FILE_LOCATION)
        }
        punishment.notify(false, removed)
        return removed
    }

    /**
     * Gets the characteristic instance from a file
     */
    private val punishmentsFromFile: List<Punishment>?
        private get() {
            val file = File(PUNISHMENTS_FILE_LOCATION)
            if (!file.exists()) {
                return null
            }
            val text = Misc.getText(PUNISHMENTS_FILE_LOCATION)
            return GsonFunctions.GSON.fromJson(text, object : TypeToken<List<Punishment?>?>() {}.type)
        }

    /**
     * Checks if the player has any of the following punishments
     */
    @JvmStatic
    fun isPunished(player: Player, vararg types: PunishmentType?): Boolean {
        val typeList: List<PunishmentType> = ArrayList(Arrays.asList(*types))
        val applicable = PUNISHMENTS.stream().filter { punishment: Punishment -> typeList.contains(punishment.type) }
            .collect(Collectors.toList())
        for (punishment in applicable) {
            when (punishment.type) {
                PunishmentType.PLAYER_MUTE -> if (punishment.punished == player.username) {
                    return true
                }
                PunishmentType.PLAYER_BAN -> if (punishment.punished == player.username) {
                    return true
                }
                PunishmentType.ADDRESS_MUTE -> if (punishment.ip.orElse("n/a") == player.session.iPAddress || punishment.mac.orElse(
                        "n/a"
                    ) == player.session.macAddress
                ) {
                    return true
                }
                PunishmentType.ADDRESS_BAN -> if (punishment.ip.orElse("n/a") == player.session.iPAddress || punishment.mac.orElse(
                        "n/a"
                    ) == player.session.macAddress
                ) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Gets all the punishments
     */
    @JvmStatic
    val punishments: List<Punishment>
        get() = PUNISHMENTS

    /**
     * Finds all punishments that match the name and type
     */
    @JvmStatic
    fun findPunishments(name: String, target: Player?, type: PunishmentType): List<Punishment> {
        var target = target
        val punishments: MutableList<Punishment> = ArrayList()
        for (punishment in PUNISHMENTS) {
            if (punishment.type !== type) {
                continue
            }
            when (punishment.type) {
                PunishmentType.PLAYER_MUTE, PunishmentType.PLAYER_BAN -> if (punishment.punished == name) {
                    punishments.add(punishment)
                }
                PunishmentType.ADDRESS_MUTE -> if (target != null && (target.session.iPAddress == punishment.ip.orElse("n/a") || target.session.macAddress == punishment.mac.orElse(
                        "n/a"
                    ))
                ) {
                    punishments.add(punishment)
                }
                PunishmentType.ADDRESS_BAN -> {
                    target = fromFile(name)
                    if (target != null && (target.attributes.lastIP == punishment.ip.orElse("n/a") || target.attributes.lastMac == punishment.mac.orElse(
                            "n/a"
                        ))
                    ) {
                        punishments.add(punishment)
                    }
                }
            }
        }
        return punishments
    }

    private val logger = InlineLogger()
}