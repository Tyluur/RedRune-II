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
	 * The port that the login server listens on
	 */
	int PORT = 8000;
	
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
	 * The packet that is sent from the rs2 server to the master server saying that the client has disconnected
	 */
	int DISCONNECTION_CLIENT_PACKET = 3;
	
	/**
	 * The packet that is sent from the rs2 server to the master server, saying that we need an update on the statistics
	 * client sided.
	 */
	int STATISTICS_REQUEST_CLIENT_PACKET = 4;
	
	/**
	 * The packet that is sent from the master server to the master client, with information about the worlds
	 */
	int STATISTICS_SEND_SERVER_PACKET = 5;
	
}
