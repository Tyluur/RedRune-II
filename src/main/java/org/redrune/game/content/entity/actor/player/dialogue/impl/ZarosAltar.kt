package org.redrune.game.content.entity.actor.player.dialogue.impl

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue

class ZarosAltar : Dialogue() {

    override fun start() {
        sendOptions("Select an Option", "Change magic book", "Change prayer book")
    }

    override fun run(interfaceId: Int, componentId: Int) {
        when (this.stage.toInt()) {
            -1 -> when (componentId) {
                FIRST -> {
                    sendOptions("Select an Option", "Modern", "Ancient", "Lunar")
                    stage = 0
                }
                SECOND -> {
                    sendOptions("Select an Option", "Holy", "Cursed")
                    stage = 1
                }
            }
            0 -> {
                when (componentId) {
                    FIRST -> {
                        player.combatDefinitions.spellBook = -1
                    }
                    SECOND -> {
                        player.combatDefinitions.spellBook = 0
                    }
                    THIRD -> {
                        player.combatDefinitions.spellBook = 1
                    }
                }
                sendPlayerDialogue(UNSURE, "Wtf just happened to my magic dude?....")
            }
            1 -> {
                when(componentId) {
                    FIRST -> {
                        player.prayer.setPrayerBook(false)
                    }
                    SECOND -> {
                        player.prayer.setPrayerBook(true)
                    }
                }
                sendPlayerDialogue(UNSURE, "Wtf just happened to my prayer dude?....")
            }
        }
    }

    override fun finish() {}
}