package org.redrune.networking.packet.context.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.RouteEvent;
import org.redrune.game.entity.item.FloorItem;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.networking.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
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
		if (forceRun) {
			player.setRunModeOn(true);
		}
		player.stopAll(false);
		player.setRouteEvent(new RouteEvent(item, () -> {
			final FloorItem item1 = RegionManager.getRegion(regionId).getGroundItem(item.getId(), tile, player);
			if (item1 == null) {
				return;
			}
			player.setNextFaceWorldTile(tile);
			RegionManager.removeGroundItem(player, item1);
		}, true));
	}
}
