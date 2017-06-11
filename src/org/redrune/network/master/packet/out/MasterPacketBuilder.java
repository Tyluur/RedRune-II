package org.redrune.network.master.packet.out;

import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.MasterPacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public abstract class MasterPacketBuilder<T extends MasterPacketContext> implements MasterConstants {
	
	/**
	 * The context of the packet
	 */
	protected final T context;
	
	/**
	 * Constructs a new master packet
	 *
	 * @param context
	 * 		The context of the packet
	 */
	public MasterPacketBuilder(T context) {
		this.context = context;
	}
	
	/**
	 * Gets the master packet to build
	 */
	public abstract MasterPacket build();
	
}
