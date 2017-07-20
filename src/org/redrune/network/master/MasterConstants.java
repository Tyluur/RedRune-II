package org.redrune.network.master;

import io.netty.util.AttributeKey;
import org.redrune.network.NetworkConstants;
import org.redrune.network.master.network.MasterSession;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/10/2017
 */
public interface MasterConstants {
	
	/**
	 * The ip of the master server to connect to
	 */
	String IP = "127.0.0.1";
	
	/**
	 * The id of the port we're listening on
	 */
	int PORT_ID = NetworkConstants.MASTER_SERVER_PORT_ID;
	
	/**
	 * The message you get when you connect successfully and were verified
	 */
	String WELCOME_MESSAGE = "Welcome to the RedRune MS, world #! Your connection has been verified :)";
	
	/**
	 * The keys
	 */
	String[] KEYS = new String[] { "LOBBY PASSWORD", "WORLD 1 PASSWORD", "WORLD 2 PASSWORD" };
	
	/**
	 * The attribute that contains the key for a session.
	 */
	AttributeKey<MasterSession> SESSION_KEY = AttributeKey.valueOf("session.key");
	
	/**
	 * The status values for friend communication
	 */
	byte ONLINE_STATUS = 0, FRIENDS_STATUS = 1, OFFLINE_STATUS = 2, LOBBY_STATUS = 3;
	
	/**
	 * The id of the lobby world
	 */
	byte LOBBY_WORLD_ID = 0;
}
