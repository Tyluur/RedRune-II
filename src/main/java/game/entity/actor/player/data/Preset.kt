package game.entity.actor.player.data

import game.entity.actor.player.Player
import game.entity.item.Item
import utility.constants.SkillConstants
import java.io.Serializable

class Preset(
    val name: String = "",
    val inventory: Array<Item?> = arrayOf(),
    val equipment: Array<Item?> = arrayOf(),
    val isAncientCurses: Boolean = false,
    val spellBook: Int = 0,
    val levels: DoubleArray = DoubleArray(SkillConstants.SKILL_NAME.size)
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
                player.combatDefinitions.spellBook.toInt(),
                player.skills.xp.copyOf(7)
            )
        }

        private const val serialVersionUID = 1385575955598546603L
    }
}