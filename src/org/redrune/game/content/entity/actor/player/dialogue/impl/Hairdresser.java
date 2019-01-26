package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.cache.loaders.NPCDefinitions;
import org.redrune.game.content.PlayerLook;
import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;

public class Hairdresser extends Dialogue {
	
	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "Good afternoon, " + (player.getAppearance().isMale() ? "sir" : "lady") + ".", "In need of a haircut or shave, are we?" }, IS_NPC, npcId, 9827);
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
			case -1:
				stage = 0;
				sendDialogue(SEND_2_OPTIONS, "Select an Option", "Yes, please.", "No, thank you.");
				break;
			case 0:
				if (componentId == OPTION_2) {
					sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "No, thank you." }, IS_PLAYER, player.getIndex(), 9827);
					stage = 1;
				} else {
					stage = 2;
					sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Yes, please." }, IS_PLAYER, player.getIndex(), 9827);
				}
				break;
			case 1:
				stage = -2;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "Very well. Come back if you change your mind." }, IS_NPC, npcId, 9827);
				break;
			case 2:
				if (player.getEquipment().getHatId() != -1) {
					stage = -2;
					sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "Of course; but I can't see your head at the moment.", "Please remove your headgear first." }, IS_NPC, npcId, 9827);
				} else if (player.getEquipment().getWeaponId() != -1 || player.getEquipment().getShieldId() != -1) {
					stage = -2;
					sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "I don't feel comfortable cutting hair when you are", "wielding something. Please remove what you ", "are holding first." }, IS_NPC, npcId, 9827);
				} else {
					stage = 3;
					sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "Certainly, sir. We have a special offer at the moment:", "all shaves and haircuts are free!" }, IS_NPC, npcId, 9827);
				}
				break;
			case 3:
				stage = 4;
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "Please select the hairstyle, beard and colour", "you would like from this brochure." }, IS_NPC, npcId, 9827);
				break;
			case 4:
				PlayerLook.openHairdresserSalon(player);
				end();
				break;
			default:
				end();
				break;
		}
	}
	
	@Override
	public void finish() {
	
	}
	
}
