package org.redrune.rs2.world.list;

import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all the current worlds.
 *
 * @author Dementhium development team
 */
public class WorldList {
	
	/**
	 * A list holding all the currently loaded worlds.
	 */
	private static final List<WorldDefinition> WORLD_LIST = new ArrayList<>();
	
	/* Populates the world list. */
	static {
		WORLD_LIST.add(new WorldDefinition(1, WorldConstants.COUNTRY_CANADA, WorldConstants.FLAG_MEMBERS, "World 1", "127.0.0.1", "USA", WorldConstants.COUNTRY_CANADA));
		WORLD_LIST.add(new WorldDefinition(2, WorldConstants.COUNTRY_CANADA, WorldConstants.FLAG_PVP, "PvP World", "127.0.0.1", "USA", WorldConstants.COUNTRY_CANADA));
	}
	
	/**
	 * Gets the packet to update the world list in the lobby.
	 *
	 * @param worldConfiguration
	 * 		If the configuration should be added.
	 * @param worldStatus
	 * 		If the status should be added.
	 * @return The {@code OutgoingPacket} to write.
	 */
	public static PacketBuilder getData(boolean worldConfiguration, boolean worldStatus) {
		PacketBuilder bldr = new PacketBuilder(23, PacketType.VAR_SHORT);
		bldr.writeByte(1);
		bldr.writeByte(2);
		bldr.writeByte(1);
		if (worldConfiguration) {
			populateConfiguration(bldr);
		}
		if (worldStatus) {
			populateStatus(bldr);
		}
		return bldr;
	}
	
	/**
	 * Adds the world configuration on the packet.
	 *
	 * @param buffer
	 * 		The current packet.
	 */
	private static void populateConfiguration(PacketBuilder buffer) {
		buffer.writeSmart(getWorldList().size());
		setCountry(buffer);
		buffer.writeSmart(0);
		buffer.writeSmart(getWorldList().size() + 1);
		buffer.writeSmart(getWorldList().size());
		for (WorldDefinition w : getWorldList()) {
			buffer.writeSmart(w.getWorldId());
			buffer.writeByte(w.getLocation());
			buffer.writeInt(w.getFlag());
			buffer.writeGJString(w.getActivity());
			buffer.writeGJString(w.getIp());
		}
		buffer.writeInt(0x94DA4A87);
	}
	
	/**
	 * Adds the world status on the packet.
	 *
	 * @param buffer
	 * 		The current packet.
	 */
	private static void populateStatus(PacketBuilder buffer) {
		for (WorldDefinition w : getWorldList()) {
			buffer.writeSmart(w.getWorldId());
			buffer.writeShort(1337);
		}
	}
	
	private static List<WorldDefinition> getWorldList() {
		List<WorldDefinition> list = new ArrayList<>();
		
		list.add(new WorldDefinition(1, 0, WorldConstants.FLAG_MEMBERS | WorldConstants.FLAG_LOOTSHARE, "Main World", "127.0.0.1", "USA", WorldConstants.COUNTRY_CANADA));
		list.add(new WorldDefinition(2, 0, WorldConstants.FLAG_MEMBERS | WorldConstants.FLAG_PVP | WorldConstants.FLAG_LOOTSHARE, "PvP World", "127.0.0.1", "USA", WorldConstants.COUNTRY_USA));
		return list;
	}
	
	/**
	 * Sets the countries for each world.
	 *
	 * @param buffer
	 * 		The current packet.
	 */
	private static void setCountry(PacketBuilder buffer) {
		for (WorldDefinition w : getWorldList()) {
			buffer.writeSmart(w.getCountry());
			buffer.writeGJString(w.getRegion());
		}
	}
}
