package org.redrune.game.node.entity.player.render.flag.impl;

import org.redrune.game.node.entity.player.render.flag.UpdateFlag;
import org.redrune.network.rs666.packet.PacketBuilder;

/**
 * Represents the secondary animation mask?
 *
 * @author Emperor
 */
public class Graphic3 extends UpdateFlag {
	
	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * The height.
	 */
	private final int height;
	
	/**
	 * The rotation.
	 */
	private final int rotation;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code Graphic3} {@code Object}.
	 *
	 * @param id
	 * 		The graphic id.
	 * @param height
	 * 		The height.
	 * @param rotation
	 * 		The rotation.
	 * @param npc
	 * 		If the entity is an NPC.
	 */
	public Graphic3(int id, int height, int rotation, boolean npc) {
		this.id = id;
		this.height = height;
		this.rotation = rotation;
		this.npc = npc;
	}
	
	@Override
	public void write(PacketBuilder bldr) {
		if (npc) {
			bldr.writeLEShortA(id);
			bldr.writeLEInt(height << 16);
			bldr.writeByteA(rotation);
		} else {
			bldr.writeLEShort(id);
			bldr.writeInt(height << 16);
			bldr.writeByteC(rotation);
		}
	}
	
	@Override
	public int getMaskData() {
		return npc ? 0x10000 : 0x100000;
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 9 : 5;
	}
	
}