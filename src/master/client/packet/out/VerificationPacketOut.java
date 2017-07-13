package master.client.packet.out;

import master.client.MCFlags;
import master.network.packet.writeable.WriteablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class VerificationPacketOut extends WriteablePacket {
	
	public VerificationPacketOut() {
		super(VERIFICATION_ATTEMPT_PACKET_ID);
	}
	
	@Override
	public WriteablePacket create() {
		final byte worldId = MCFlags.worldId;
		writeByte(worldId);
		writeString(KEYS[worldId]);
		return this;
	}
}
