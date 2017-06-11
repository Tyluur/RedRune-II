package org.redrune.network.master.packet.out.client.context;

import lombok.Getter;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ClientLoginResponseContext implements MasterPacketContext {
	
	/**
	 * The uid of the session
	 */
	@Getter
	private final long uid;
	
	/**
	 * If the connection is to the lobby
	 */
	@Getter
	private final boolean lobbyConnection;
	
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
	 * The world attempting to connect to
	 */
	@Getter
	private final int worldId;
	
	public ClientLoginResponseContext(long uid, String username, String password, boolean lobbyConnection, int worldId) {
		this.uid = uid;
		this.lobbyConnection = lobbyConnection;
		this.username = username;
		this.password = password;
		this.worldId = worldId;
	}
}
