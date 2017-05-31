package org.redrune.network.rs666.packet.structure.out;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.structure.OutgoingPacketStructure;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class ContainerPacketBuilder implements OutgoingPacketStructure {
	
	/**
	 * The type.
	 */
	private final int type;
	
	/**
	 * The items of items to send.
	 */
	private final Item[] items;
	
	/**
	 * If split interfaces.
	 */
	private final boolean split;
	
	public ContainerPacketBuilder(int type, Item[] items, boolean split) {
		this.type = type;
		this.items = items;
		this.split = split;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(122, PacketType.VAR_SHORT);
		
		bldr.writeShort(type);
		bldr.writeByte(split ? 1 : 0);
		bldr.writeShort(items.length);
		for (Item item : items) {
			int id, amt;
			if (item == null) {
				id = -1;
				amt = 0;
			} else {
				id = item.getId();
				amt = item.getAmount();
			}
			bldr.writeByte(amt > 254 ? 255 : amt);
			if (amt > 254) {
				bldr.writeInt1(amt);
			}
			bldr.writeLEShortA(id + 1);
		}
		return bldr.toPacket();
	}
}
