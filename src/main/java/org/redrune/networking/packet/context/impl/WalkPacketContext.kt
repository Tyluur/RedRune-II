package org.redrune.networking.packet.context.impl

import org.redrune.game.content.entity.actor.player.event.player.PlayerWalkEvent
import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.context.PacketContext

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class WalkPacketContext(
    /**
     * The destination x to travel to
     */
    private val destX: Int,
    /**
     * The destination y to travel to
     */
    private val destY: Int,
    /**
     * If the player should be forced to run
     */
    private val forceRun: Boolean
) : PacketContext() {
    override fun handle(player: Player) {
        player.eventManager.start(PlayerWalkEvent(destX, destY, forceRun))
    }
}