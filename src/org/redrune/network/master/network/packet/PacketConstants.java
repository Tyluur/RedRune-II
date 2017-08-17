package org.redrune.network.master.network.packet;

import org.redrune.network.master.MasterConstants;

/**
 * All constant packet data is put here
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public interface PacketConstants extends MasterConstants {
	
	/**
	 * The id of the verification packet
	 */
	int VERIFICATION_ATTEMPT_PACKET_ID = 1;
	
	/**
	 * The id of the successful verification packet
	 */
	int SUCCESSFUL_VERIFICATION_PACKET_ID = 2;
	
	/**
	 * The id of the login request packet
	 */
	int LOGIN_REQUEST_PACKET_ID = 3;
	
	/**
	 * The id of the login response packet
	 */
	int LOGIN_RESPONSE_PACKET_ID = 4;
	
	/**
	 * The id of the disconnection packet
	 */
	int PLAYER_DISCONNECTION_PACKET_ID = 5;
	
	/**
	 * The id of the player file update packet
	 */
	int PLAYER_FILE_UPDATE_PACKET_ID = 6;
	
	/**
	 * The id of the repository update packet
	 */
	int REPOSITORY_UPDATE_PACKET_ID = 7;
	
	/**
	 * The id of the reconnection packet
	 */
	int RECONNECTION_PACKET_ID = 8;
	
	/**
	 * The id of the packet that contacts data about the player requesting friend information
	 */
	int FRIEND_DETAILS_REQUEST_PACKET_ID = 9;
	
	/**
	 * The id of the packet that contains the details of a friend
	 */
	int FRIEND_DETAILS_COMPLETE_PACKET_ID = 10;
	
	/**
	 * The id of the packet sent to the master server saying a player updated their status
	 */
	int FRIEND_STATUS_CHANGE_RECEIVE_PACKET_ID = 11;
	
	/**
	 * The id of the packet sent to all master clients saying a player updated their status
	 */
	int FRIEND_STATUS_CHANGE_DELIVER_PACKET_ID = 12;
	
	/**
	 * The id of the packet sent to the master server with information about a private message to be delivered
	 */
	int PRIVATE_MESSAGE_ATTEMPT_PACKET_ID = 13;
	
	/**
	 * The id of the packet that is sent to the player who sent the private message
	 */
	int PRIVATE_MESSAGE_DELIVERY_PACKET_ID = 14;
	
	/**
	 * The id of the packet that is sent to the player who receives the private message
	 */
	int PRIVATE_MESSAGE_RECEIVED_PACKET_ID = 15;
	
	/**
	 * The id of the packet that contains an account creation request
	 */
	int ACCOUNT_CREATION_REQUEST_PACKET_ID = 16;
	
	/**
	 * The id of the packet that contains the account creation response data
	 */
	int ACCOUNT_CREATION_RESPONSE_PACKET_ID = 17;
}
