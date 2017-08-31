package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * 
 * @author Gircat <gircat101@gmail.com>
 * @author Feather RuneScape 2011
 */

public class RangedInstructor extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello, archer." }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Hey! ",
							"Can you help me out?" }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Sure I can! What are you seaking help in?",
							"There are various things I can help you out",
							"with, just ask and I'll answer for you ",
							"the best I can." }, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendDialogue(SEND_4_OPTIONS, player.getDisplayName(), "How do you train Range?", "What are the differences between bows?", "Who are you?", "Nevermind, thanks.");
			stage = 3;
		} else if (stage == 3) {
			if (componentId == 1) {
				sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Training Range is a simple skill. There are many",
						"ways you can train. For beginners I recommend using",
						"a regular Short bow and Bronze arrows. Walk South",
						"towards Al kharid you can kill ducks in the river." },
						IS_NPC, npcId, 9827);
				stage = 4;
			} else if (componentId == 2) {
				sendEntityDialogue(
						SEND_4_TEXT_CHAT,
						new String[] {
								NPCDefinitions.getNPCDefinitions(npcId).name,
								"Different bows have different bonuses. Some bows",
								"even have a special attack options, which makes you",
								"hit higher than bows without them. The higher the level",
								"you are the better bows and arrows you earn." },
						IS_NPC, npcId, 9827);
				stage = 5;
			} else if (componentId == 3) {
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"I am the instructor for the ranging skill, I don't",
						"do much besides stand here and help new comers." },
						IS_NPC, npcId, 9827);

				stage = 6;
			} else if (componentId == 4) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "Nevermind." },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 100;
			}
		} else if (stage == 100) {
			end();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
