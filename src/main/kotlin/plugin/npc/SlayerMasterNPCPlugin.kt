package plugin.npc

import org.redrune.game.content.plugin.type.NPCPlugin
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class SlayerMasterNPCPlugin : NPCPlugin {
    override fun handle(player: Player, npc: NPC, option: String): Boolean {
        when (option) {
            "Talk-to" -> {
                player.dialogueManager.startDialogue("Turael", npc.id)
                return true
            }
        }
        return false
    }

    override fun register() {
        registerNPC(8461, "Talk-to")
    }
}