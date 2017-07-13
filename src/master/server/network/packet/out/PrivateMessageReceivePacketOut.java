package master.server.network.packet.out;

import master.network.packet.writeable.WriteablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class PrivateMessageReceivePacketOut extends WriteablePacket {
	
	/**
	 * The name of the person who this message is coming from
	 */
	private final String fromName;
	
	/**
	 * The rights of the person who this message is coming from
	 */
	private final byte fromRights;
	
	/**
	 * The name of the person who this message is going to
	 */
	private final String toName;
	
	/**
	 * The text that is in the message
	 */
	private final String message;
	
	public PrivateMessageReceivePacketOut(String fromName, byte fromRights, String toName, String message) {
		super(PRIVATE_MESSAGE_RECEIVE_PACKET_ID);
		this.fromName = fromName;
		this.fromRights = fromRights;
		this.toName = toName;
		this.message = message;
	}
	
	@Override
	public WriteablePacket create() {
		writeString(fromName);
		writeByte(fromRights);
		writeString(toName);
		writeString(message);
		return this;
	}
}
