@file:Suppress("unused")

package plugin.rsinterface

import org.redrune.game.content.entity.actor.player.skills.PresetHandler
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.SkillConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class QuestTabInterfacePlugin : InterfacePlugin {
    override fun register() {
        registerInterfacePlugin(34)
    }

    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        val presets = PresetHandler.Preset.values()
        val index = slotId
        if (index > presets.size || index < 0) {
            return true
        }
        val preset = presets[index]
        val equipment = preset.equipment()
        val inventory = preset.inventory()
        val skills = preset.skills()
        val spellBook = preset.spellBook()
        val prayerBook = preset.prayerBook()

        equipment.forEach { (slot, item) ->
            player.equipment.items[slot] = item
        }
        inventory.forEach { (slot, item) ->
            player.inventory.items[slot] = item
        }
        skills.forEach { (skillId, level) ->
            player.skills[skillId] = level
            player.skills.setXp(skillId, SkillConstants.getXPForLevel(level).toDouble())
        }
        player.inventory.refresh()
        player.equipment.refreshAll()
        player.appearance.generateAppearanceData()

        player.prayer.setPrayerBook(prayerBook == 2)
        player.combatDefinitions.spellBook = spellBook

        player.dialogueManager.startDialogue(
            "SimpleNPCMessage",
            945,
            "You have just activated the preset '${preset.title}'.",
            "Go tease the noobs!"
        )

        return true
    }
}