package org.redrune.networking.codec.packet.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.RouteEvent;
import org.redrune.game.entity.item.FloorItem;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.networking.codec.decode.handlers.InventoryOptionsHandler;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class ItemInteractionPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(ITEM_ON_ITEM_PACKET, ITEM_TAKE_PACKET, ITEM_EXAMINE_PACKET);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		switch (packetId) {
			case ITEM_EXAMINE_PACKET: {
				final int id = stream.readUnsignedShort128();
				@SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;// Dont delete this.
				
				int y = stream.readUnsignedShort();
				int x = stream.readUnsignedShortLE();
				final WorldTile tile = new WorldTile(x, y, player.getPlane());
				final int regionId = tile.getRegionId();
				final FloorItem item = RegionManager.getRegion(regionId).getGroundItem(id, tile, player);
				player.getPackets().sendGameMessage(ItemCharacteristicRepository.getExamine(item.getId()));
				break;
			}
			case ITEM_ON_ITEM_PACKET:
				InventoryOptionsHandler.handleItemOnItem(player, stream);
				break;
			case ITEM_TAKE_PACKET: {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || player.isFrozen()) {
					return;
				}
				final int id = stream.readUnsignedShort128();
				boolean forceRun = stream.readByte() == 1;
				int y = stream.readUnsignedShort();
				int x = stream.readUnsignedShortLE();
				final WorldTile tile = new WorldTile(x, y, player.getPlane());
				final int regionId = tile.getRegionId();
				if (player.getLocks().isInteractionLocked() || !player.getMapRegionsIds().contains(regionId)) {
					return;
				}
				final FloorItem item = RegionManager.getRegion(regionId).getGroundItem(id, tile, player);
				if (item == null) {
					return;
				}
				if (forceRun) {
					player.setRun(true);
				}
				player.stopAll(false);
				player.setRouteEvent(new RouteEvent(item, () -> {
					final FloorItem item1 = RegionManager.getRegion(regionId).getGroundItem(id, tile, player);
					if (item1 == null) {
						return;
					}
					player.setNextFaceWorldTile(tile);
					RegionManager.removeGroundItem(player, item1);
				}, true));
			}
			break;
		}
	}
}
