package com.rs.game.content.dialogue.varrock;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * Dialogue for the Grand Exchange Tutor.
 * 
 * @author Gircat <gircat101@gmail.com>
 * @author Feather RuneScape 2011
 */
public class GrandExchangeTutor extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"How can I help? " }, IS_NPC,
				npcId, 9827);
	}
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Can you teach me about the Grand Exchange again?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,new String[] {NPCDefinitions.getNPCDefinitions(npcId).name,
							"Of course.", }, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"The building you see here is the Grand Exchange.",
							" You can simply tell us what you want to buy or ",
							"sell and for how much, and we'll pair you up with",
							"another player and make the trade for you! "},
					IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Buying and selling is done in a very similar way. ",
							"Let me describe it in five steps.", },
					IS_NPC, npcId, 9827);
			stage = 4;
		} else if (stage == 4) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Step 1: You decide what to buy or sell and come here",
							"with the items to sell or the money to buy with. ", },
					IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Step 2: Speak with one of the clerks, behind the desk ",
							"in the middle of the building and they will guide you ",
							"through placing the bid and the finer details of what you ",
							"are looking for.", },
					IS_NPC, npcId, 9827);
			stage = 6;
		} else if (stage == 6) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Step 3: The clerks will take the items or money off you",
							"and look for someone to complete the trade. ", },
					IS_NPC, npcId, 9827);
			stage = 7;
		} else if (stage == 7) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Step 4: You then need to wait perhaps a matter of moments",
							"or maybe even days until someone is looking for what you ",
							"have offered. ", },
					IS_NPC, npcId, 9827);
			stage = 8;
		} else if (stage == 8) {
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Step 5: When the trade is complete, we will let you",
							"know with a message and you can pick up your winnings by",
							"talking to the clerks or by visiting any bank",
							" in RuneScape.",},
					IS_NPC, npcId, 9827);
			stage = 9;
		} else if (stage == 9) {
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"There's a lot more information about the Grand Exchange, ",
							"all of which you can find out from Brugsen Bursen, the guy ",
							"with the megaphone. I would suggest you speak to him to",
							"fully get to grips with the Grand Exchange. Good luck!", },
					IS_NPC, npcId, 9827);
			stage = 10;
		} else if (stage == 10) {
			end();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
	}
}