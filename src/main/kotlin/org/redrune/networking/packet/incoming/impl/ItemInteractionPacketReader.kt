package org.redrune.networking.packet.incoming.impl

import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager.chooseBuyItem
import org.redrune.game.content.entity.actor.player.skills.firemaking.Firemaking
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import org.redrune.game.global.map.region.RegionManager
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.GroundItemPickupPacketContext
import org.redrune.networking.packet.context.impl.ItemOnItemPacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class ItemInteractionPacketReader : IncomingPacketReader {

    override fun bindings(): IntArray {
        return arguments(
            PacketConstants.ITEM_ON_ITEM_PACKET,
            PacketConstants.ITEM_TAKE_PACKET,
            PacketConstants.ITEM_EXAMINE_PACKET,
            PacketConstants.GRAND_EXCHANGE_SELECTION,
            PacketConstants.ITEM_ON_FLOOR_PACKET
        )
    }

    override fun read(player: Player, stream: Packet): PacketContext? {
        when (stream.opcode) {
            PacketConstants.ITEM_ON_FLOOR_PACKET -> {
                val id = stream.readUnsignedShort128()
                @Suppress("unused") val unknown = stream.readByte().toInt() == 1 // Dont delete this.
                val y = stream.readUnsignedShort()
                val x = stream.readUnsignedShortLE()
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        //TODO remove item correctly from ground
                        if (Firemaking.isFiremakingCapable(id)) {
                            Firemaking.startFiremaking(player, id)
                        }
                    }
                }
            }

            PacketConstants.ITEM_EXAMINE_PACKET -> {
                val id = stream.readUnsignedShort128()
                @Suppress("unused") val unknown = stream.readByte().toInt() == 1 // Dont delete this.
                val y = stream.readUnsignedShort()
                val x = stream.readUnsignedShortLE()
                val tile = WorldTile(x, y, player.plane)
                val regionId = tile.regionId
                val item = RegionManager.getRegion(regionId).getGroundItem(id, tile, player)
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        player.packets.sendMessage(ItemCharacteristicRepository.getExamine(item.id))
                    }
                }
            }

            PacketConstants.ITEM_ON_ITEM_PACKET -> {
                val interfaceId = stream.readIntV1() shr 16
                val itemUsedId = stream.readUnsignedShort128()
                val fromSlot = stream.readUnsignedShortLE128()
                val interfaceId2 = stream.readIntV2() shr 16
                val itemUsedWithId = stream.readUnsignedShort128()
                val toSlot = stream.readUnsignedShortLE()
                if (player.locks.isComponentLocked) {
                    return null
                }
                return ItemOnItemPacketContext(interfaceId, itemUsedId, fromSlot, interfaceId2, itemUsedWithId, toSlot)
            }

            PacketConstants.ITEM_TAKE_PACKET -> {
                if (!player.hasStarted() || !player.attributes.clientHasLoadedMapRegion() || player.isDead || player.isFrozen) {
                    return null
                }
                val id = stream.readUnsignedShort128()
                val forceRun = stream.readByte().toInt() == 1
                val y = stream.readUnsignedShort()
                val x = stream.readUnsignedShortLE()
                val tile = WorldTile(x, y, player.plane)
                val regionId = tile.regionId
                if (player.locks.isInteractionLocked || !player.mapRegionsIds.contains(regionId)) {
                    return null
                }
                val item = RegionManager.getRegion(regionId).getGroundItem(id, tile, player) ?: return null
                return GroundItemPickupPacketContext(regionId, forceRun, item, tile)
            }

            PacketConstants.GRAND_EXCHANGE_SELECTION -> {
                val itemId = stream.readShort()
                chooseBuyItem(player, itemId)
            }
        }
        return null
    }
}