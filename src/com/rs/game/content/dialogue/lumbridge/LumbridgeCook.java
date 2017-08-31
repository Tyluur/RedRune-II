package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * The Feather Dialogue for the Lumbridge Cook.
 * 
 * @author Gircat <gircat101@gmail.com>
 * @author Feather RuneScape 2011
 */

public class LumbridgeCook extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendDialogue(SEND_5_OPTIONS, player.getDisplayName(), "Do you have any other quests for me? ", "I am getting strong and mighty. ", "I keep on dying. ", "Can I use your range? ", "Can you tell me anything about that chest in the basement?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == 1) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"Do you have any other quests for me?" },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 1;
			} else if (componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"I'm getting strong and mighty. Grr. " },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 7;
			} else if (componentId == 3) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"I keep on dying." },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 9;
			} else if (componentId == 4) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"Can I use your range? " },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 11;
			} else if (componentId == 5) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"Can you tell me anything about that chest in the basement?" },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 19;
			}
			
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"That last one of yours was fun! " }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Ooh dear, yes I do." }, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"It's the Duke of Lumbridge's birthday today, and",
							"I need to bake him a cake! " }, IS_NPC, npcId,
					9827);
			stage = 4;
		} else if (stage == 4) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"I need you to bring me some eggs, some flour, ",
							"some milk and a chocolate bar... " }, IS_NPC,
					npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Nah, not really, I'm just messing with you! ",
							"Thanks for all your help, I know I can count on you",
							"again in the future! " }, IS_NPC, npcId, 9827);
			stage = 6;
		} else if (stage == 6) {
			end();
		} else if (stage == 7) {
			sendEntityDialogue(
					SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Glad to hear it." }, IS_NPC, npcId, 9827);
			stage = 8;
		} else if (stage == 8) {
			end();
		} else if (stage == 9) {
			sendEntityDialogue(
					SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Ah, well, at least you keep coming back to life too! " }, IS_NPC, npcId, 9827);
			stage = 10;
		} else if (stage == 10) {
			end();
		} else if (stage ==11) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Go ahead! It's a very good range; it's better than ",
							"most other ranges. " }, IS_NPC, npcId, 9827);
			stage = 12;
		} else if (stage ==12) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"It's called the Cook-o-Matic 25 and it uses a ",
							"combination of state-of-the-art temperature ",
							"regulation and magic." }, IS_NPC, npcId, 9827);
			stage = 13;
		} else if (stage == 13) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Will it mean my food will burn less often? " }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 14;
		} else if (stage ==14) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"As long as the food is fairly easy to cook in ",
							"the first place! " }, IS_NPC, npcId, 9827);
			stage = 15;
		} else if (stage ==15) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Here, take this manual. It should tell you ",
							"everything you need to know about this range. " }, IS_NPC, npcId, 9827);
			stage = 16;
		} else if (stage ==16) {
			sendDialogue(
					SEND_1_TEXT_INFO, "�� The cook hands you a manual. ��");
			player.getInventory().addItem(15411, 1);
			stage = 17;
		} else if (stage == 17) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Thanks!" }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 18;
		} else if (stage == 18) {
			end();
		} else if (stage == 19) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							" So that chest in the basement that suddenly ",
							"appeared along with the Culinaromancer... " }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 20;
		} else if (stage == 20) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Can you tell me anything about it? " }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 21;
		} else if (stage ==21) {
			sendEntityDialogue(
					SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You mean you didn't check it out yet? " }, IS_NPC, npcId, 9827);
			stage = 22;
		} else if (stage ==22) {
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"I really think you should, it seems to be some kind of ",
							"magical cooking chest, I found a bunch of food in it ",
							"earlier, along with a bunch of weird looking kitchen ",
							"equipment and some snazzy gloves! " }, IS_NPC, npcId, 9827);
			stage = 23;
		} else if (stage ==23) {
			end();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
