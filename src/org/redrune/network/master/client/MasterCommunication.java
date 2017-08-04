package org.redrune.network.master.client;

import org.redrune.game.GameFlags;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.NetworkSession;
import org.redrune.network.lobby.packet.readable.LobbyRepositoryPacketIn;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.client.network.MCNetworkSystem;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.network.packet.OutgoingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.world.WorldSession;
import org.redrune.network.world.packet.outgoing.impl.LoginResponseCodeBuilder;
import org.redrune.network.world.packet.outgoing.impl.PrivateMessageReceiveBuilder;
import org.redrune.network.world.packet.outgoing.impl.PrivateMessageSendBuilder;
import org.redrune.utility.tool.Misc;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class MasterCommunication implements PacketConstants {
	
	/**
	 * The instance of the network system
	 */
	private static final MCNetworkSystem SYSTEM = new MCNetworkSystem(GameFlags.worldId);
	
	/**
	 * Starts the communication
	 */
	public static void start() {
		SYSTEM.connect();
		if (GameFlags.worldId == MasterConstants.LOBBY_WORLD_ID) {
			MCSession.getReadableRepository().include(new LobbyRepositoryPacketIn());
		}
	}
	
	/**
	 * Writes an outgoing packet to the master server
	 *
	 * @param packet
	 * 		The packet to write
	 */
	public static void write(OutgoingPacket packet) {
		SYSTEM.write(packet);
	}
	
	/**
	 * Reads packets that the client receives back
	 *
	 * @param packetId
	 * 		The id of the packet
	 * @param params
	 * 		Var-ags parameters
	 */
	public static void read(int packetId, Object... params) {
		switch (packetId) {
			case LOGIN_RESPONSE_PACKET_ID:
				handleLoginResponse((String) params[0], (String) params[1], (String) params[2], (byte) params[3], (boolean) params[4]);
				break;
		}
	}
	
	/**
	 * Handles the login response
	 *
	 * @param uid
	 * 		The uid of the login
	 * @param username
	 * 		The name of the player logging in
	 * @param fileText
	 * 		The text
	 * @param responseCode
	 * 		The response code
	 * @param lobby
	 * 		If the player is headed to the lobby
	 */
	private static void handleLoginResponse(String uid, String username, String fileText, byte responseCode, boolean lobby) {
		Optional<NetworkSession> optional = NetworkSession.findByUid(uid);
		if (!optional.isPresent()) {
			System.err.println("Unable to find session by id " + uid);
			return;
		}
		NetworkSession session = optional.get();
		
		// sends the response code
		session.write(new LoginResponseCodeBuilder(responseCode).build(null));
		
		// simply show the response
		if (responseCode != 2) {
			return;
		}
		
		Player player;
		if (fileText == null || fileText.equals("empty")) {
			player = new Player(username);
			System.out.println("Created a new player, fileText=" + fileText);
		} else {
			player = Misc.loadPlayer(fileText);
			System.out.println("loaded a player from the file!");
		}
		
		if (player == null) {
			System.err.println("Unable to read file text for user '" + username + "'.");
			session.getChannel().close();
			return;
		}
		
		if (!(session instanceof WorldSession)) {
			System.out.println("Session was not a world session instance on login attempt...");
			return;
		}
		
		try {
			// syncs the session variables
			((WorldSession) session).sync(player);
			
			if (lobby) {
				player.registerToLobby();
			} else {
				player.register();
			}
		} catch (Exception e) {
			session.getChannel().close();
			e.printStackTrace();
		}
		
		System.out.println("Registered player! Lobby = " + lobby + ", session=" + session);
	}
	
	/**
	 * Handles the private message being delivered and received
	 *
	 * @param fromName
	 * 		The name of the person the message is from
	 * @param toName
	 * 		The name of the person the message is to
	 * @param message
	 * 		The message
	 */
	private static void handlePrivateMessageDelivery(String fromName, String toName, String message) {
		Optional<NetworkSession> optional = NetworkSession.findByName(fromName);
		if (!optional.isPresent()) {
			System.err.println("Unable to find session by name " + fromName);
			return;
		}
		NetworkSession session = optional.get();
		session.write(new PrivateMessageSendBuilder(toName, message).build(null));
	}
	
	/**
	 * Handles receiving a private message
	 *
	 * @param fromName
	 * 		The name of the player the message is from
	 * @param fromRights
	 * 		The rights of the player the message is from
	 * @param toName
	 * 		The name of the player the message is to
	 * @param message
	 * 		The message
	 */
	private static void handlePrivateMessageReceive(String fromName, byte fromRights, String toName, String message) {
		Optional<NetworkSession> optional = NetworkSession.findByName(toName);
		if (!optional.isPresent()) {
			System.err.println("Unable to find session by name " + toName);
			return;
		}
		NetworkSession session = optional.get();
		session.write(new PrivateMessageReceiveBuilder(fromName, message, fromRights).build(null));
	}
	
	private static void handleContactDetails(String uid, String username, byte worldId, byte status) {
		// TODO this
		System.out.println("MasterCommunication.handleContactDetails");
		System.out.println("uid = [" + uid + "], username = [" + username + "], worldId = [" + worldId + "], status = [" + status + "]");
/*		Optional<NetworkSession> optional = NetworkSession.findByUid(uid);
		if (!optional.isPresent()) {
			System.err.println("Unable to find session by uid " + uid);
			return;
		}
		NetworkSession session = optional.get();
		if (session.getPlayer() == null) {
			System.err.println("Unable to identify player for session " + session);
			return;
		}
		session.getPlayer().getManager().getContacts().updateContact(username, worldId, status);
		System.err.println("uid = [" + uid + "], username = [" + username + "], worldId = [" + worldId + "], status = [" + status + "]");
		System.err.println("uid = [" + uid + "], username = [" + username + "], worldId = [" + worldId + "], status = [" + status + "]");*/
	}
	
	/**
	 * Handles the user updating their status
	 *
	 * @param username
	 * 		The user
	 * @param status
	 * 		The new status
	 */
	private static void handleStatusUpdate(String username, byte worldId, byte status) {
		for (Player player : World.get().getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getManager().getContacts().hasFriend(username)) {
				player.getManager().getContacts().updateContact(username, worldId, status);
			}
		}
	}
	
	/**
	 * Checks that we are connected to the master server
	 */
	public static boolean isConnected() {
		if (SYSTEM.getSession() == null) {
			return false;
		}
		// if we had a session we need to make sure its still connected
		return SYSTEM.getSession().isConnected();
	}
	
}