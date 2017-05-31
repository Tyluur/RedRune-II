package org.redrune.network.rs666.packet.structure.in;

import org.redrune.cache.Cache;
import org.redrune.game.module.ModuleRepository;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.utility.Misc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/22/2017
 */
public class InterfaceClickPacketStructure implements IncomingPacketStructure {
	
	/**
	 * The logger instance
	 */
	private final Logger logger = Misc.constructLogger(InterfaceClickPacketStructure.class);
	
	@Override
	public int[] bindings() {
		return Misc.arguments(85, 7, 66, 11, 48, 17, 84, 40, 25, 8, 54);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int clickData = packet.readLEInt();
		int interfaceId = clickData & 0xFFF;
		int componentId = clickData >> 16;
		int itemId = packet.readShortA();
		int slotId = packet.readShortA();
		if (itemId == 65535) {
			itemId = -1;
		}
		if (slotId == 65535) {
			slotId = -1;
		}
		if (interfaceId > Cache.getAmountOfInterfaces()) {
			logger.log(Level.SEVERE, "Unable to handle interface post-decoding!");
			return;
		}
		if (!player.getManager().getInterfaces().hasInterfaceOpen(interfaceId)) {
			logger.log(Level.SEVERE, "Interface " + interfaceId + " was not existent in the player's mapping of opened interface.");
			return;
		}
		if (ModuleRepository.handle(player, interfaceId, componentId, itemId, slotId, packet.getOpcode())) {
			return;
		}
		StringBuilder bldr = new StringBuilder("[interfaceId=" + interfaceId + ", componentId=" + componentId + "");
		bldr.append(itemId == -1 ? "" : ", itemId=" + itemId + "");
		bldr.append(slotId == -1 ? "" : ", slotId=" + slotId + "");
		bldr.append(", packetId=").append(packet.getOpcode()).append("]");
		System.out.println(bldr.toString());
	}
}
