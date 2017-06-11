package org.redrune.network;

import org.jboss.netty.channel.ChannelFuture;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.client.MasterClientHandler;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.utility.Misc;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * This classs handles the communication between the RS2 server and the master server
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class RS2MasterCommunication {
	
	/**
	 * The logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(RS2MasterCommunication.class);
	
	/**
	 * The list of sessions
	 */
	private static final CopyOnWriteArrayList<NetworkSession> NETWORK_SESSIONS = new CopyOnWriteArrayList<>();
	
	/**
	 * Writes a master packet
	 *
	 * @param packet
	 * 		The packet to write
	 */
	public static ChannelFuture writeMasterPacket(MasterPacket packet) {
		if (!loginServerOnline()) {
			return null;
		}
		return MasterClientHandler.getSession().write(packet);
	}
	
	/**
	 * Checks if the login server is online
	 */
	public static boolean loginServerOnline() {
		if (!MasterClientHandler.isConnected()) {
			LOGGER.info("Attempted to write packet before the connection was initialized.");
			return false;
		}
		if (MasterClientHandler.getSession() == null) {
			LOGGER.info("Attempted to write a packet before the channel session was stored.");
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the session by the uid
	 *
	 * @param uid
	 * 		The session uid
	 */
	public static NetworkSession getSession(long uid) {
		Optional<NetworkSession> optional = NETWORK_SESSIONS.stream().filter(session -> session.getUid() == uid).findFirst();
		return optional.orElse(null);
	}
	
	/**
	 * Gets the sessions list
	 */
	public static CopyOnWriteArrayList<NetworkSession> getNetworkSessions() {
		return NETWORK_SESSIONS;
	}
}
