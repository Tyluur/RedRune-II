package org.redrune.game.global.punishment

import org.redrune.game.global.World
import org.redrune.game.global.punishment.PunishmentHandler.getMessage
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/17/2017
 */
class Punishment @JvmOverloads constructor(
    /**
     * The name of the player carried out the punishment
     */
    val punisher: String,

    /**
     * The name of the player who was punished
     */
    val punished: String,
    /**
     * The type of punishment this was
     */
    val type: PunishmentType,
    /**
     * The time the punishment will be over at
     */
    val time: Long,
    /**
     * The event executed when the punishment is added
     */
    @field:Transient var additionEvent: Runnable? = null
) {

    /**
     * The time the punishment was created, in simple date format
     */
    val punishedAt: String = Date().toLocaleString()

    /**
     * The time the punishment was created, in simple date format
     */
    val overAt: String = Date(time).toLocaleString()

    /**
     * Additional parameters, used for storing things like mac address/ip address
     */
    private val parameters: MutableMap<String, String> = HashMap()

    override fun equals(obj: Any?): Boolean {
        if (obj !is Punishment) {
            return false
        }
        val p = obj
        return punished == p.punished && punisher == p.punisher && type == p.type
    }

    /**
     * If the duration for the punishment has expired
     */
    fun hasExpired(): Boolean {
        return System.currentTimeMillis() > time
    }

    override fun toString(): String {
        return "Punishment{punisher='$punisher', punished='$punished', type=$type, time=$time}"
    }

    /**
     * Notifies the [.punisher] of the change of states in the punishment
     */
    fun notify(addition: Boolean, success: Boolean) {
        val player = World.getPlayer(punisher) ?: return
        val message = getMessage(this, addition, success)
        player.packets.sendMessage(message)
    }

    /**
     * Gets the ip of the punishment
     */
    val ip: Optional<String>
        get() = Optional.ofNullable(parameters["ip"])

    /**
     * Gets the mac of the punishment
     */
    val mac: Optional<String>
        get() = Optional.ofNullable(parameters["mac"])

    /**
     * Puts a parameter
     */
    fun putParameter(key: String, value: String) {
        parameters[key] = value
    }

}