package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.context.NodeReachEventContext;
import org.redrune.game.node.entity.player.event.context.item.FloorItemPickupContext;
import org.redrune.game.node.entity.player.event.impl.NodeReachEvent;
import org.redrune.game.node.entity.player.event.impl.item.FloorItemPickupEvent;
import org.redrune.game.node.item.FloorItem;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemPacketDecoder implements IncomingPacketDecoder{
	
	/**
	 * The packet id for picking up floor items
	 */
	private static final int FLOOR_ITEM_PICKUP_ID = 24;
	
	@Override
	public int[] bindings() {
		return arguments(FLOOR_ITEM_PICKUP_ID, 49);
	}
	
	@SuppressWarnings("unused")
	@Override
	public void read(Player player, Packet packet) {
		// pickup
		if (packet.getOpcode() == FLOOR_ITEM_PICKUP_ID) {
			int y = packet.readShort();
			int itemId = packet.readShort();
			int x = packet.readShort();
			boolean forceRun = packet.readByte() == 1;
			
			Optional<FloorItem> optional = player.getRegion().getFloorItem(itemId, x, y, player.getLocation().getPlane());
			if (!optional.isPresent()) {
				player.getTransmitter().sendMessage("Oops! You're too late!");
				return;
			}
			FloorItem item = optional.get();
			if (!item.isRenderable()) {
				player.getTransmitter().sendMessage("Oops! You're too late!");
				return;
			}
			player.getMovement().reset(forceRun);
			player.getManager().getEvents().executeEvent(player, new NodeReachEvent(new NodeReachEventContext(item, () -> player.getManager().getEvents().executeEvent(player, new FloorItemPickupEvent(new FloorItemPickupContext(item))))));
		} else {
			// TODO: lighting a fire right click
		}
	}
}
