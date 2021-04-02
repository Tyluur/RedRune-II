package plugin.npc

import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager
import org.redrune.game.content.plugin.type.NPCPlugin
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class ExchangeClerkNPCPlugin : NPCPlugin {

    override fun register() {
        registerNPC(2593, "Talk-to")
        registerNPC(2593, "Exchange")
        registerNPC(2593, "History")
        registerNPC(2593, "Sets")
    }


    override fun handle(player: Player, npc: NPC, option: String): Boolean {
        when (option) {
            "Talk-to", "Exchange" -> {
                ExchangeManager.open(player)
            }
            else -> {
                player.packets.sendMessage("This has not yet been added.")
            }
        }
        return true
    }
}