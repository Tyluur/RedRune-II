package org.redrune.network.master.packet.out.server.context;

import lombok.Getter;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ServerPlayerLogoutContext implements MasterPacketContext {
	
	/**
	 * The username of the player that logged out
	 */
	@Getter
	private final String username;
	
	/**
	 * If the player that logged out logged out from the lobby
	 */
	@Getter
	private final boolean lobby;
	
	public ServerPlayerLogoutContext(String username, boolean lobby) {
		this.username = username;
		this.lobby = lobby;
	}
}
