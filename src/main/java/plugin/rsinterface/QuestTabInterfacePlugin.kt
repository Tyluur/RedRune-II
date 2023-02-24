@file:Suppress("unused")

package plugin.rsinterface

import org.redrune.game.content.entity.actor.player.skills.PresetHandler
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player

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
        packetId: Int,
    ): Boolean {
        val presets = PresetHandler.presets
        val index = slotId
        if (index > presets.size || index < 0) {
            return true

        }
        val set = presets[index]
        val inventory = player.inventory.items.itemsCopy
        val equipment = player.equipment.items.itemsCopy
        for (item in inventory) {
            if (item != null) player.bank.addItem(item, true)
        }
        for (item in equipment) {
            if (item != null) player.bank.addItem(item, true)
        }
        player.inventory.reset()
        player.inventory.refresh()
        player.equipment.reset()
        player.equipment.refresh()
        player.appearance.generateAppearanceData()
        for (id in 0 until set.levels.size) {
            player.skills.setXp(id, set.levels[id])
            player.skills[id] = player.skills.getLevelForXp(id)
        }
        player.refreshHitPoints()
        player.prayer.reset()
        player.inventory.init()
        player.equipment.refreshAll()
        player.appearance.generateAppearanceData()

        player.prayer.setPrayerBook(set.isAncientCurses)
        player.combatDefinitions.spellBook = set.spellBook.toInt()

        player.dialogueManager.startDialogue(
            "SimpleNPCMessage",
            945,
            "You have just activated the preset '${set.name}'.",
            "Go tease the noobs!"
        )

        player.restoreAll()

        return true
    }
}