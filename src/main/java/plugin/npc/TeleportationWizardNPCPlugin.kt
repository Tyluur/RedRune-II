package plugin.npc

import org.redrune.game.content.plugin.type.NPCPlugin
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.key.AttributeKey
import plugin.rsinterface.TeleportationInterfacePlugin.Companion.displaySelectionInterface
import plugin.rsinterface.TeleportationInterfacePlugin.Companion.teleportPlayer
import plugin.rsinterface.TeleportationInterfacePlugin.TransportationLocation

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/13/2017
 */
class TeleportationWizardNPCPlugin : NPCPlugin {
    override fun handle(player: Player, npc: NPC, option: String): Boolean {
        when (option) {
            "Talk-to" -> {
                displaySelectionInterface(player, true)
                return true
            }
            "Previous" -> {
                val last =
                    player.attributes.getAttribute<TransportationLocation>(AttributeKey.LAST_TRANSPORTATION_LOCATION)
                        ?: return true
                teleportPlayer(
                    player,
                    last.destination,
                    Runnable { last.locations.handlePostTeleportation(player, last.optionIndex) })
                return true
            }
        }
        return false
    }

    override fun register() {
        registerNPC(9434, "Talk-to")
    }
}