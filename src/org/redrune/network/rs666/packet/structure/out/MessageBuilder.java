package org.redrune.network.rs666.packet.structure.out;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class MessageBuilder implements OutgoingPacketStructure {
	
	/**
	 * The id of this message.
	 */
	private final int id;
	
	/**
	 * The identifier.
	 */
	private final byte identifier;
	
	/**
	 * The actual message to send.
	 */
	private final String message;
	
	/**
	 * The username.
	 */
	private final String username;
	
	/**
	 * Constructs a new {@code MessagePacket} {@code Object} with {@code 0} as message id (normal game message).
	 *
	 * @param message
	 * 		The message.
	 */
	public MessageBuilder(String message) {
		this(0, message);
	}
	
	/**
	 * Constructs a new {@code MessagePacket} {@code Object}.
	 *
	 * @param id
	 * 		The message id.
	 * @param message
	 * 		The message.
	 */
	public MessageBuilder(int id, String message) {
		this.id = id;
		this.message = message;
		this.identifier = 0;
		this.username = null;
	}
	
	/**
	 * Constructs a new {@code MessagePacket} {@code Object} used for sending a duel or trade request.
	 *
	 * @param id
	 * 		The text id (100 = trade, 101 = duel request).
	 * @param message
	 * 		The message.
	 * @param username
	 * 		The username.
	 */
	public MessageBuilder(int id, String message, String username) {
		this.id = id;
		this.identifier = 0x1;
		this.message = message;
		this.username = username;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(98, PacketType.VAR_BYTE);
		bldr.writeSmart(id);
		bldr.writeInt(0);
		bldr.writeByte(identifier);
		if (username != null) {
			bldr.writeRS2String(username);
		}
		bldr.writeRS2String(message);
		return bldr.toPacket();
	}
}
