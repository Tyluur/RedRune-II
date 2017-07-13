package master.network.packet.readable;

import master.network.MasterSession;
import master.network.packet.IncomingPacket;
import master.network.packet.PacketConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public interface ReadablePacket<T extends MasterSession> extends PacketConstants {
	
	/**
	 * Reads the packet
	 *
	 * @param session
	 * 		The session the packet is from
	 * @param packet
	 * 		The packet the data is from
	 */
	void read(T session, IncomingPacket packet);
}
