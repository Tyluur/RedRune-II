package org.redrune.networking.packet.incoming.impl

import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.PlayerInventory
import org.redrune.networking.packet.incoming.IncomingPacketReader
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.context.PacketContext
import org.redrune.networking.packet.context.impl.InterfaceInteractionPacketContext
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class InterfaceInteractionPacketReader : IncomingPacketReader {

    override fun bindings(): IntArray {
        return arguments(
            PacketConstants.INTERFACE_ON_GROUND_ITEM_PACKET,
            PacketConstants.CLOSE_INTERFACE_PACKET,
            PacketConstants.IN_OUT_SCREEN_PACKET,
            PacketConstants.SCREEN_PACKET,
            PacketConstants.ACTION_BUTTON1_PACKET,
            PacketConstants.ACTION_BUTTON2_PACKET,
            PacketConstants.ACTION_BUTTON3_PACKET,
            PacketConstants.ACTION_BUTTON4_PACKET,
            PacketConstants.ACTION_BUTTON5_PACKET,
            PacketConstants.ACTION_BUTTON6_PACKET,
            PacketConstants.ACTION_BUTTON7_PACKET,
            PacketConstants.ACTION_BUTTON8_PACKET,
            PacketConstants.ACTION_BUTTON9_PACKET,
            PacketConstants.ACTION_BUTTON10_PACKET,
            PacketConstants.SWITCH_INTERFACE_ITEM_PACKET
        )
    }

    override fun read(player: Player, stream: Packet): PacketContext? {
        when (val packetId = stream.opcode) {
            PacketConstants.INTERFACE_ON_GROUND_ITEM_PACKET -> {
                val inventoryInter = stream.readInt() shr 16
                val itemId = stream.readShort()
                val junk = stream.readShort()
                val itemSlot = stream.readShortLE()
                val interfaceSet = stream.readIntV1()
                val spellId = interfaceSet and 0xFFF
                val magicInter = interfaceSet shr 16
                println("Item:" + itemId + "slot:" + itemSlot + "spell:" + spellId + "i:" + interfaceSet + "l:" + magicInter + "x:" + junk + "k:" + inventoryInter)
            }

            PacketConstants.CLOSE_INTERFACE_PACKET -> {
                if (!player.isRunning) {
                    player.run()
                    return null
                }
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        player.stopAll()
                    }
                }
            }

            PacketConstants.IN_OUT_SCREEN_PACKET ->                 // not using this check because not 100% efficient
                @Suppress("unused") val inScreen = stream.readByte().toInt() == 1

            PacketConstants.SCREEN_PACKET -> {
                val displayMode = stream.readUnsignedByte()
                val screenWidth = stream.readUnsignedShort()
                val screenHeight = stream.readUnsignedShort()
                @Suppress("unused") val switchScreenMode = stream.readUnsignedByte() == 1
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        player.interfaceManager.screenWidth = screenWidth
                        player.interfaceManager.screenHeight = screenHeight
                        if (!player.hasStarted() || player.isFinished || displayMode == player.interfaceManager.displayMode || !player.interfaceManager.containsInterface(
                                742
                            )
                        ) {
                            return
                        }
                        player.interfaceManager.displayMode = displayMode
                        player.interfaceManager.removeAll()
                        player.interfaceManager.sendInterfaces()
                        player.interfaceManager.sendInterface(742)
                    }
                }
            }

            PacketConstants.ACTION_BUTTON1_PACKET, PacketConstants.ACTION_BUTTON2_PACKET, PacketConstants.ACTION_BUTTON4_PACKET, PacketConstants.ACTION_BUTTON5_PACKET, PacketConstants.ACTION_BUTTON6_PACKET, PacketConstants.ACTION_BUTTON7_PACKET, PacketConstants.ACTION_BUTTON8_PACKET, PacketConstants.ACTION_BUTTON3_PACKET, PacketConstants.ACTION_BUTTON9_PACKET, PacketConstants.ACTION_BUTTON10_PACKET -> {
                val interfaceHash = stream.readIntV2()
                val interfaceId = interfaceHash shr 16
                if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
                    return null
                }
                if (player.isDead || player.locks.isComponentLocked || !player.interfaceManager.containsInterface(
                        interfaceId
                    )
                ) {
                    return null
                }
                val componentId = interfaceHash - (interfaceId shl 16)
                if (componentId != 65535 && Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
                    return null
                }
                val itemId = stream.readUnsignedShortLE128()
                val slotId = stream.readUnsignedShort()
                return InterfaceInteractionPacketContext(interfaceId, componentId, itemId, slotId, packetId)
            }

            PacketConstants.SWITCH_INTERFACE_ITEM_PACKET -> {
                stream.readUnsignedShort()
                val fromSlot = stream.readUnsignedShortLE()
                stream.readUnsignedShort128()
                val interface1Hash = stream.readIntV1()
                val toSlot = stream.readUnsignedShortLE()
                val interface2Hash = stream.readIntV2()
                val fromInterfaceId = interface1Hash shr 16
                val fromComponentId = interface1Hash - (fromInterfaceId shl 16)
                val toInterfaceId = interface2Hash shr 16
                val toComponentId = interface2Hash - (toInterfaceId shl 16)
                if (Misc.getInterfaceDefinitionsSize() <= fromInterfaceId || Misc.getInterfaceDefinitionsSize() <= toInterfaceId) {
                    return null
                }
                if (!player.interfaceManager.containsInterface(fromInterfaceId) || !player.interfaceManager.containsInterface(
                        toInterfaceId
                    )
                ) {
                    return null
                }
                if (fromComponentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId) {
                    return null
                }
                if (toComponentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId) {
                    return null
                }
                val toSlotFinal = toSlot
                return object : PacketContext() {
                    override fun handle(player: Player) {
                        var toSlot = toSlotFinal
                        if (fromInterfaceId == PlayerInventory.INVENTORY_INTERFACE && fromComponentId == 0 && toInterfaceId == PlayerInventory.INVENTORY_INTERFACE && toComponentId == 0) {
                            toSlot -= 28
                            if (toSlot < 0 || toSlot >= player.inventory.itemsContainerSize || fromSlot >= player.inventory.itemsContainerSize) {
                                return
                            }
                            player.inventory.switchItem(fromSlot, toSlot)
                        } else if (fromInterfaceId == 763 && fromComponentId == 0 && toInterfaceId == 763 && toComponentId == 0) {
                            if (toSlot >= player.inventory.itemsContainerSize || fromSlot >= player.inventory.itemsContainerSize) {
                                return
                            }
                            player.inventory.switchItem(fromSlot, toSlot)
                        } else if (fromInterfaceId == 762 && toInterfaceId == 762) {
                            player.bank.switchItem(fromSlot, toSlot, fromComponentId, toComponentId)
                        }
                    }
                }
            }
        }
        return null
    }
}