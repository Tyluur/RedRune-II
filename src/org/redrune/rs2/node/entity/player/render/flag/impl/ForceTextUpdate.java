package org.redrune.rs2.node.entity.player.render.flag.impl;

import org.redrune.network.stream.IoWriteEvent;
import org.redrune.rs2.node.entity.player.render.flag.UpdateFlag;

/**
 * Handles the chat update flag.
 *
 * @author Emperor
 */
public class ForceTextUpdate extends UpdateFlag {
	
	/**
	 * The message to send.
	 */
	private final String message;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code ChatUpdate} {@code Object}.
	 *
	 * @param message
	 * 		The message.
	 */
	public ForceTextUpdate(String message, boolean npc) {
		this.message = message;
		this.npc = npc;
	}
	
	@Override
	public void write(IoWriteEvent bldr) {
		bldr.writeRS2String(message);
	}
	
	@Override
	public int getMaskData() {
		return npc ? 0x80 : 0x4000;
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 18 : 7;
	}
	
}