package org.redrune.net.packet.context.impl;

import org.redrune.game.content.entity.actor.player.event.item.ItemFloorPickupEvent;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.FloorItem;
import org.redrune.game.global.WorldTile;
import org.redrune.net.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class GroundItemPickupPacketContext extends PacketContext {
	
	private final int regionId;
	
	private final boolean forceRun;
	
	private final FloorItem item;
	
	private final WorldTile tile;
	
	public GroundItemPickupPacketContext(int regionId, boolean forceRun, FloorItem item, WorldTile tile) {
		this.regionId = regionId;
		this.forceRun = forceRun;
		this.item = item;
		this.tile = tile;
	}
	
	@Override
	public void handle(Player player) {
		player.getEventManager().start(new ItemFloorPickupEvent(regionId, forceRun, item, tile));
	}
}
