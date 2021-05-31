package org.redrune.net.packet.context.impl;

import org.redrune.game.content.entity.actor.player.event.item.ItemInterfaceInteractionEvent;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.net.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class ItemOnItemPacketContext extends PacketContext {
	
	private final int interfaceId;
	
	private final int itemUsedId;
	
	private final int fromSlot;
	
	private final int interfaceId2;
	
	private final int itemUsedWithId;
	
	private final int toSlot;
	
	public ItemOnItemPacketContext(int interfaceId, int itemUsedId, int fromSlot, int interfaceId2, int itemUsedWithId, int toSlot) {
		this.interfaceId = interfaceId;
		this.itemUsedId = itemUsedId;
		this.fromSlot = fromSlot;
		this.interfaceId2 = interfaceId2;
		this.itemUsedWithId = itemUsedWithId;
		this.toSlot = toSlot;
	}
	
	@Override
	public void handle(Player player) {
		player.getEventManager().start(new ItemInterfaceInteractionEvent(interfaceId, itemUsedId, fromSlot, interfaceId2, itemUsedWithId, toSlot));
	}
}
