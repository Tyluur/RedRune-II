package org.redrune.game.content.entity.actor.player.event.object;

import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.networking.packet.handler.ObjectHandler;
import org.redrune.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-06
 */
public class ObjectInteractionEvent extends Event {
	
	/**
	 * The object to interact with
	 */
	private final WorldObject object;
	
	/**
	 * The option that was clicked on the object
	 */
	private final ClickOption option;
	
	public ObjectInteractionEvent(WorldObject object, ClickOption option) {
		this.object = object;
		this.option = option;
	}
	
	@Override
	public void run(Player player) {
		switch (option) {
			case FIRST:
				ObjectHandler.handleOption1(player, object);
				break;
			case SECOND:
				ObjectHandler.handleOption2(player, object);
				break;
			case THIRD:
				ObjectHandler.handleOption3(player, object);
				break;
			case EXAMINE:
				ObjectHandler.handleExamine(player, object);
				break;
		}
	}
	
	@Override
	public EventPolicy[] policies() {
		return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK);
	}
}
