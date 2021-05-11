package org.redrune.game.entity.actor.player.data

import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import java.io.Serializable

class Preset(
    val name: String,
    val inventory: Array<Item?>,
    val equipment: Array<Item?>,
    val isAncientCurses: Boolean,
    val spellBook: Byte,
    val levels: DoubleArray
) : Serializable {

    fun getId(player: Player): Int {
        var i = 0
        for ((key) in player.presetManager.setups) {
            if (key.toLowerCase() == name) {
                return i
            }
            i++
        }
        throw RuntimeException("failed to locate preset")
    }

    companion object {

        fun generatePreset(player: Player): Preset {
            return Preset(
                "",
                player.inventory.items.itemsCopy,
                player.equipment.items.itemsCopy,
                player.prayer.isAncientCurses,
                player.combatDefinitions.spellBook.toByte(),
                player.skills.xp.copyOf(7)
            )
        }

        private const val serialVersionUID = 1385575955598546603L
    }
}