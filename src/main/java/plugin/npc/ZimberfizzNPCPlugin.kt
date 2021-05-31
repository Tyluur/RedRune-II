package plugin.npc

import org.redrune.game.content.plugin.type.NPCPlugin
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player

class ZimberfizzNPCPlugin : NPCPlugin {

    override fun register() {
        registerNPC(8592, "Talk-to")
    }

    override fun handle(
        player: Player,
        npc: NPC,
        option: String
    ): Boolean {
        player.interfaceManager.sendInterface(276)
        return true
    }
}