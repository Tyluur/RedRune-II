package org.redrune.network.rs666;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class NetworkTransmitter {
	
	/**
	 * The player
	 */
	private Player player;
	
	public NetworkTransmitter(Player player) {
		this.player = player;
	}
	
	/**
	 * Sends the outgoing packet
	 *
	 * @param packet
	 * 		The outgoing packet
	 */
	public void send(Packet packet) {
		player.getNetworkSession().write(packet);
	}
	
}
