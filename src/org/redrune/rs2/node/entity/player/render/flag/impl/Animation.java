package org.redrune.rs2.node.entity.player.render.flag.impl;

import org.redrune.anetworking.rs666.packet.PacketBuilder;
import org.redrune.network.stream.IoWriteEvent;
import org.redrune.rs2.node.entity.player.render.UpdateMasks;
import org.redrune.rs2.node.entity.player.render.flag.UpdateFlag;
import org.redrune.utility.backend.Priority;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an animation update flag.
 *
 * @author Emperor
 */
public class Animation extends UpdateFlag {
	
	/**
	 * The animation id.
	 */
	@Getter
	private final int id;
	
	/**
	 * The speed of the animation.
	 */
	@Getter
	private final int speed;
	
	/**
	 * If the entity is an NPC.
	 */
	@Getter
	@Setter
	private boolean npc;
	
	/**
	 * The priority.
	 */
	@Getter
	@Setter
	private Priority priority;
	
	/**
	 * Constructs a new {@code Animation} {@code Object}.
	 *
	 * @param id
	 * 		The animation id.
	 * @param speed
	 * 		The speed of the animation.
	 * @param npc
	 * 		If the entity is an NPC.
	 */
	public Animation(int id, int speed, boolean npc) {
		this(id, speed, npc, Priority.NORMAL);
	}
	
	/**
	 * Constructs a new {@code Animation} {@code Object}.
	 *
	 * @param id
	 * 		The animation id.
	 */
	
	public Animation(int id) {
		this(id, 0, false, Priority.NORMAL);
	}
	
	/**
	 * Constructs a new {@code Animation} {@code Object}.
	 *
	 * @param id
	 * 		The animation id.
	 * @param speed
	 * 		The speed of the animation.
	 * @param npc
	 * 		If the entity is an NPC.
	 * @param priority
	 * 		The animation priority.
	 */
	public Animation(int id, int speed, boolean npc, Priority priority) {
		this.id = id;
		this.speed = speed;
		this.npc = npc;
		this.priority = priority;
	}
	
	@Override
	public void write(IoWriteEvent bldr) {
		if (npc) {
			bldr.writeLEShortA(id);
			bldr.writeLEShortA(id);
			bldr.writeLEShortA(id);
			bldr.writeLEShortA(id);
			bldr.writeByteA(speed << 16);
		} else {
			for (int i = 0; i < 4; i++) {
				bldr.writeA(id);
			}
			bldr.write(speed << 16);
		}
	}
	
	@Override
	public boolean canRegister(UpdateMasks updateMasks) {
		if (priority == Priority.LOWEST && updateMasks.getAnimationPriority().ordinal() > priority.ordinal()) {
			return false;
		}
		if (updateMasks.get(getMaskData())) {
			if (updateMasks.getAnimationPriority().ordinal() > priority.ordinal()) {
				return false;
			}
			updateMasks.setAnimationPriority(priority);
		}
		return true;
	}
	
	@Override
	public int getMaskData() {
		return npc ? 0x1 : 0x10;
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 6 : 15;
	}
	
	public static Animation create(int animationId) {
		return new Animation(animationId);
	}
	
}
