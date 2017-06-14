package org.redrune.network.master.packet.out.server.context;

import lombok.Getter;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ServerPrivateMessageContext implements MasterPacketContext {
	
	/**
	 * The uid of the player's session that is sending the message
	 */
	@Getter
	private final long uid;
	
	/**
	 * The username the message is coming from
	 */
	@Getter
	private final String fromUsername;
	
	/**
	 * The rights of the user coming from
	 */
	@Getter
	private final int fromRights;
	
	/**
	 * The username the message is sent to
	 */
	@Getter
	private final String toUsername;
	
	/**
	 * The message that is sent
	 */
	@Getter
	private final String message;
	
	/**
	 * If the message should be successfully sent
	 */
	@Getter
	private final boolean successful;
	
	public ServerPrivateMessageContext(long uid, String fromUsername, int fromRights, String toUsername, String message, boolean successful) {
		this.uid = uid;
		this.fromUsername = fromUsername;
		this.fromRights = fromRights;
		this.toUsername = toUsername;
		this.message = message;
		this.successful = successful;
	}
}
