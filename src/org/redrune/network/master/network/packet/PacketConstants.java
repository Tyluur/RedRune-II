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
}
