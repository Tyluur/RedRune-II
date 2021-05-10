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
        for (clerk in clerks) {
            registerNPC(clerk, "Talk-to")
            registerNPC(clerk, "Exchange")
            registerNPC(clerk, "History")
            registerNPC(clerk, "Sets")
        }
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

    private val clerks = listOf(1419, 2240, 2241, 2593)
}