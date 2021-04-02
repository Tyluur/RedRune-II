package org.redrune.game.content.entity.actor.player

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.GameConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
object PlayerTutorial {

    fun onLogin(player: Player) {
        if (player.attributes.isReceivedTutorial) {
            return
        }

        player.dialogueManager.startDialogue(
            "SimpleNPCMessage",
            945,

            "Welcome to ${GameConstants.SERVER_NAME} pvp.",
            "You can set your levels by clicking on a skill.",
            "And buy items from the grand exchange clerk infront of you.",
            "Find me beside Mandrith if you'd ever like help."
        )

        player.setNextWorldTile(bankTile)
        player.bank.openBank()
        
        addBankStarter(player)
        player.attributes.isReceivedTutorial = true
    }

    private fun addBankStarter(player: Player) {
        player.bank.addItem(995, 5_000_000, true)

        player.bank.addItem(1323, 1, true)
        player.bank.addItem(1725, 1, true)
        player.bank.addItem(3105, 1, true)

        player.bank.addItem(11838, 1, true)
        player.bank.addItem(11870, 1, true)

        for (rune in 553..566) {
            player.bank.addItem(Item(rune, 500), true)
        }
    }

    private val bankTile = WorldTile(3097, 3496, 0)

    private val logger = InlineLogger()
}