package org.redrune.network.master.packet.out.client.context;

import lombok.Getter;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ClientVerificationPacketContext implements MasterPacketContext{
	
	/**
	 * The id of the world
	 */
	@Getter
	private final int worldId;
	
	/**
	 * The password to ensure connection
	 */
	@Getter
	private final String password;
	
	public ClientVerificationPacketContext(int worldId, String password) {
		this.worldId = worldId;
		this.password = password;
	}
}
