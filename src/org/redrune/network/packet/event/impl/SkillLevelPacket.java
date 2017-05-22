package org.redrune.network.packet.event.impl;

import org.redrune.network.packet.event.PacketEvent;

import lombok.Getter;

/**
 * SkillLevelPacket.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class SkillLevelPacket implements PacketEvent {
	
	@Getter
	private final int skill;
	
	@Getter
	private final int experience;
	
	@Getter
	private final int level;

	public SkillLevelPacket(int skill, int exp, int level) {
		this.skill = skill;
		this.experience = exp;
		this.level = level;
	}

}
