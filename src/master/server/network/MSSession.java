package master.server.network;

import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.Setter;
import master.network.MasterSession;
import master.network.packet.IncomingPacket;
import master.network.packet.readable.ReadableRepository;
import master.server.network.packet.in.VerificationPacketIn;
import master.server.world.MSWorld;

/**
 * This class holds the details regarding a session in which a connection was registered to the master client.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class MSSession extends MasterSession {
	
	/**
	 * The instance of the readable repository
	 */
	private static final ReadableRepository READABLE_REPOSITORY = new ReadableRepository(VerificationPacketIn.class.getPackage().getName());
	
	/**
	 * The world this session is for
	 */
	@Getter
	@Setter
	private MSWorld world;
	
	public MSSession(SocketChannel channel) {
		super(channel);
	}
	
	@Override
	public void read(IncomingPacket packet) {
		READABLE_REPOSITORY.read(this, packet);
	}
	
	/**
	 * Syncs a world with us
	 *
	 * @param world
	 * 		The world
	 */
	public void sync(MSWorld world) {
		setWorld(world);
		world.setSession(this);
	}
	
}
