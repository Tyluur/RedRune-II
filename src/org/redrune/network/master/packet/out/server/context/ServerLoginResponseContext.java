package org.redrune.network.master.packet.out.server.context;

import lombok.Getter;
import lombok.Setter;
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
	@Setter
	private int responseCode;
	
	/**
	 * The text of the file in json
	 */
	@Getter
	@Setter
	private String fileJsonText = "null";
	
	public ServerLoginResponseContext(long uid, String username, String password, boolean lobbyConnection, int responseCode) {
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.lobbyConnection = lobbyConnection;
		this.responseCode = responseCode;
	}
}
