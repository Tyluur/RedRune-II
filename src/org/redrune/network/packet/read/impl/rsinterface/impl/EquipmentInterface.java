package org.redrune.network.packet.read.impl.rsinterface.impl;

import org.redrune.network.packet.read.impl.rsinterface.RSInterface;
import org.redrune.rs2.node.entity.player.Player;

public class EquipmentInterface implements RSInterface {

	@Override
	public void handleInterface(Player player, int interfaceId, int buttonId, int slotId, int itemId, int packetId) {
		switch (buttonId) {
//		case 40:// Equipment Bonuses
//			player.getInterfaceManager().sendInterface(667);
//			break;
//		case 7:// Hat
//			player.getEquipment().removeItem(EquipmentSlot.HAT);
//			break;
//		case 10:// Cape
//			player.getEquipment().removeItem(EquipmentSlot.CAPE);
//			break;
//		case 13:// Neck
//			player.getEquipment().removeItem(EquipmentSlot.AMULET);
//			break;
//		case 16:// Main Hand
//			player.getEquipment().removeItem(EquipmentSlot.MAIN_HAND);
//			break;
//		case 19:// Chest
//			player.getEquipment().removeItem(EquipmentSlot.CHEST);
//			break;
//		case 22:// Off Hand
//			player.getEquipment().removeItem(EquipmentSlot.OFF_HAND);
//			break;
//		case 25:// Legs
//			player.getEquipment().removeItem(EquipmentSlot.LEGS);
//			break;
//		case 28:// Gloves
//			player.getEquipment().removeItem(EquipmentSlot.HANDS);
//			break;
//		case 31:// Feet
//			player.getEquipment().removeItem(EquipmentSlot.FEET);
//			break;
//		case 34:// Ring
//			player.getEquipment().removeItem(EquipmentSlot.RING);
//			break;
//		case 39:// Ammo
//			player.getEquipment().removeItem(EquipmentSlot.AMMO);
//			break;
//		case 48:// Aura
//			player.getEquipment().removeItem(EquipmentSlot.AURA);
//			break;
		}
	}

	@Override
	public int[] getPossibleInterfaces() {
		return new int[] { 387 };
	}

}
