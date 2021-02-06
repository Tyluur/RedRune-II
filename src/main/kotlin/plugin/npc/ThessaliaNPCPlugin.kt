package plugin.npc

import org.redrune.game.content.entity.actor.player.PlayerLook
import org.redrune.game.content.plugin.type.NPCPlugin
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class ThessaliaNPCPlugin : NPCPlugin {

    override fun handle(player: Player, npc: NPC, option: String): Boolean {
        when (option) {
            "Change-clothes" -> {
                PlayerLook.openThessaliasMakeOver(player)
                return true
            }
        }
        return false
    }

    override fun register() {
        registerNPC(548, "Change-clothes")
    }
}