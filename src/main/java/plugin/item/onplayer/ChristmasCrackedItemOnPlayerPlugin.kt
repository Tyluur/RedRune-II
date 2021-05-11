package plugin.item.onplayer

import game.content.entity.actor.player.dialogue.impl.ChristmasCrackerD
import game.content.plugin.type.ItemOnPlayerPlugin
import game.entity.actor.player.Player
import game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-09
 */
class ChristmasCrackedItemOnPlayerPlugin : ItemOnPlayerPlugin {
    override fun handle(player: Player, item: Item, partner: Player): Boolean {
        if (player.inventory.freeSlots < 3 || partner.inventory.freeSlots < 3) {
            val message =
                (if (player.inventory.freeSlots < 3) "You do" else "The other player does") + " not have enough inventory space to open this cracker."
            player.packets.sendMessage(message)
            return true
        }
        player.dialogueManager.startDialogue(ChristmasCrackerD::class.java, partner, item.id)
        return true
    }

    override fun register() {
        registerItemOnPlayerPlugin(962)
    }
}