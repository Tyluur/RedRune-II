package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * Dialogue for the Lumbridge Sage.
 * 
 * @author Gircat <gircat101@gmail.com>
 * @author Feather RuneScape 2011
 */
public class LumbridgeSage extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Greetings, adventurer. How may I help you?" }, IS_NPC,
				npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Who are you?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"I am Phileas, the Lumbridge Sage. In times past,",
							"people came from all around to ask me for advice.",
							"My renown seems to have diminished somewhat in",
							"recent years, though. Can I help you with anything?", },
					IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"The town is governed by Duke Horacio, who is a good",
							"friend of our monarch, King Roald of Misthalin.", },
					IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Recently, however, there have been great changes",
							"due to the Battle of Lumbridge.", }, IS_NPC,
					npcId, 9827);
			stage = 4;
		} else if (stage == 4) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"What about the battle?" }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 5;
		} else if (stage == 5) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Why, the battle rages even now, on the far side of the",
							"castle. Saradomin and Zamorak are locked in battle,",
							"neither able to gain the upper hand.", }, IS_NPC,
					npcId, 9827);
			stage = 6;
		} else if (stage == 6) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Where once was forest, there is a giant crater,",
							"in which soldiers and creatures fight to the death.", },
					IS_NPC, npcId, 9827);
			stage = 7;
		} else if (stage == 7) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"And the city of Lumbridge has seen the ill-effects already!",
							"The castle walls themselves have fallen! ", },
					IS_NPC, npcId, 9827);
			stage = 8;
		} else if (stage == 8) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Is there anything I can do?" }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 9;
		} else if (stage == 9) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You could join the battle if you wish. Both sides are seeking",
							"help from any individual who is willing, and not just for",
							"fighting - they are seeking something known as divine tears.", },
					IS_NPC, npcId, 9827);
			stage = 10;
		} else if (stage == 10) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You can join saradomin by going to the camp at the",
							"north of the battlefield, and Zamorak by going to ",
							"the south.", }, IS_NPC, npcId, 9827);
			stage = 11;
		} else if (stage == 11) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"I'm fine for now, thanks." }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 12;
		} else if (stage == 12) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Good adventuring, traveller.", }, IS_NPC, npcId,
					9827);
			stage = 13;
		} else if (stage == 13) {
			end();
		}
	}

	@Override
	public void finish() {

	}

}
