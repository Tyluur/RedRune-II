package org.redrune.game.content.entity.actor.player.event.player

import org.redrune.game.content.entity.actor.player.action.impl.PlayerFollowAction
import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-01-25
 */
class PlayerFollowEvent(
    /**
     * The target player we want to follow
     */
    private val target: Player
) : Event() {
    override fun run(player: Player) {
        player.actionManager.action = PlayerFollowAction(target)
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK)
    }
}