package org.redrune.rs2.node.entity.player.render.flag.impl;

import org.redrune.anetworking.rs666.packet.PacketBuilder;
import org.redrune.rs2.node.entity.player.render.flag.UpdateFlag;

/**
 * Represents the graphic 1 update flag.
 *
 * @author Emperor
 */
public class Graphic extends UpdateFlag {
	
	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * The graphic height.
	 */
	private final int height;
	
	/**
	 * The speed.
	 */
	private final int speed;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code Graphic} {@code Object}.
	 *
	 * @param id
	 * 		The graphic id.
	 * @param height
	 * 		The graphic height.
	 * @param speed
	 * 		The speed.
	 * @param npc
	 * 		If the entity is an NPC.
	 */
	public Graphic(int id, int height, int speed, boolean npc) {
		this.id = id;
		this.height = height;
		this.speed = speed;
		this.npc = npc;
	}
	
	/**
	 * Constructs a new graphic
	 *
	 * @param id
	 * 		The id of the graphic
	 */
	public Graphic(int id) {
		this.id = id;
		this.height = 0;
		this.speed = 0;
		this.npc = false;
	}
	
	@Override
	public void write(PacketBuilder bldr) {
		if (npc) {
			bldr.writeLEShortA(id);
			bldr.writeLEInt(height << 16);
			bldr.writeByteA(speed);
		} else {
			bldr.writeLEShort(id);
			bldr.writeInt(height << 16);
			bldr.writeByteS(speed << 7);
		}
	}
	
	@Override
	public int getMaskData() {
		return npc ? 0x1000 : 0x10000;
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 2 : 13;
	}
	
	public static Graphic create(int i) {
		return new Graphic(i);
	}
	
}
