package org.redrune.networking.packet.incoming.impl

import org.redrune.game.GameFlags
import org.redrune.game.content.entity.actor.player.event.npc.NPCAttackEvent
import org.redrune.game.content.entity.actor.player.event.npc.NPCInterfaceInteractionEvent
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.NPCInteractionPacketContext
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.ClickOption
import org.redrune.utility.game.repository.npc.characteristic.NPCCharacteristicRepository
import org.redrune.utility.game.repository.npc.spawn.NPCSpawnRepository.removeSpawn

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class NPCInteractionPacketReader : IncomingPacketReader {
    override fun bindings(): IntArray {
        return arguments(
            PacketConstants.NPC_CLICK1_PACKET,
            PacketConstants.NPC_CLICK2_PACKET,
            PacketConstants.NPC_CLICK3_PACKET,
            PacketConstants.NPC_CLICK4_PACKET,
            PacketConstants.NPC_EXAMINE_PACKET,
            PacketConstants.ATTACK_NPC,
            PacketConstants.INTERFACE_ON_NPC
        )
    }

    override fun read(player: Player, packet: Packet): PacketContext? {
        when (packet.opcode) {
            PacketConstants.NPC_CLICK1_PACKET -> return decodeNPCStream(player, packet, ClickOption.FIRST)
            PacketConstants.NPC_CLICK2_PACKET -> return decodeNPCStream(player, packet, ClickOption.SECOND)
            PacketConstants.NPC_CLICK3_PACKET -> return decodeNPCStream(player, packet, ClickOption.THIRD)
            PacketConstants.NPC_CLICK4_PACKET -> return decodeNPCStream(player, packet, ClickOption.FOURTH)
            PacketConstants.NPC_EXAMINE_PACKET -> decodeNPCExamine(player, packet)
            PacketConstants.ATTACK_NPC -> {
                if (!player.hasStarted() || !player.attributes.clientHasLoadedMapRegion() || player.isDead) {
                    return null
                }
                if (player.locks.isInteractionLocked) {
                    return null
                }
                @Suppress("unused") val unknown = packet.readByte128() == 1
                val npcIndex = packet.readUnsignedShort128()
                val npc = World.getNPCs()[npcIndex]
                if (npc == null || npc.isDead || npc.isFinished || !player.mapRegionsIds.contains(npc.regionId) || !npc.definitions.hasAttackOption()) {
                    return null
                }
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        player.setNextFaceActor(npc)
                        if (!player.controllerManager.canAttack(npc)) {
                            return
                        }
                        player.eventManager.start(NPCAttackEvent(npc))
                    }
                }
            }

            PacketConstants.INTERFACE_ON_NPC -> {
                if (!player.hasStarted() || !player.attributes.clientHasLoadedMapRegion() || player.isDead) {
                    return null
                }
                if (player.locks.isInteractionLocked) {
                    return null
                }
                val slot = packet.readUnsignedShortLE128()
                packet.readUnsignedShortLE()
                val npcIndex = packet.readUnsignedShortLE()
                val interfaceHash = packet.readIntV2()
                packet.readByte()
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
                if (componentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
                    return null
                }
                val finalComponentId = componentId
                val npc = World.getNPCs()[npcIndex]
                if (npc == null || npc.isDead || npc.isFinished || !player.mapRegionsIds.contains(npc.regionId)) {
                    return null
                }
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        player.eventManager.start(
                            NPCInterfaceInteractionEvent(
                                npc,
                                interfaceId,
                                finalComponentId,
                                slot
                            )
                        )
                    }
                }
            }
        }
        return null
    }

    companion object {
        /**
         * Decodes the npc stream and passes the npc to the correct handler
         *
         * @param player The player
         * @param stream The stream
         * @param option The option clicked, used for handler identification
         */
        private fun decodeNPCStream(player: Player, stream: Packet, option: ClickOption): PacketContext? {
            val running = stream.readByte128() == 1
            val npcIndex = stream.readUnsignedShort128()
            val npc = World.getNPCs()[npcIndex]
            return if (npc == null || npc.isCantInteract || npc.isDead || npc.isFinished || !player.mapRegionsIds.contains(
                    npc.regionId
                ) || player.locks.isInteractionLocked
            ) {
                null
            } else NPCInteractionPacketContext(npc, option, running)
        }

        /**
         * Decodes the examine packet
         *
         * @param player The player
         * @param stream The stream
         */
        private fun decodeNPCExamine(player: Player, stream: Packet) {
            val running = stream.readByte128() == 1
            val npcIndex = stream.readUnsignedShort128()
            val npc = World.getNPCs()[npcIndex]
            if (npc == null || npc.isCantInteract || npc.isDead || npc.isFinished || !player.mapRegionsIds.contains(npc.regionId) || player.locks.isInteractionLocked) {
                return
            }
            if (player.getTemporaryAttribute("removing_npcs", false)) {
                removeSpawn(npc)
                npc.finish()
                return
            }
            player.packets.sendNPCMessage(0, npc, NPCCharacteristicRepository.getExamine(npc.id))
            if (GameFlags.debugMode) {
                player.packets.sendMessage(npc.toString())
            }
        }
    }
}