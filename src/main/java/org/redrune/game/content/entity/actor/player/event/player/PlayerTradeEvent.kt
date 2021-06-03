package org.redrune.game.content.entity.actor.player.event.player

import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.RouteEvent
import org.redrune.utility.constants.key.AttributeKey

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-01-25
 */
class PlayerTradeEvent(
    /**
     * The target player we wish to trade
     */
    private val target: Player
) : Event() {
    override fun run(player: Player) {
        player.setRouteEvent(RouteEvent(target, Runnable {
            player.setNextFaceActor(target)
            if (target.interfaceManager.containsScreenInter()) {
                player.packets.sendMessage("The other player is busy.")
                return@Runnable
            }
            if (!target.withinDistance(player, 14)) {
                player.packets.sendMessage("Unable to find target " + target.displayName)
                return@Runnable
            }
            if (target.getTemporaryAttribute<Any>(AttributeKey.TRADE_TARGET) === player) {
                target.removeTemporaryAttribute<Any>(AttributeKey.TRADE_TARGET)
                player.tradeManager.openTrade(target)
                target.tradeManager.openTrade(player)
                return@Runnable
            }
            player.nextFaceWorldTile = target
            player.putTemporaryAttribute(AttributeKey.TRADE_TARGET, target)
            player.packets.sendMessage("Sending " + target.displayName + " a request...")
            target.packets.sendTradeRequestMessage(player)
        }))
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK)
    }
}