package org.redrune.rs2.node.entity.npc.render.flag.impl;

import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.rs2.node.entity.player.render.flag.UpdateFlag;

/**
 * Represents the rename NPC update mask.
 *
 * @author Emperor
 */
public class RenameNPCUpdate extends UpdateFlag {
	
	/**
	 * The name to set.
	 */
	private final String name;
	
	/**
	 * Constructs a new {@code RenameNPCUpdate} {@code Object}.
	 *
	 * @param name
	 * 		The name to set.
	 */
	public RenameNPCUpdate(String name) {
		this.name = name;
	}
	
	@Override
	public void write(PacketBuilder bldr) {
		bldr.writeRS2String(name);
	}
	
	@Override
	public int getMaskData() {
		return 0x100000;
	}
	
	@Override
	public int getOrdinal() {
		return 17;
	}
	
}
