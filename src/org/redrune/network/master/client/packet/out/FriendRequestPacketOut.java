package org.redrune.network.master.client.packet.out;

import org.redrune.network.master.network.packet.writeable.WriteablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
public class FriendRequestPacketOut extends WriteablePacket {
	
	/**
	 * The name of the player requesting information
	 */
	private final String requester;
	
	/**
	 * The id of the world that the requester was on
	 */
	private final byte requesterWorldId;
	
	/**
	 * The name of the player we requested information about
	 */
	private final String requested;
	
	/**
	 * Constructs a new outgoing packet
	 */
	public FriendRequestPacketOut(String requester, byte requesterWorldId, String requested) {
		super(FRIEND_REQUEST_PACKET_ID);
		this.requester = requester;
		this.requesterWorldId = requesterWorldId;
		this.requested = requested;
	}
	
	@Override
	public WriteablePacket create() {
		writeString(requester);
		writeByte(requesterWorldId);
		writeString(requested);
		return this;
	}
}
