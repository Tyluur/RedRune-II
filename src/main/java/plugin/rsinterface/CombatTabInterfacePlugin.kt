package plugin.rsinterface

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class CombatTabInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        when (componentId) {
            4 -> {
                player.putTemporaryAttribute("special_attack_toggled", true)
                CombatAlgorithm.checkSpecialToggle(player, 0)
            }
            in 11..14 -> {
                player.combatDefinitions.attackStyle = componentId - 11
            }
            15 -> {
                player.combatDefinitions.switchAutoRelatie()
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(884)
    }
}