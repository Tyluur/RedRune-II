package master.server.network.packet.in;

import master.network.packet.IncomingPacket;
import master.network.packet.readable.Readable;
import master.network.packet.readable.ReadablePacket;
import master.server.network.MSSession;
import master.server.network.packet.out.SuccessfulVerificationOut;
import master.server.world.MSRepository;
import master.server.world.MSWorld;

import static master.network.packet.PacketConstants.VERIFICATION_ATTEMPT_PACKET_ID;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Readable(packetIds = { VERIFICATION_ATTEMPT_PACKET_ID })
public class VerificationPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		if (session.isVerified()) {
			System.out.println("Session attempted duplicate verification: " + session);
			return;
		}
		
		byte worldId = (byte) packet.readByte();
		String key = packet.readString();
		
		// the id of the world connecting to us
		if (worldId < 0 || worldId >= KEYS.length) {
			return;
		}
		
		// the key we expected for this world
		String expectedKey = KEYS[worldId];
		
		// keys didn't match
		if (!expectedKey.equals(key)) {
			System.out.println("Keys were not equal!");
			return;
		}
		
		// the world created
		final MSWorld world = MSRepository.createNewWorld(worldId);
		// if the world was created successfully
		boolean created = world != null;
		
		// we let the session know they have been verified
		session.setVerified(true);
		session.sync(world);
		session.write(new SuccessfulVerificationOut(created));
	}
}
