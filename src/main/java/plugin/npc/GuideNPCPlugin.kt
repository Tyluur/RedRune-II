package plugin.npc

import org.redrune.game.content.plugin.type.NPCPlugin
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class GuideNPCPlugin : NPCPlugin {
    
    override fun register() {
        registerNPC(945, "Talk-to")
    }

    override fun handle(player: Player, npc: NPC, option: String): Boolean {
        player.dialogueManager.startDialogue("GuideDialogue", npc.id)
        return true
    }

}