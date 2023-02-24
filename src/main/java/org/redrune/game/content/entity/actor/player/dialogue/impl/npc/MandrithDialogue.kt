package org.redrune.game.content.entity.actor.player.dialogue.impl.npc

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue
import org.redrune.game.content.entity.actor.player.market.ShopRepository
import org.redrune.game.entity.actor.npc.NPC

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class MandrithDialogue : Dialogue() {

    var npc: NPC? = null

    override fun start() {
        npc = getParam(0)
        sendNPCDialogue(
            npc!!.id,
            THINKING,
            "Hello, traveller, may I interest you in",
            "some of the items from my fare collection?"
        )
    }

    override fun run(interfaceId: Int, componentId: Int) {
        when (stage.toInt()) {
            -1 -> {
                sendOptions(
                    "What would you like to say?",
                    "Yes, let me see your spoils.",
                    "No, can you tell me about this game?",
                    "No thanks."
                )
                stage = 0
            }

            0 -> {
                when (componentId) {
                    first -> {
                        sendPlayerDialogue(NORMAL, "Yes, let me see your spoils")
                        stage = 10
                    }
                }
            }

            10 -> {
                ShopRepository.open(player, 1)
                end()
            }
        }

    }

    override fun finish() {
    }
}