package plugin.npc

import game.content.entity.actor.player.design.PlayerDesign
import game.content.plugin.type.NPCPlugin
import game.entity.actor.npc.NPC
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class MakeoverMageNPCPlugin : NPCPlugin {
    override fun handle(player: Player, npc: NPC, option: String): Boolean {
        when (option) {
            "Talk-to" -> {
                player.dialogueManager.startDialogue("MakeOverMage", npc.id, 0)
                return true
            }
            "Makeover" -> {
                PlayerDesign.open(player)
                return true
            }
        }
        return false
    }

    override fun register() {
        val ids = intArrayOf(2676, 599)
        for (id in ids) {
            registerNPC(id, "Talk-to")
            registerNPC(id, "Makeover")
        }
    }
}