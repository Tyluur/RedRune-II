package org.redrune.net.packet.context.impl;

import org.redrune.game.content.entity.actor.player.event.object.ObjectInteractionEvent;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.net.packet.context.PacketContext;
import org.redrune.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class ObjectClickInteractionPacketContext extends PacketContext {
	
	/**
	 * The object to interact with
	 */
	private final WorldObject object;
	
	/**
	 * The option that was clicked on the object
	 */
	private final ClickOption option;
	
	public ObjectClickInteractionPacketContext(WorldObject object, ClickOption option) {
		this.object = object;
		this.option = option;
	}
	
	@Override
	public void handle(Player player) {
		player.getEventManager().start(new ObjectInteractionEvent(object, option));
	}
}
