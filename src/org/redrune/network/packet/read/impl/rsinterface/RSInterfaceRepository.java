package org.redrune.network.packet.read.impl.rsinterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.redrune.rs2.node.entity.player.Player;

public class RSInterfaceRepository {

	private final static List<RSInterface> INTERFACES = new ArrayList<RSInterface>();

	public static void loadInterfaces() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		File[] files = new File("./src/ordinance/rs751/network/packet/read/impl/rsinterface/impl/").listFiles();
		for (File file : files) {
			INTERFACES.add((RSInterface) Class.forName(
					"org.redrune.network.packet.read.impl.rsinterface.impl." + file.getName().replace(".java", ""))
					.newInstance());
		}
	}

	public static void handle(Player player, int interfaceId, int buttonId, int slotId, int itemId, int packetId) {
		RSInterface inter = getInterface(interfaceId);
		if (inter == null) {
			System.err.println("Unhandled RS Interface - Interface: " + interfaceId + ", Button: " + buttonId
					+ ", Slot: " + slotId + ", Item ID: " + itemId + ", Packet: " + packetId);
			return;
		}
		inter.handleInterface(player, interfaceId, buttonId, slotId, itemId, packetId);
	}

	public static RSInterface getInterface(int interfaceId) {
		for (RSInterface rsInterface : INTERFACES) {
			for (int id : rsInterface.getPossibleInterfaces()) {
				if (id == interfaceId) {
					return rsInterface;
				}
			}
		}
		return null;
	}

	public static List<RSInterface> getInterfaces() {
		return INTERFACES;
	}

}
