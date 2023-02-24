package org.redrune.networking.packet.context.impl

import org.redrune.game.content.entity.actor.player.event.npc.NPCInteractionEvent
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.context.PacketContext
import org.redrune.utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class NPCInteractionPacketContext(private val npc: NPC, private val option: ClickOption, private val running: Boolean) :
    PacketContext() {
    override fun handle(player: Player) {
        if (running) {
            player.isRunModeOn = true
        }
        when (option) {
            ClickOption.FIRST, ClickOption.SECOND, ClickOption.THIRD, ClickOption.FOURTH -> player.eventManager.start(
                NPCInteractionEvent(
                    npc, option
                )
            )

            else -> {}
        }
    }
}