package master.server.network.packet.out;

import master.network.packet.writeable.WriteablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class PrivateMessageDeliveryPacketOut extends WriteablePacket {
	
	/**
	 * The name of the person who this message is coming from
	 */
	private final String fromName;
	
	/**
	 * The name of the person who this message is going to
	 */
	private final String toName;
	
	/**
	 * The text that is in the message
	 */
	private final String message;
	
	public PrivateMessageDeliveryPacketOut(String fromName, String toName, String message) {
		super(PRIVATE_MESSAGE_DELIVERY_PACKET_ID);
		this.fromName = fromName;
		this.toName = toName;
		this.message = message;
	}
	
	@Override
	public WriteablePacket create() {
		writeString(fromName);
		writeString(toName);
		writeString(message);
		return this;
	}
}
