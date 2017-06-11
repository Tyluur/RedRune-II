package org.redrune.network.master.client.packet;

import org.jboss.netty.channel.Channel;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.network.master.packet.in.client.ClientLoginResponseReader;
import org.redrune.network.master.packet.in.client.ClientStatisticsReader;
import org.redrune.utility.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class MasterClientPacketManager implements MasterConstants {
	
	/**
	 * The map for packets
	 */
	private static final Map<Integer, MasterPacketReader> BINDINGS = new HashMap<>();
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(MasterClientPacketManager.class);
	
	static {
		BINDINGS.put(LOGIN_INFORMATION_SERVER_PACKET, new ClientLoginResponseReader());
		BINDINGS.put(STATISTICS_SEND_SERVER_PACKET, new ClientStatisticsReader());
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
