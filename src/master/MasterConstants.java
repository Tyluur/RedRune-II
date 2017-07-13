package master;

import com.google.common.collect.ImmutableList;
import io.netty.util.AttributeKey;
import master.network.MasterSession;

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
	int PORT_ID = 5555;
	
	/**
	 * The message you get when you connect successfully and were verified
	 */
	String WELCOME_MESSAGE = "Welcome to the RedRune MS, world #! Your connection has been verified :)";
	
	/**
	 * The keys
	 */
	String[] KEYS = new String[] { "World1Key", "World2Key" };
	
	/**
	 * The attribute that contains the key for a session.
	 */
	AttributeKey<MasterSession> SESSION_KEY = AttributeKey.valueOf("session.key");
	
	/**
	 * The list of exceptions that are ignored
	 */
	ImmutableList<String> IGNORED_EXCEPTIONS = ImmutableList.of("An existing connection was forcibly closed by the remote host", "An established connection was aborted by the software in your host machine");
}
