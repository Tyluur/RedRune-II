package org.redrune.network.master.packet.out.client.context;

import lombok.Getter;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class ClientFriendRequestContext implements MasterPacketContext {
	
	/**
	 * The uid of the session
	 */
	@Getter
	private final long uid;
	
	/**
	 * The name of the friend we want to add to our friend list
	 */
	@Getter
	private final String name;
	
	public ClientFriendRequestContext(long uid, String name) {
		this.uid = uid;
		this.name = name;
	}
}
