package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;
import org.redrune.rs2.node.entity.player.components.PlayerSkills;

/**
 * SkillLevelPacket.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class SkillLevelPacket implements PacketEvent {

	private final int skill;

	public SkillLevelPacket(int skills) {
		this.skill = skills;
	}

	public int getSkill() {
		return skill;
	}

}
