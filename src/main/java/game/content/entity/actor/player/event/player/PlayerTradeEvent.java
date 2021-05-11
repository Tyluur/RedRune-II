package game.content.entity.actor.player.event.player;

import game.content.entity.actor.player.event.Event;
import game.entity.actor.player.Player;
import game.entity.actor.player.data.RouteEvent;
import utility.constants.key.AttributeKey;

/**
 * @author Tyluur <itstyluur@icloud.com>
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
				player.getPackets().sendMessage("The other player is busy.");
				return;
			}
			if (!target.withinDistance(player, 14)) {
				player.getPackets().sendMessage("Unable to find target " + target.getDisplayName());
				return;
			}
			if (target.getTemporaryAttribute(AttributeKey.TRADE_TARGET) == player) {
				target.removeTemporaryAttribute(AttributeKey.TRADE_TARGET);
				player.getTradeManager().openTrade(target);
				target.getTradeManager().openTrade(player);
				return;
			}
			player.setNextFaceWorldTile(target);
			player.putTemporaryAttribute(AttributeKey.TRADE_TARGET, target);
			player.getPackets().sendMessage("Sending " + target.getDisplayName() + " a request...");
			target.getPackets().sendTradeRequestMessage(player);
		}));
	}
	
	@Override
	public EventPolicy[] policies() {
		return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK);
	}
}
