package plugin.npc

import game.content.entity.actor.player.dialogue.impl.SimpleNPCMessage
import game.content.plugin.type.NPCPlugin
import game.entity.actor.mask.Animation
import game.entity.actor.mask.ForceTalk
import game.entity.actor.mask.Graphics
import game.entity.actor.npc.NPC
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since January 29, 2021
 */
class NurseTafariNPCPlugin : NPCPlugin {

    override fun register() {
        registerNPC(961, "Talk-to")
        registerNPC(961, "Heal")
    }

    override fun handle(player: Player, npc: NPC, option: String): Boolean {
        healPlayer(player, npc)
        return true
    }

    /**
     * Heals the player
     *
     * @param player
     * The player
     * @param npc
     * The npc
     */
    fun healPlayer(player: Player, npc: NPC): Boolean {
        /** Performing a cool npc interaction  */
        npc.nextAnimation = Animation(12575)
        player.setNextGraphics(Graphics(1314))
        /** Refreshing all player characteristics to optimal settings  */
        player.restoreAll()
        /** Sending surgeon dialogue  */
        player.dialogueManager.startDialogue(
            SimpleNPCMessage::class.java,
            961,
            "I have restored your character to extreme health!"
        )
        player.packets.sendMessage("You feel refreshed, past your normal health levels.")
        npc.nextForceTalk = ForceTalk("There you go! All better, ${player.displayName} :)")
        return false
    }

}
