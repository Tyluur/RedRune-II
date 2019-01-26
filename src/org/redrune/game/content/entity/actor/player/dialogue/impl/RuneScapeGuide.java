package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.cache.loaders.NPCDefinitions;
import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;

public class RuneScapeGuide extends Dialogue {
	
	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "Greetings! I see you are a new arrival in this land. My", "job is welcome all new visitors. So Welcome!" }, IS_NPC, npcId, 9827);
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "You have already learned the first thing needed to", "succeed in this world talking to other people!" }, IS_NPC, npcId, 9827);
			stage = 0;
		} else if (stage == 0) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "You will find many inhabitants of this world have useful", "things to say to you. By clicking on them with your", "mouse you can talk to them." }, IS_NPC, npcId, 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "I would also suggest reading through some of the", "supporting information on the website. There you can", "find the Knowledge Base, which contains all the.", "additional information you're likely to need. It also" }, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "contains maps and helpfull tips to help you on your", "journey." }, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "Good luck, " + player.getDisplayName() + ". If you have any trouble,", "contact <img=1>Gircat, <img=1>Ferret or <img=1>Zephyr." }, IS_NPC, npcId, 9827);
			stage = 4;
		} else if (stage == 4) {
			end();
		}
	}
	
	@Override
	public void finish() {
		
	}
	
}
