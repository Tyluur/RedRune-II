package org.redrune.network.master.packet.out.server.context;

import lombok.Getter;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ServerPlayerLoginContext implements MasterPacketContext {
	
	/**
	 * The username of the player
	 */
	@Getter
	private final String username;
	
	/**
	 * The id of the world the player is in (0 = lobby)
	 */
	@Getter
	private final int worldId;
	
	public ServerPlayerLoginContext(String username, int worldId) {
		this.username = username;
		this.worldId = worldId;
	}
}
