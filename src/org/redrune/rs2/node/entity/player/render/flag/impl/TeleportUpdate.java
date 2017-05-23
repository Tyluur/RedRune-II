package org.redrune.rs2.node.entity.player.render.flag.impl;

import org.redrune.network.stream.IoWriteEvent;
import org.redrune.rs2.node.entity.player.render.flag.UpdateFlag;

/**
 * Represents an animation update flag.
 *
 * @author Emperor
 */
public class TeleportUpdate extends UpdateFlag {
	
	@Override
	public void write(IoWriteEvent outgoing) {
		outgoing.writeByteC(127);
	}
	
	@Override
	public int getMaskData() {
		return 0x400;
	}
	
	@Override
	public int getOrdinal() {
		return 11;
	}
	
}
