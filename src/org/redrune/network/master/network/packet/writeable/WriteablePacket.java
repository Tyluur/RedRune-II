package org.redrune.network.master.network.packet.writeable;

import org.redrune.network.master.network.packet.OutgoingPacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public abstract class WriteablePacket extends OutgoingPacket {
	
	/**
	 * Constructs a new outgoing packet
	 *
	 * @param id
	 * 		The id of the packet
	 */
	public WriteablePacket(int id) {
		super(id);
	}
	
	/**
	 * Creates a writeable packet and appends data to it
	 */
	public abstract WriteablePacket create();
}
