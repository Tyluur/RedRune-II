package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * Dialogue for Father Aereck
 * 
 * @author Gircat <gircat101@gmail.com>
 * @author Feather RuneScape 2011
 */

public class FatherAereck extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"What do you need, " + player.getDisplayName() + "?" },
				IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Can you tell me about the battlefield?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Lumbridge battlefield is where Saradomin himself is",
							"currently fighting Zamorak! ", }, IS_NPC, npcId,
					9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Thank goodness that he is here! If he'd not shown up",
							"when he did, we'd all be thralls of Zamorak now! ", },
					IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"But he came to us from far away in our time of need, ",
							"and even now he fights evil on Lumbridge Battlefield.", },
					IS_NPC, npcId, 9827);
			stage = 4;
		} else if (stage == 4) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Oh, you must be on your way to Saradomin's war camp",
							"in order to help out. It's to the northwest of here. ", },
					IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"We cannot let evil conquer! ",
							"Good luck! And may Saradomin be with you! ", },
					IS_NPC, npcId, 9827);
			stage = 6;
		} else if (stage == 6) {
			end();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}