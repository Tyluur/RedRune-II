package org.redrune.network.master.server.network.packet.out;

import org.redrune.network.master.network.packet.writeable.WriteablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
public class FriendDetailsPacketOut extends WriteablePacket {
	
	/**
	 * The name of the player who requested the details
	 */
	private final String requester;
	
	/**
	 * The name of the player who was requested
	 */
	private final String requested;
	
	/**
	 * If the player requested was online
	 */
	private final boolean online;
	
	/**
	 * The id of the world the requested player was on
	 */
	private final byte requestedWorldId;
	
	/**
	 * Constructs a new outgoing packet
	 */
	public FriendDetailsPacketOut(String requester, String requested, boolean online, byte requestedWorldId) {
		super(FRIEND_DETAILS_PACKET_ID);
		this.requester = requester;
		this.requested = requested;
		this.online = online;
		this.requestedWorldId = requestedWorldId;
	}
	
	@Override
	public WriteablePacket create() {
		writeString(requester);
		writeString(requested);
		writeByte((byte) (online ? 1 : 0));
		writeByte(requestedWorldId);
		return this;
	}
}
