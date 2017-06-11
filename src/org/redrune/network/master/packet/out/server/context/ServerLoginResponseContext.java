package org.redrune.network.master.packet.out.server.context;

import lombok.Getter;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public class ServerLoginResponseContext implements MasterPacketContext {
	
	/**
	 * The uid of the session
	 */
	@Getter
	private final long uid;
	
	/**
	 * The username attempting to log in
	 */
	@Getter
	private final String username;
	
	/**
	 * The password we're attempting to log in with
	 */
	@Getter
	private final String password;
	
	/**
	 * If we're connecting to the lobby
	 */
	@Getter
	private final boolean lobbyConnection;
	
	/**
	 * The response code of the login request
	 */
	@Getter
	private final int responseCode;
	
	public ServerLoginResponseContext(long uid, String username, String password, boolean lobbyConnection, int responseCode) {
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.lobbyConnection = lobbyConnection;
		this.responseCode = responseCode;
	}
}
