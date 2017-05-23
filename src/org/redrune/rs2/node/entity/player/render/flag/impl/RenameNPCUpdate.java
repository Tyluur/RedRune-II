package org.redrune.rs2.node.entity.player.render.flag.impl;

import org.redrune.network.stream.IoWriteEvent;
import org.redrune.rs2.node.entity.player.render.flag.UpdateFlag;
import org.redrune.utility.Misc;

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
	public void write(IoWriteEvent bldr) {
		bldr.writeRS2String(Misc.formatPlayerNameForDisplay(name));
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
