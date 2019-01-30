package org.redrune.game.content.entity.actor.player.event.player;

import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.RouteEvent;
import org.redrune.utility.constants.AttributeKey;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-25
 */
public class PlayerTradeEvent extends Event {
	
	/**
	 * The target player we wish to trade
	 */
	private final Player target;
	
	public PlayerTradeEvent(Player target) {
		this.target = target;
	}
	
	@Override
	public void run(Player player) {
		player.setRouteEvent(new RouteEvent(target, () -> {
			player.setNextFaceActor(target);
			if (target.getInterfaceManager().containsScreenInter()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (!target.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage("Unable to find target " + target.getDisplayName());
				return;
			}
			if (target.getTemporaryAttribute(AttributeKey.TRADE_TARGET) == player) {
				target.removeTemporaryAttribute(AttributeKey.TRADE_TARGET);
				player.getTradeManager().openTrade(target);
				target.getTradeManager().openTrade(player);
				return;
			}
			player.setNextFaceWorldTile(target);
			player.putAttribute(AttributeKey.TRADE_TARGET, target);
			player.getPackets().sendGameMessage("Sending " + target.getDisplayName() + " a request...");
			target.getPackets().sendTradeRequestMessage(player);
		}));
	}
	
	@Override
	public EventPolicy[] policies() {
		return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK);
	}
}
