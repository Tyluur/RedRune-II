package org.redrune.networking.packet.context

import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
abstract class PacketContext {

    /**
     * Handles the packet context
     *
     * @param player
     * The player to handle it for
     */
    abstract fun handle(player: Player)
}