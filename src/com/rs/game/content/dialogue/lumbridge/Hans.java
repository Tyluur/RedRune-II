package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * Dialogue for Hans.
 * 
 * @author Gircat <gircat101@gmail.com>
 * @author Feather RuneScape 2011
 */
public class Hans extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { player.getDisplayName(),
						"I wondered what happened to the castle?" }, IS_PLAYER,
				player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"It did take a battering recently. If you've not ",
							"noticed already, there is a battle going on just ",
							"outside between Saradomin and Zamorak! ", },
					IS_NPC, npcId, 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Never in all my years did I expect to see this ",
							"happen to the castle! ", }, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			end();
		}
	}

	@Override
	public void finish() {
	}
}
