package org.redrune.game.content.entity.actor.player.event.player;

import org.redrune.game.content.entity.actor.player.action.impl.PlayerFollowAction;
import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-25
 */
public class PlayerFollowEvent extends Event {
	
	/**
	 * The target player we want to follow
	 */
	private final Player target;
	
	public PlayerFollowEvent(Player target) {
		this.target = target;
	}
	
	@Override
	public void run(Player player) {
		player.getActionManager().setAction(new PlayerFollowAction(target));
	}
	
	@Override
	public EventPolicy[] policies() {
		return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK);
	}
}
