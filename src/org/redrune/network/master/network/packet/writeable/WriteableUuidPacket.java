package org.redrune.network.master.network.packet.writeable;

/**
 * The packet that has a header of a uuid is wrapped by this class
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public abstract class WriteableUuidPacket extends WriteablePacket {
	
	/**
	 * The uid of the session this packet is delivered from
	 */
	private final String uid;
	
	/**
	 * Constructs a new outgoing packet
	 *
	 * @param id
	 * 		The id of the packet
	 */
	public WriteableUuidPacket(int id, String uid) {
		super(id);
		this.uid = uid;
		writeString(uid);
	}
}
