package org.redrune.network.master.packet.out.server.context;

import lombok.Getter;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public final class ServerFriendResponseContext implements MasterPacketContext {
	
	/**
	 * The uid of the session
	 */
	@Getter
	private final long uid;
	
	/**
	 * The name of the friend
	 */
	@Getter
	
	private final String name;
	
	/**
	 * The response received
	 */
	@Getter
	private final int response;
	
	public ServerFriendResponseContext(long uid, String name, int response) {
		this.uid = uid;
		this.name = name;
		this.response = response;
	}
	
}
