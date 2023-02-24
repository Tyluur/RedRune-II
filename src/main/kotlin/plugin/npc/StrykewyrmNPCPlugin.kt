package plugin.npc

import org.redrune.game.content.plugin.type.NPCPlugin
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.npc.impl.slayer.Strykewyrm
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class StrykewyrmNPCPlugin : NPCPlugin {
    override fun register() {
        registerNPC(9462, "Investigate")
    }

    override fun handle(player: Player, npc: NPC, option: String): Boolean {
        Strykewyrm.handleStomping(player, npc)
        return true
    }
}