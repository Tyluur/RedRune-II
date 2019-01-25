package org.redrune.networking.codec.packet.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.PlayerInventory;
import org.redrune.networking.codec.decode.handlers.ButtonHandler;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class InterfaceInteractionPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(INTERFACE_ON_GROUND_ITEM_PACKET, CLOSE_INTERFACE_PACKET, IN_OUT_SCREEN_PACKET, SCREEN_PACKET, ACTION_BUTTON1_PACKET, ACTION_BUTTON2_PACKET, ACTION_BUTTON3_PACKET, ACTION_BUTTON4_PACKET, ACTION_BUTTON5_PACKET, ACTION_BUTTON6_PACKET, ACTION_BUTTON7_PACKET, ACTION_BUTTON8_PACKET, ACTION_BUTTON9_PACKET, ACTION_BUTTON10_PACKET, SWITCH_INTERFACE_ITEM_PACKET);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
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
					return;
				}
				player.stopAll();
				break;
			case IN_OUT_SCREEN_PACKET:
				// not using this check because not 100% efficient
				@SuppressWarnings("unused") boolean inScreen = stream.readByte() == 1;
				break;
			case SCREEN_PACKET:
				int displayMode = stream.readUnsignedByte();
				player.setScreenWidth(stream.readUnsignedShort());
				player.setScreenHeight(stream.readUnsignedShort());
				@SuppressWarnings("unused") boolean switchScreenMode = stream.readUnsignedByte() == 1;
				if (!player.hasStarted() || player.hasFinished() || displayMode == player.getDisplayMode() || !player.getInterfaceManager().containsInterface(742)) {
					return;
				}
				player.setDisplayMode(displayMode);
				player.getInterfaceManager().removeAll();
				player.getInterfaceManager().sendInterfaces();
				player.getInterfaceManager().sendInterface(742);
				break;
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
				ButtonHandler.decodeInterfaceStream(player, stream, packetId);
				break;
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
					return;
				}
				if (!player.getInterfaceManager().containsInterface(fromInterfaceId) || !player.getInterfaceManager().containsInterface(toInterfaceId)) {
					return;
				}
				if (fromComponentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId) {
					return;
				}
				if (toComponentId != -1 && Misc.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId) {
					return;
				}
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
				System.out.println("Switch item " + fromInterfaceId + ", " + fromSlot + ", " + toSlot);
				break;
		}
	}
}
