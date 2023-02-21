package org.redrune.networking.packet.incoming.impl;

import org.jetbrains.annotations.NotNull;
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager;
import org.redrune.game.content.entity.actor.player.skills.firemaking.Firemaking;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.FloorItem;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.context.impl.GroundItemPickupPacketContext;
import org.redrune.networking.packet.context.impl.ItemOnItemPacketContext;
import org.redrune.networking.packet.incoming.IncomingPacketReader;
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class ItemInteractionPacketReader implements IncomingPacketReader {

    @Override
    public int[] bindings() {
        return arguments(ITEM_ON_ITEM_PACKET, ITEM_TAKE_PACKET, ITEM_EXAMINE_PACKET, GRAND_EXCHANGE_SELECTION, ITEM_ON_FLOOR_PACKET);
    }

    @Override
    public PacketContext read(Player player, Packet stream) {
        switch (stream.getOpcode()) {
            case ITEM_ON_FLOOR_PACKET: {

                final int id = stream.readUnsignedShort128();
                @SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;// Dont delete this.

                int y = stream.readUnsignedShort();
                int x = stream.readUnsignedShortLE();

                return new PacketContext() {
                    @Override
                    public void handle(@NotNull Player player) {
                        if (Firemaking.isFiremakingCapable(id)) {
                            Firemaking.startFiremaking(player, id);
                            return;
                        }
                    }
                };
            }
            case ITEM_EXAMINE_PACKET: {
                final int id = stream.readUnsignedShort128();
                @SuppressWarnings("unused") boolean unknown = stream.readByte() == 1;// Dont delete this.

                int y = stream.readUnsignedShort();
                int x = stream.readUnsignedShortLE();
                final WorldTile tile = new WorldTile(x, y, player.getPlane());
                final int regionId = tile.getRegionId();
                final FloorItem item = RegionManager.getRegion(regionId).getGroundItem(id, tile, player);
                return new PacketContext() {
                    @Override
                    public void handle(Player player) {
                        player.getPackets().sendMessage(ItemCharacteristicRepository.getExamine(item.getId()));
                    }
                };
            }
            case ITEM_ON_ITEM_PACKET:
                int interfaceId = stream.readIntV1() >> 16;
                int itemUsedId = stream.readUnsignedShort128();
                int fromSlot = stream.readUnsignedShortLE128();
                int interfaceId2 = stream.readIntV2() >> 16;
                int itemUsedWithId = stream.readUnsignedShort128();
                int toSlot = stream.readUnsignedShortLE();
                if (player.getLocks().isComponentLocked()) {
                    break;
                }
                return new ItemOnItemPacketContext(interfaceId, itemUsedId, fromSlot, interfaceId2, itemUsedWithId, toSlot);
            case ITEM_TAKE_PACKET: {
                if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead() || player.isFrozen()) {
                    break;
                }
                final int id = stream.readUnsignedShort128();
                boolean forceRun = stream.readByte() == 1;
                int y = stream.readUnsignedShort();
                int x = stream.readUnsignedShortLE();
                final WorldTile tile = new WorldTile(x, y, player.getPlane());
                final int regionId = tile.getRegionId();
                if (player.getLocks().isInteractionLocked() || !player.getMapRegionsIds().contains(regionId)) {
                    break;
                }
                final FloorItem item = RegionManager.getRegion(regionId).getGroundItem(id, tile, player);
                if (item == null) {
                    break;
                }
                return new GroundItemPickupPacketContext(regionId, forceRun, item, tile);
            }
            case GRAND_EXCHANGE_SELECTION: {

                int itemId = stream.readShort();
                ExchangeManager.INSTANCE.chooseBuyItem(player, itemId);
                break;
            }
        }
        return null;
    }
}
