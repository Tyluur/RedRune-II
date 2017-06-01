package org.redrune.network.rs666.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketStructure;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class ContainerUpdateBuilder implements OutgoingPacketStructure {
	
	/**
	 * The key of the container.
	 */
	private final int key;
	
	/**
	 * The items of items to send.
	 */
	private final Item[] items;
	
	/**
	 * If split interfaces.
	 */
	private final boolean split;
	
	/**
	 * The slots to send
	 */
	private final int[] slots;
	
	/**
	 * Constructs a new instance
	 *
	 * @param key
	 * 		The key of the update
	 * @param items
	 * 		The items
	 * @param slots
	 * 		The slots
	 */
	// TODO: fix this
	public ContainerUpdateBuilder(int key, Item[] items, int... slots) {
		this(key, items, key < 0, slots);
	}
	
	/**
	 * Constructs a new instance
	 *
	 * @param key
	 * 		The key of the container
	 * @param items
	 * 		The items
	 * @param split
	 * 		Whether items should be seperated into two groups.
	 * @param slots
	 * 		The slots
	 */
	public ContainerUpdateBuilder(int key, Item[] items, boolean split, int... slots) {
		this.key = key;
		this.items = items;
		this.split = split;
		this.slots = slots;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(141, PacketType.VAR_SHORT);
		
		bldr.writeShort(key);
		bldr.writeByte(split ? 1 : 0);
		
		System.out.println(split + ", " + Arrays.toString(items) + ", " + Arrays.toString(slots));
		
		if (slots != null) {
			for (int i = 0; i < items.length; i++) {
				if (i >= items.length) {
					System.out.println("Skipped " + i);
					continue;
				}
				Item item = items[i];
				if (item != null) {
					bldr.writeSmart(i);
					bldr.writeShort(item.getId() + 1);
					bldr.writeByte(item.getAmount() > 254 ? 255 : item.getAmount());
					if (item.getAmount() > 254) {
						bldr.writeInt(item.getAmount());
					}
					System.out.println("wrote item:\t" + item);
				} else {
					bldr.writeSmart(i);
					bldr.writeShort(0); //id
					System.out.println("Wrote nothing on slot " + i);
				}
			}
		}
		return bldr.toPacket();
	}
}
