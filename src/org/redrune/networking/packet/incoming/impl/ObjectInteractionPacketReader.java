package org.redrune.networking.packet.incoming.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.context.impl.ObjectClickInteractionPacketContext;
import org.redrune.networking.packet.context.impl.ObjectItemInteractionPacketContext;
import org.redrune.networking.packet.incoming.IncomingPacketReader;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class ObjectInteractionPacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(OBJECT_CLICK1_PACKET, OBJECT_CLICK2_PACKET, OBJECT_CLICK3_PACKET, OBJECT_EXAMINE_PACKET, ITEM_ON_OBJECT_PACKET);
	}
	
	@Override
	public PacketContext read(Player player, Packet stream) {
		switch (stream.getOpcode()) {
			case OBJECT_CLICK1_PACKET:
			case OBJECT_CLICK2_PACKET:
			case OBJECT_CLICK3_PACKET:
			case OBJECT_EXAMINE_PACKET: {
				int runFlag = stream.readUnsignedByte128();
				final int x = stream.readUnsignedShort128();
				final int id = stream.readUnsignedShortLE128();
				int y = stream.readUnsignedShortLE();
				
				final WorldTile tile = new WorldTile(x, y, player.getPlane());
				final int regionId = tile.getRegionId();
				final boolean forceRun = runFlag == 1;
				
				if (!player.getMapRegionsIds().contains(regionId)) {
					return null;
				}
				if (player.getLocks().isInteractionLocked()) {
					return null;
				}
				WorldObject object = RegionManager.getObjectWithId(tile, id);
				if (object == null || object.getId() != id) {
					return null;
				}
				return new ObjectClickInteractionPacketContext(object, getClickOptionById(stream.getOpcode()));
			}
			case ITEM_ON_OBJECT_PACKET:
				if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead()) {
					break;
				}
				long currentTime = Misc.currentTimeMillis();
				if (player.getLocks().isInteractionLocked() || player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
					break;
				}
				@SuppressWarnings("unused") final int unknown = stream.readUnsignedByteC();
				final int y = stream.readUnsignedShortLE();
				final int itemSlot = stream.readUnsignedShortLE();
				final int interfaceHash = stream.readIntLE();
				final int interfaceId = interfaceHash >> 16;
				final int itemId = stream.readUnsignedShortLE128();
				final int x = stream.readUnsignedShortLE();
				final int id = stream.readInt();
				final WorldTile tile = new WorldTile(x, y, player.getPlane());
				int regionId = tile.getRegionId();
				if (!player.getMapRegionsIds().contains(regionId)) {
					break;
				}
				WorldObject mapObject = RegionManager.getObjectWithId(tile, id);
				if (mapObject == null || mapObject.getId() != id) {
					break;
				}
				final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(id, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
				final Item item = player.getInventory().getItem(itemSlot);
				if (player.isDead() || Misc.getInterfaceDefinitionsSize() <= interfaceId) {
					break;
				}
				if (player.getLocks().isInteractionLocked() || !player.getInterfaceManager().containsInterface(interfaceId)) {
					break;
				}
				if (item == null || item.getId() != itemId) {
					break;
				}
				return new ObjectItemInteractionPacketContext(object, y, x, itemSlot, interfaceId, itemId, item);
		}
		return null;
	}
	
	private ClickOption getClickOptionById(int packetId) {
		switch (packetId) {
			case OBJECT_CLICK1_PACKET:
				return ClickOption.FIRST;
			case OBJECT_CLICK2_PACKET:
				return ClickOption.SECOND;
			case OBJECT_CLICK3_PACKET:
				return ClickOption.THIRD;
			case OBJECT_EXAMINE_PACKET:
				return ClickOption.EXAMINE;
		}
		return null;
	}
}
