package org.redrune.network;

import org.jboss.netty.channel.ChannelFuture;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.client.MasterClientHandler;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.network.rs666.packet.outgoing.impl.FriendsListBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.PrivateMessageReceiveBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.PrivateMessageSendBuilder;
import org.redrune.utility.Misc;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.redrune.network.master.MasterConstants.*;

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
		if (packet.getOpcode() != CLIENT_VERIFICATION_PACKET && !MasterClientHandler.isVerified()) {
			System.out.println("Attempted to write packet #" + packet.getOpcode() + " before we were verified.");
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
	 * Reads an incoming packet
	 *
	 * @param uid
	 * 		The uid of the packet
	 * @param packet
	 * 		The packet
	 * @param params
	 * 		Addition varags parameters
	 */
	public static void readPacket(long uid, MasterPacket packet, Object... params) {
		try {
			NetworkSession session = getSession(uid);
			if (session == null) {
				System.out.println("Unable to find session by uid " + uid + ", quitting.");
				return;
			}
			final int opcode = packet.getOpcode();
			if (opcode == SERVER_FRIEND_DATA_PACKET) {
				String name = (String) params[0];
				int response = (int) params[1];
				
				FriendsListBuilder builder = new FriendsListBuilder(name, "", response >= 0 ? response : -1, 0, false, response == 0, response >= 0);
				session.write(builder.build(null));
			} else if (opcode == SERVER_PRIVATE_MESSAGE_SENDER_PACKET) {
				String toUsername = (String) params[1];
				String message = (String) params[2];
				boolean successful = (boolean) params[3];
				
				if (!successful) {
					return;
				}
				session.write(new PrivateMessageSendBuilder(toUsername, message).build(null));
			} else if (opcode == SERVER_PRIVATE_MESSAGE_RECEIVER_PACKET) {
				String fromUsername = (String) params[0];
				int fromRights = (int) params[1];
				String message = (String) params[3];
				boolean successful = (boolean) params[4];
				
				if (!successful) {
					return;
				}
				session.write(new PrivateMessageReceiveBuilder(fromUsername, message, fromRights).build(null));
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets all the active sessions.
	 */
	public static List<NetworkSession> getActiveSessions() {
		return NETWORK_SESSIONS.stream().filter(session -> session.isActive() && session.getPlayer() != null).collect(Collectors.toList());
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
