package network.packet.incoming.impl;

import game.entity.actor.player.Player;
import game.entity.actor.player.data.PlayerInventory;
import network.packet.Packet;
import network.packet.context.PacketContext;
import network.packet.context.impl.InterfaceInteractionPacketContext;
import network.packet.incoming.IncomingPacketReader;
import utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class InterfaceInteractionPacketReader implements IncomingPacketReader {

    @Override
    public int[] bindings() {
        return arguments(INTERFACE_ON_GROUND_ITEM_PACKET, CLOSE_INTERFACE_PACKET, IN_OUT_SCREEN_PACKET, SCREEN_PACKET, ACTION_BUTTON1_PACKET, ACTION_BUTTON2_PACKET, ACTION_BUTTON3_PACKET, ACTION_BUTTON4_PACKET, ACTION_BUTTON5_PACKET, ACTION_BUTTON6_PACKET, ACTION_BUTTON7_PACKET, ACTION_BUTTON8_PACKET, ACTION_BUTTON9_PACKET, ACTION_BUTTON10_PACKET, SWITCH_INTERFACE_ITEM_PACKET);
    }

    @Override
    public PacketContext read(Player player, Packet stream) {
        int packetId = stream.getOpcode();
        switch (packetId) {
            case INTERFACE_ON_GROUND_ITEM_PACKET: {
                int inventoryInter = stream.readInt() >> 16;
                int itemId = stream.readShort();
                int junk = stream.readShort();
                int itemSlot = stream.readShortLE();
                int interfaceSet = stream.readIntV1();
                int spellId = interfaceSet & 0xFFF;
                int magicInter = interfaceSet >> 16;
                System.out.println("Item:" + itemId + "slot:" + itemSlot + "spell:" + spellId + "i:" + interfaceSet + "l:" + magicInter + "x:" + junk + "k:" + inventoryInter);
                break;
            }
            case CLOSE_INTERFACE_PACKET:
                if (!player.isRunning()) {
                    player.run();
                    break;
                }
                return new PacketContext() {
                    @Override
                    public void handle(Player player) {
                        player.stopAll();
                    }
                };
            case IN_OUT_SCREEN_PACKET:
                // not using this check because not 100% efficient
                @SuppressWarnings("unused") boolean inScreen = stream.readByte() == 1;
                break;
            case SCREEN_PACKET:
                int displayMode = stream.readUnsignedByte();
                int screenWidth = stream.readUnsignedShort();
                int screenHeight = stream.readUnsignedShort();
                @SuppressWarnings("unused") boolean switchScreenMode = stream.readUnsignedByte() == 1;

                return new PacketContext() {
                    @Override
                    public void handle(Player player) {
                        player.getInterfaceManager().setScreenWidth(screenWidth);
                        player.getInterfaceManager().setScreenHeight(screenHeight);
                        if (!player.hasStarted() || player.isFinished() || displayMode == player.getInterfaceManager().getDisplayMode() || !player.getInterfaceManager().containsInterface(742)) {
                            return;
                        }
                        player.getInterfaceManager().setDisplayMode(displayMode);
                        player.getInterfaceManager().removeAll();
                        player.getInterfaceManager().sendInterfaces();
                        player.getInterfaceManager().sendInterface(742);
                    }
                };
            case ACTION_BUTTON1_PACKET:
            case ACTION_BUTTON2_PACKET:
            case ACTION_BUTTON4_PACKET:
            case ACTION_BUTTON5_PACKET:
            case ACTION_BUTTON6_PACKET:
            case ACTION_BUTTON7_PACKET:
            case ACTION_BUTTON8_PACKET:
            case ACTION_BUTTON3_PACKET:
            case ACTION_BUTTON9_PACKET:
            case ACTION_BUTTON10_PACKET:
                int interfaceHash = stream.readIntV2();
                int interfaceId = interfaceHash >> 16;
                if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
                    break;
                }
                if (player.isDead() || player.getLocks().isComponentLocked() || !player.getInterfaceManager().containsInterface(interfaceId)) {
                    break;
                }
                final int componentId = interfaceHash - (interfaceId << 16);
                if (componentId != 65535 && Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
                    break;
                }
                final int itemId = stream.readUnsignedShortLE128();
                final int slotId = stream.readUnsignedShort();
                return new InterfaceInteractionPacketContext(interfaceId, componentId, itemId, slotId, packetId);
            case SWITCH_INTERFACE_ITEM_PACKET:
                stream.readUnsignedShort();
                int fromSlot = stream.readUnsignedShortLE();
                stream.readUnsignedShort128();
                int interface1Hash = stream.readIntV1();
                int toSlot = stream.readUnsignedShortLE();
                int interface2Hash = stream.readIntV2();

                int fromInterfaceId = interface1Hash >> 16;
                int fromComponentId = interface1Hash - (fromInterfaceId << 16);

                int toInterfaceId = interface2Hash >> 16;
                int toComponentId = interface2Hash - (toInterfaceId << 16);

                if (Misc.getInterfaceDefinitionsSize() <= fromInterfaceId || Misc.getInterfaceDefinitionsSize() <= toInterfaceId) {
                    break;
                }
                if (!player.getInterfaceManager().containsInterface(fromInterfaceId) || !player.getInterfaceManager().containsInterface(toInterfaceId)) {
                    break;
                }
                if (fromComponentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId) {
                    break;
                }
                if (toComponentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId) {
                    break;
                }
                final int toSlotFinal = toSlot;
                return new PacketContext() {
                    @Override
                    public void handle(Player player) {
                        int toSlot = toSlotFinal;
                        if (fromInterfaceId == PlayerInventory.INVENTORY_INTERFACE && fromComponentId == 0 && toInterfaceId == PlayerInventory.INVENTORY_INTERFACE && toComponentId == 0) {
                            toSlot -= 28;
                            if (toSlot < 0 || toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player.getInventory().getItemsContainerSize()) {
                                return;
                            }
                            player.getInventory().switchItem(fromSlot, toSlot);
                        } else if (fromInterfaceId == 763 && fromComponentId == 0 && toInterfaceId == 763 && toComponentId == 0) {
                            if (toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player.getInventory().getItemsContainerSize()) {
                                return;
                            }
                            player.getInventory().switchItem(fromSlot, toSlot);
                        } else if (fromInterfaceId == 762 && toInterfaceId == 762) {
                            player.getBank().switchItem(fromSlot, toSlot, fromComponentId, toComponentId);
                        }
                    }
                };
        }
        return null;
    }
}
