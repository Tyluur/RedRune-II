package plugin.rsinterface

import org.redrune.engine.SystemManager
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player
import java.util.concurrent.TimeUnit

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
        if (componentId == 4) {
            player.putTemporaryAttribute("special_attack_toggled", true)
            SystemManager.SLOW_EXECUTOR.schedule({
                try {
                    if (player.isDead) {
                        return@schedule
                    }
                    CombatAlgorithm.checkSpecialToggle(player, 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 400, TimeUnit.MILLISECONDS)
        } else if (componentId >= 11 && componentId <= 14) {
            player.combatDefinitions.attackStyle = componentId - 11
        } else if (componentId == 15) {
            player.combatDefinitions.switchAutoRelatie()
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(884)
    }
}