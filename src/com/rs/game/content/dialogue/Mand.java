package com.rs.game.content.dialogue;

import com.rs.cache.loaders.NPCDefinitions;

public class Mand extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_3_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello and...", "Welcome to RsCalifornia!",
						"Is there anything you would like you know?" }, IS_NPC,
				npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Who are you!?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"My name is Mandrith.",
							"I am RsCalifornia's PK Guru.",
							"You can collect pk tokens by killing players in the wilderness,",
							"then come back to me to exchange to rewards!" },
					IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"My shop is coming soon!",
							"So please keep upto date with updates by checking forums.",
							"and by clicking 'Updates' in the quest tab!", "" },
					IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Hope you enjoy your stay on,", "RsCalifornia!",
							"", "" }, IS_NPC, npcId, 9827);
			stage = 4;
		} else if (stage == 4) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Please View the rest of the rules,",
							"and other infomation indebth on our forums.", "",
							"" }, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			// controler.updateProgress();
			end();
		} else
			end();
	}

	@Override
	public void finish() {

	}
}