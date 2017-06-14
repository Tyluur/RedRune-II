package org.redrune.network.master;

/**
 * The constants for the master server
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public interface MasterConstants {
	
	/**
	 * The host for the login server
	 */
	String HOST = "localhost";
	
	/**
	 * The location of the player saves
	 */
	String SAVE_LOCATION = "./data/saves/";
	
	/**
	 * The master server password
	 */
	String PASSWORD = "REDRUNE_MASTER_PASSWORD";
	
	/**
	 * The port that the login server listens on
	 */
	int PORT = 43593;
	
	/**
	 * The packet the client will send to the server with verification details.
	 */
	int CLIENT_VERIFICATION_PACKET = 200;
	
	/**
	 * The packet the server will send to the client saying that we were verified
	 */
	int SERVER_VERIFICATION_PACKET = 201;
	
	/**
	 * The file update packet, saying we should update the save on file.
	 */
	int CLIENT_FILE_UPDATE_PACKET = 203;
	
	/**
	 * The opcode that identifies the packet that is sent from the rs2 server to the master server with login
	 * information.
	 */
	int LOGIN_INFORMATION_CLIENT_PACKET = 1;
	
	/**
	 * The opcode that identifies the packet that is sent from the master server to the master client, with information
	 * about the success of the login attempt.
	 */
	int LOGIN_INFORMATION_SERVER_PACKET = 2;
	
	/**
	 * The packet that is sent from the rs2 server to the master server saying that the client has disconnected has this
	 * opcode
	 */
	int DISCONNECTION_CLIENT_PACKET = 3;
	
	/**
	 * The packet that is sent from the rs2 server to the master server, saying that we need an update on the statistics
	 * client sided, has this opcode
	 */
	int STATISTICS_REQUEST_CLIENT_PACKET = 4;
	
	/**
	 * The packet that is sent from the master server to the master client, with information about the worlds has this
	 * opcode
	 */
	int STATISTICS_SEND_SERVER_PACKET = 5;
	
	/**
	 * The opcode that symbolizes the packet going to the server, requesting information about a friend
	 */
	int CLIENT_FRIEND_REQUEST_PACKET = 6;
	
	/**
	 * The opcode that symbolizes the packet going to the client with data about the friend that was requested.
	 */
	int SERVER_FRIEND_DATA_PACKET = 7;
	
	/**
	 * The opcode that symbolizes the packet going to the server with data about a private message being sent
	 */
	int CLIENT_PRIVATE_MESSAGE_PACKET = 8;
	
	/**
	 * The opcode that identifies that packet going to the master client, after a pm request is received at the server
	 * end. This one says that we should show the sender the private message.
	 */
	int SERVER_PRIVATE_MESSAGE_SENDER_PACKET = 9;
	
	/**
	 * The opcode that identifies that packet going to the master client, after a pm request is received at the server
	 * end. This one says that we should show the receiver the private message.
	 */
	int SERVER_PRIVATE_MESSAGE_RECEIVER_PACKET = 10;
	
	/**
	 * The opcode that identifies the packet sent to the master client, with information about the player who just
	 * logged out.
	 */
	int SERVER_PLAYER_LOGOUT_PACKET = 11;
	
	/**
	 * The opcode that identifies the packet sent to the master clients, letting them know that a player logged in.
	 */
	int SERVER_PLAYER_LOGIN_PACKET = 12;
	
}