package org.redrune.net.packet.context.impl;

import org.redrune.game.content.entity.actor.player.event.object.ObjectInterfaceInteractionEvent;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.net.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class ObjectItemInteractionPacketContext extends PacketContext {
	
	private final WorldObject object;
	
	private final int y;
	
	private final int x;
	
	private final int itemSlot;
	
	private final int interfaceId;
	
	private final int itemId;
	
	private final Item item;
	
	public ObjectItemInteractionPacketContext(WorldObject object, int y, int x, int itemSlot, int interfaceId, int itemId, Item item) {
		this.object = object;
		this.y = y;
		this.x = x;
		this.itemSlot = itemSlot;
		this.interfaceId = interfaceId;
		this.itemId = itemId;
		this.item = item;
	}
	
	@Override
	public void handle(Player player) {
		player.getEventManager().start(new ObjectInterfaceInteractionEvent(object, y, x, itemSlot, interfaceId, itemId, item));
	}
}
