package org.redrune.game.content.entity.actor.player.dialogue.impl.npc

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class GuideDialogue : Dialogue() {

    var npcId: Int = -1

    override fun start() {
        npcId = getParam(0)
        sendNPCDialogue(npcId, LAUGHING, "Hello there, ${player.displayName}, how may I help you today?")
    }

    override fun run(interfaceId: Int, componentId: Int) {
        when (stage.toInt()) {
            -1 -> {
                sendOptions(
                    "What would you like to say?",
                    "How do I get money here?",
                    "How do I travel?",
                    "Where do I buy items?",
                    "Nothing, never mind."
                )
                stage = 0
            }
            0 -> {
                when (componentId) {
                    first -> {
                        sendPlayerDialogue(NORMAL, "How do I get money here?")
                        stage = 1
                    }
                    second -> {
                        sendPlayerDialogue(NORMAL, "How do I travel around the world?")
                        stage = 2
                    }
                    third -> {
                        sendPlayerDialogue(NORMAL, "Where do I buy items?")
                        stage = 3
                    }
                    fourth -> {
                        sendPlayerDialogue(UNSURE, "Nothing, never mind.")
                        stage = 4
                    }
                }
            }
            1 -> {
                sendNPCDialogue(
                    npcId,
                    NORMAL,
                    "You can get money by killing people all across the world.",
                    "When your earning potential is high, you just might",
                    "get a rare item when you kill somebody."
                )
                stage = 5
            }
            5 -> {
                sendNPCDialogue(
                    npcId,
                    NORMAL,
                    "You can also skill and sell items for cash to players,",
                    "or to the grand exchange clerk"
                )
                stage = 6
            }
            6 -> {
                sendPlayerDialogue(LAUGHING, "Oh, that's pretty simple!", "Thanks!")
                stage = -2
            }

        }
    }

    override fun finish() {

    }
}