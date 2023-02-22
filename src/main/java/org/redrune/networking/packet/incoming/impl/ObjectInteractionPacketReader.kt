package org.redrune.networking.packet.incoming.impl

import org.redrune.game.content.entity.`object`.ObjectHandler
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import org.redrune.game.global.map.region.RegionManager
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.ObjectClickInteractionPacketContext
import org.redrune.networking.packet.context.impl.ObjectItemInteractionPacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class ObjectInteractionPacketReader : IncomingPacketReader {
    override fun bindings(): IntArray {
        return arguments(
            PacketConstants.OBJECT_CLICK1_PACKET,
            PacketConstants.OBJECT_CLICK2_PACKET,
            PacketConstants.OBJECT_CLICK3_PACKET,
            PacketConstants.OBJECT_EXAMINE_PACKET,
            PacketConstants.ITEM_ON_OBJECT_PACKET
        )
    }

    override fun read(player: Player, stream: Packet): PacketContext? {
        if (!player.hasStarted() || !player.attributes.clientHasLoadedMapRegion() || player.isDead) {
            return null
        }
        val currentTime = Misc.currentTimeMillis()
        if (player.locks.isInteractionLocked || player.emotesManager.nextEmoteEnd >= currentTime) {
            return null
        }
        when (stream.opcode) {
            PacketConstants.OBJECT_CLICK1_PACKET, PacketConstants.OBJECT_CLICK2_PACKET, PacketConstants.OBJECT_CLICK3_PACKET, PacketConstants.OBJECT_EXAMINE_PACKET -> {
                val runFlag = stream.readUnsignedByte128()
                val x = stream.readUnsignedShort128()
                val id = stream.readUnsignedShortLE128()
                val y = stream.readUnsignedShortLE()
                val tile = WorldTile(x, y, player.plane)
                val regionId = tile.regionId
                val forceRun = runFlag == 1
                if (!player.mapRegionsIds.contains(regionId)) {
                    return null
                }
                val `object`: WorldObject = RegionManager.getObjectWithId(tile, id)
                if (`object`.id != id) {
                    return null
                }
                val clickOption: ClickOption? = getClickOptionById(stream.opcode)
                return if (clickOption == ClickOption.EXAMINE) {
                    ObjectHandler.handleExamine(player, `object`)
                    return null
                } else {
                    ObjectClickInteractionPacketContext(`object`, clickOption!!)
                }
            }
            PacketConstants.ITEM_ON_OBJECT_PACKET -> {
                stream.readUnsignedByteC()
                val y = stream.readUnsignedShortLE()
                val itemSlot = stream.readUnsignedShortLE()
                val interfaceHash = stream.readIntLE()
                val interfaceId = interfaceHash shr 16
                val itemId = stream.readUnsignedShortLE128()
                val x = stream.readUnsignedShortLE()
                val id = stream.readShort128()
                val tile = WorldTile(x, y, player.plane)
                val regionId = tile.regionId
                if (!player.mapRegionsIds.contains(regionId)) {
                    return null
                }
                val mapObject: WorldObject = RegionManager.getObjectWithId(tile, id)
                if (mapObject == null || mapObject.id != id) {
                    return null
                }
                val `object`: WorldObject = if (!player.isAtDynamicRegion) mapObject else WorldObject(
                    id,
                    mapObject.type,
                    mapObject.rotation,
                    x,
                    y,
                    player.plane
                )
                val item = player.inventory.getItem(itemSlot)
                if (player.isDead || Misc.getInterfaceDefinitionsSize() <= interfaceId) {
                    return null
                }
                if (player.locks.isInteractionLocked || !player.interfaceManager.containsInterface(interfaceId)) {
                    return null
                }
                if (item == null || item.id != itemId) {
                    return null
                }
                return ObjectItemInteractionPacketContext(`object`, y, x, itemSlot, interfaceId, itemId, item)
            }
        }
        return null
    }

    private fun getClickOptionById(packetId: Int): ClickOption? {
        when (packetId) {
            PacketConstants.OBJECT_CLICK1_PACKET -> return ClickOption.FIRST
            PacketConstants.OBJECT_CLICK2_PACKET -> return ClickOption.SECOND
            PacketConstants.OBJECT_CLICK3_PACKET -> return ClickOption.THIRD
            PacketConstants.OBJECT_EXAMINE_PACKET -> return ClickOption.EXAMINE
        }
        return null
    }
}