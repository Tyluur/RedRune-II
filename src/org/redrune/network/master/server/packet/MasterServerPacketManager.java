package org.redrune.network.master.server.packet;

import org.jboss.netty.channel.Channel;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.master.packet.in.client.ClientDisconnectionReader;
import org.redrune.network.master.packet.in.server.ServerLoginResponseReader;
import org.redrune.network.master.packet.in.server.ServerStatisticsRequestReader;
import org.redrune.utility.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class MasterServerPacketManager implements MasterConstants {
	
	/**
	 * The map for packets
	 */
	private static final Map<Integer, MasterPacketReader> BINDINGS = new HashMap<>();
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(MasterServerPacketManager.class);
	
	static {
		BINDINGS.put(LOGIN_INFORMATION_CLIENT_PACKET, new ServerLoginResponseReader());
		BINDINGS.put(DISCONNECTION_CLIENT_PACKET, new ClientDisconnectionReader());
		BINDINGS.put(STATISTICS_REQUEST_CLIENT_PACKET, new ServerStatisticsRequestReader());
	}
	
	/**
	 * Reads an incoming packet
	 *
	 * @param channel
	 * 		The channel that received the packet
	 * @param packet
	 * 		The packet
	 */
	public static boolean read(Channel channel, MasterPacket packet) {
		int opcode = packet.readByte();
		MasterPacketReader reader = BINDINGS.get(opcode);
		if (reader == null) {
			LOGGER.info("Unable to find reader by opcode #" + opcode);
			return false;
		}
		reader.read(channel, packet);
		return true;
	}
	
}
