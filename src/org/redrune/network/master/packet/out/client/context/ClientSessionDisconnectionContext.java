package org.redrune.network.master.packet.out.client.context;

import lombok.Getter;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class ClientSessionDisconnectionContext implements MasterPacketContext {
	
	/**
	 * The uid of the session
	 */
	@Getter
	private final long uid;
	
	/**
	 * If the session was connected to the lobby
	 */
	@Getter
	private final boolean lobby;
	
	/**
	 * The username of the player, if exists
	 */
	@Getter
	private final String username;
	
	public ClientSessionDisconnectionContext(long uid, boolean lobby, String username) {
		this.uid = uid;
		this.lobby = lobby;
		this.username = username;
	}
}
