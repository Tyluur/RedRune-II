package org.redrune.networking.packet.incoming.impl

import org.redrune.game.content.entity.actor.player.event.player.PlayerAttackEvent
import org.redrune.game.content.entity.actor.player.event.player.PlayerFollowEvent
import org.redrune.game.content.entity.actor.player.event.player.PlayerInterfaceInteractionEvent
import org.redrune.game.content.entity.actor.player.event.player.PlayerTradeEvent
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class PlayerInteractionPacketReader : IncomingPacketReader {

    override fun bindings(): IntArray {
        return arguments(
            PacketConstants.ACCEPT_TRADE_CHAT_PACKET,
            PacketConstants.PLAYER_TRADE_OPTION_PACKET,
            PacketConstants.PLAYER_OPTION_1_PACKET,
            PacketConstants.PLAYER_OPTION_2_PACKET,
            PacketConstants.INTERFACE_ON_PLAYER
        )
    }

    override fun read(player: Player, stream: Packet): PacketContext? {
        val packetId = stream.opcode
        when (packetId) {
            PacketConstants.PLAYER_OPTION_1_PACKET -> {
                if (!(player.hasStarted() && player.attributes.clientHasLoadedMapRegion() && !player.isDead)) {
                    return null
                }
                @Suppress("unused") val unknown = stream.readByte().toInt() == 1
                val playerIndex = stream.readUnsignedShort()
                val target = World.getPlayers()[playerIndex]
                if (target == null || target.isDead || target.isFinished || !player.mapRegionsIds.contains(target.regionId)) {
                    return null
                }
                if (player.locks.isInteractionLocked || !player.controllerManager.canEntityClick(
                        target,
                        ClickOption.FIRST
                    ) || !player.attributes.isCanPvp
                ) {
                    return null
                }
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        player.setNextFaceActor(target)
                        if (!player.controllerManager.canAttack(target)) {
                            return
                        }
                        player.eventManager.start(PlayerAttackEvent(target))
                    }
                }
            }

            PacketConstants.PLAYER_OPTION_2_PACKET -> {
                if (!player.hasStarted() || !player.attributes.clientHasLoadedMapRegion() || player.isDead) {
                    return null
                }
                @Suppress("unused") val unknown = stream.readByte().toInt() == 1
                val playerIndex = stream.readUnsignedShort()
                val target = World.getPlayers()[playerIndex]
                // null and online checks individually
                if (target == null || target.isDead || target.isFinished || !player.mapRegionsIds.contains(target.regionId)) {
                    return null
                }
                // game verification checks
                if (player.locks.isInteractionLocked) {
                    return null
                }
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        player.eventManager.start(PlayerFollowEvent(target))
                    }
                }
            }

            PacketConstants.ACCEPT_TRADE_CHAT_PACKET, PacketConstants.PLAYER_TRADE_OPTION_PACKET -> {
                stream.readByte()
                val playerIndex = stream.readUnsignedShort()
                val target = World.getPlayers()[playerIndex]
                if (target == null || target.isDead || target.isFinished || !player.mapRegionsIds.contains(target.regionId)) {
                    return null
                }
                if (player.locks.isInteractionLocked) {
                    return null
                }
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        player.eventManager.start(PlayerTradeEvent(target))
                    }
                }
            }

            PacketConstants.INTERFACE_ON_PLAYER -> {
                if (!player.hasStarted() || !player.attributes.clientHasLoadedMapRegion() || player.isDead) {
                    return null
                }
                if (player.locks.isInteractionLocked) {
                    return null
                }
                val playerIndex = stream.readUnsignedShortLE()
                val interfaceHash = stream.readIntLE()
                val junk1 = stream.readUnsignedShort()
                @Suppress("unused") val unknown = stream.read128Byte() == 1
                @Suppress("unused") val slotId = stream.readUnsignedShortLE128()
                val interfaceId = interfaceHash shr 16
                var componentId = interfaceHash - (interfaceId shl 16)
                if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
                    return null
                }
                if (!player.interfaceManager.containsInterface(interfaceId)) {
                    return null
                }
                if (componentId == 65535) {
                    componentId = -1
                }
                val finalComponentId = componentId
                if (componentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
                    return null
                }
                val p2 = World.getPlayers()[playerIndex]
                if (p2 == null || p2.isDead || p2.isFinished || !player.mapRegionsIds.contains(p2.regionId)) {
                    return null
                }
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        player.eventManager.start(
                            PlayerInterfaceInteractionEvent(
                                p2,
                                interfaceId,
                                finalComponentId,
                                slotId
                            )
                        )
                    }
                }
            }
        }
        return null
    }
}