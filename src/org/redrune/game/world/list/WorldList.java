package org.redrune.game.world.list;

import org.redrune.core.master.client.MasterClientRepository;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.utility.rs.constant.WorldConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all the current worlds.
 *
 * @author Dementhium development team
 */
public class WorldList implements WorldConstants {
	
	/**
	 * A list holding all the currently loaded worlds.
	 */
	private static final List<WorldDefinition> WORLD_LIST = new ArrayList<>();
	
	/* Populates the world list. */
	static {
		WORLD_LIST.add(new WorldDefinition(1, 0, FLAG_MEMBERS | FLAG_LOOTSHARE, "Main World", "127.0.0.1", "USA", COUNTRY_CANADA));
		WORLD_LIST.add(new WorldDefinition(2, 0, FLAG_MEMBERS | FLAG_PVP | FLAG_LOOTSHARE, "PvP World", "127.0.0.1", "USA", COUNTRY_USA));
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
		buffer.writeSmart(WORLD_LIST.size());
		setCountry(buffer);
		buffer.writeSmart(0);
		buffer.writeSmart(WORLD_LIST.size() + 1);
		buffer.writeSmart(WORLD_LIST.size());
		for (WorldDefinition w : WORLD_LIST) {
			buffer.writeSmart(w.getWorldId());
			buffer.writeByte(w.getLocation());
			buffer.writeInt(w.getFlag());
			buffer.writeGJString(MasterClientRepository.isOnline(w.getWorldId()) ? w.getActivity() : "OFFLINE");
			buffer.writeGJString(MasterClientRepository.isOnline(w.getWorldId()) ? w.getIp() : "0.0.0.0");
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
		for (WorldDefinition w : WORLD_LIST) {
			buffer.writeSmart(w.getWorldId());
			buffer.writeShort(MasterClientRepository.isOnline(w.getWorldId()) ? MasterClientRepository.getPlayerCount(w.getWorldId()) : 0);
		}
	}
	
	/**
	 * Sets the countries for each world.
	 *
	 * @param buffer
	 * 		The current packet.
	 */
	private static void setCountry(PacketBuilder buffer) {
		for (WorldDefinition w : WORLD_LIST) {
			buffer.writeSmart(w.getCountry());
			buffer.writeGJString(w.getRegion());
		}
	}
}
