package plugin.rsinterface

import game.content.entity.actor.combat.function.Magic
import game.content.plugin.type.InterfacePlugin
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class MagicSpellbookInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        if (interfaceId == 192) {
            if (componentId == 2) {
                player.combatDefinitions.switchDefensiveCasting()
            } else if (componentId == 7) {
                player.combatDefinitions.switchShowCombatSpells()
            } else if (componentId == 9) {
                player.combatDefinitions.switchShowTeleportSkillSpells()
            } else if (componentId == 11) {
                player.combatDefinitions.switchShowMiscallaneousSpells()
            } else if (componentId == 13) {
                player.combatDefinitions.switchShowSkillSpells()
            } else if (componentId in 15..17) {
                player.combatDefinitions.setSortSpellBook(componentId - 15)
            } else {
                Magic.processNormalSpell(player, componentId)
            }
        } else if (interfaceId == 193) {
            if (componentId == 5) {
                player.combatDefinitions.switchShowCombatSpells()
            } else if (componentId == 7) {
                player.combatDefinitions.switchShowTeleportSkillSpells()
            } else if (componentId >= 9 && componentId <= 11) {
                player.combatDefinitions.setSortSpellBook(componentId - 9)
            } else if (componentId == 18) {
                player.combatDefinitions.switchDefensiveCasting()
            } else {
                Magic.processAncientSpell(player, componentId)
            }
        } else if (interfaceId == 430) {
            if (componentId == 5) {
                player.combatDefinitions.switchShowCombatSpells()
            } else if (componentId == 7) {
                player.combatDefinitions.switchShowTeleportSkillSpells()
            } else if (componentId == 9) {
                player.combatDefinitions.switchShowMiscallaneousSpells()
            } else if (componentId in 11..13) {
                player.combatDefinitions.setSortSpellBook(componentId - 11)
            } else if (componentId == 20) {
                player.combatDefinitions.switchDefensiveCasting()
            } else {
                Magic.processLunarSpell(player, componentId)
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(192, 193, 430)
    }
}