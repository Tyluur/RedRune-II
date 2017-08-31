package com.rs.game.content.dialogue;

import com.rs.game.entity.actor.player.data.Skills;

/**
 * Setting a skill level.
 * 
 * @author Raghav
 * 
 */
public class SetSkills extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (!player.getEquipment().wearingArmour())
			sendDialogue(SEND_1_TEXT_INFO, "Please remove your armour first.");
		else
			sendDialogue(SEND_5_OPTIONS, "Choose a skill", "" + Skills.SKILL_NAME[0], "" + Skills.SKILL_NAME[1], "" + Skills.SKILL_NAME[2], "" + Skills.SKILL_NAME[3], "More options.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_5_OPTIONS) {
			if (stage == -1) {
				if (componentId == 1) {
					player.getTemporaryAttributtes().put("skillId",
							Skills.ATTACK);
					player.getPackets().sendRunScript(108, "Enter skill level:");
				} else if (componentId == 2) {
					player.getTemporaryAttributtes().put("skillId",
							Skills.DEFENCE);
					player.getPackets().sendRunScript(108, "Enter skill level:");
				} else if (componentId == 3) {
					player.getTemporaryAttributtes().put("skillId",
							Skills.STRENGTH);
					player.getPackets().sendRunScript(108, "Enter skill level:");
				} else if (componentId == 4) {
					player.getTemporaryAttributtes().put("skillId",
							Skills.HITPOINTS);
					player.getPackets().sendRunScript(108, "Enter skill level:");
				} else if (componentId == 5) {
					stage = 1;
					sendDialogue(SEND_5_OPTIONS, "Choose a skill", "" + Skills.SKILL_NAME[4], "" + Skills.SKILL_NAME[5], "" + Skills.SKILL_NAME[6], "" + Skills.SKILL_NAME[23], "Never mind.");
				}
			} else if (stage == 1) {
				if (componentId == 1) {
					player.getTemporaryAttributtes().put("skillId",
							Skills.RANGE);
					player.getPackets().sendRunScript(108, "Enter skill level:");
				} else if (componentId == 2) {
					player.getTemporaryAttributtes().put("skillId",
							Skills.PRAYER);
					player.getPackets().sendRunScript(108, "Enter skill level:");
				} else if (componentId == 3) {
					player.getTemporaryAttributtes().put("skillId",
							Skills.MAGIC);
					player.getPackets().sendRunScript(108, "Enter skill level:");
				} else if (componentId == 4) {
					player.getTemporaryAttributtes().put("skillId",
							Skills.SUMMONING);
					player.getPackets().sendRunScript(108, "Enter skill level:");
				} else if (componentId == 5)
					end();
			}
		} else
			end();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
