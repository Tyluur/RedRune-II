package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * Dialogue for DoomSayer found in Lumbridge.
 * 
 * @author Jordan / Apollo <citellumrsps@gmail.com>
 * @author Feather RuneScape 2011
 */
public class DoomSayer extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Dooooom!" }, IS_NPC, npcId, 9827);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Do you mean the Battle of Lumbridge?",
							"Are you telling me I should go and help out",
							"by going to join in?" }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"No, why should I be doing that? I'm talking about",
							"doooooom here, not some battlefield.", }, IS_NPC,
					npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Well, everyone else seems to be... um... anyway,",
							"you mentioned doom. Where is this doom?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"All around us! I can feel it in the air, hear it",
							"on the wind, smell it...also in the air!", },
					IS_NPC, npcId, 9827);
			stage = 4;
		} else if (stage == 4) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Is there anything we can do about this doom?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 5;
		} else if (stage == 5) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"There is nothing you need to do my friend! I am the",
							"Doomsayer, although my real title could be something",
							"like the Danger Tutor.", }, IS_NPC, npcId, 9827);
			stage = 6;
		} else if (stage == 6) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Danger Tutor?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 7;
		} else if (stage == 7) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Yes! I roam the world sensing danger.", }, IS_NPC,
					npcId, 9827);
			stage = 8;
		} else if (stage == 8) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"If I find a dangerous area, then I put up warning signs",
							"that will tell you what is so dangerous about that area.", },
					IS_NPC, npcId, 9827);
			stage = 9;
		} else if (stage == 9) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"If you see the signs often enough, then you can turn",
							"them of; by that time you likely known what the area",
							"has in store for you." }, IS_NPC, npcId, 9827);
			stage = 10;
		} else if (stage == 10) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"But what if I want to see the warnings again?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 11;
		} else if (stage == 11) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"That's why I'm waiting here!" }, IS_NPC, npcId,
					9827);
			stage = 12;
		} else if (stage == 12) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"If you want to see the warning messages again, I can",
							"turn them back on for you." }, IS_NPC, npcId, 9827);
			stage = 13;
		} else if (stage == 13) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Do you need to turn on any warnings right now?" },
					IS_NPC, npcId, 9827);
			stage = 14;
		} else if (stage == 14) {
			sendDialogue(SEND_2_OPTIONS, player.getDisplayName(), "Yes, I do.", "Not right now.");
			stage = 15;
		} else if (stage == 15) {
			if (componentId == 1) {
				// TODO Find the Warnings Interface and implement into Dialogue.
				// ::inter 583
				end();
				 player.getInterfaceManager().sendInterface(Integer.valueOf("583"));
			} else if (componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Ok, keep an eye out for the messages though!" },
						IS_NPC, npcId, 9827);
				stage = 16;
			}
		} else if (stage == 16) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "I will." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 17;
		} else if (stage == 17) {
			end();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
