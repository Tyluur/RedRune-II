package plugin.npc

import game.content.plugin.type.NPCPlugin
import game.entity.actor.npc.NPC
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class MandrithNPCPlugin : NPCPlugin {

    override fun register() {
        registerNPC(6537, "Talk-to")
    }

    override fun handle(player: Player, npc: NPC, option: String): Boolean {
        player.dialogueManager.startDialogue("MandrithDialogue", npc)
        return true
    }
}