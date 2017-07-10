package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.content.event.context.NodeReachEventContext;
import org.redrune.game.content.event.context.item.FloorItemPickupContext;
import org.redrune.game.content.event.impl.NodeReachEvent;
import org.redrune.game.content.event.impl.item.FloorItemPickupEvent;
import org.redrune.game.content.event.impl.item.FloorItemUsageEvent;
import org.redrune.game.content.event.impl.item.ItemEvent;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.FloorItem;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.game.content.event.EventRepository;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The packet id for picking up floor items
	 */
	private static final byte FLOOR_ITEM_PICKUP_ID = 24;
	
	/**
	 * The packet id for examining floor items
	 */
	private static final byte FLOOR_ITEM_EXAMINE_ID = 28;
	
	/**
	 * The packet id for using items on the floor
	 */
	private static final byte FLOOR_ITEM_USAGE_ID = 49;
	
	@Override
	public int[] bindings() {
		return arguments(FLOOR_ITEM_PICKUP_ID, FLOOR_ITEM_USAGE_ID, FLOOR_ITEM_EXAMINE_ID);
	}
	
	@SuppressWarnings("unused")
	@Override
	public void read(Player player, Packet packet) {
		int y = packet.readShort();
		int itemId = packet.readShort();
		int x = packet.readShort();
		boolean forceRun = packet.readByte() == 1;
		int regionId = Location.getRegionId(x, y);
		Optional<FloorItem> optional = RegionManager.getRegion(regionId).getFloorItem(itemId, x, y, player.getLocation().getPlane(), null);
		if (!optional.isPresent()) {
			System.out.println("No item");
			return;
		}
		FloorItem item = optional.get();
		if (!item.isRenderable()) {
			System.out.println("not renderable");
			return;
		}
		player.getMovement().reset(forceRun);
		// pickup
		switch (packet.getOpcode()) {
			case FLOOR_ITEM_PICKUP_ID:
				// executes an event
				EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(item, () -> {
					// execute the floor item pickup event
					EventRepository.executeEvent(player, FloorItemPickupEvent.class, new FloorItemPickupContext(item));
				}));
				break;
			case FLOOR_ITEM_EXAMINE_ID:
				ItemEvent.handleItemExamining(player, item);
				break;
			case FLOOR_ITEM_USAGE_ID:
				EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(item, () -> {
					// execute the floor item usage event
					EventRepository.executeEvent(player, FloorItemUsageEvent.class, new FloorItemPickupContext(item));
				}));
				break;
		}
	}
}
