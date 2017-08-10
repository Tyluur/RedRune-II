package org.redrune.network.master.server.network.packet.out;

import org.redrune.network.master.network.packet.writeable.WriteablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
public class StatusReceivePacketOut extends WriteablePacket {
	
	/**
	 * The username of the player who updated their status
	 */
	private final String username;
	
	/**
	 * If the player's status is now online
	 */
	private final boolean online;
	
	/**
	 * The player's private chat status
	 */
	private final byte privateStatus;
	
	/**
	 * The id the player was on
	 */
	private final byte worldId;
	
	public StatusReceivePacketOut(String username, boolean online, byte privateStatus, byte worldId) {
		super(STATUS_RECEIVE_PACKET_ID);
		this.username = username;
		this.online = online;
		this.privateStatus = privateStatus;
		this.worldId = worldId;
	}
	
	@Override
	public WriteablePacket create() {
		writeString(username);
		writeByte((byte) (online ? 1 : 0));
		writeByte(privateStatus);
		writeByte(worldId);
		return this;
	}
}
