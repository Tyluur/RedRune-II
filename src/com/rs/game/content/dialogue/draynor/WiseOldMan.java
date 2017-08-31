package com.rs.game.content.dialogue.draynor;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * Dialogue for Wise Old Man found in Draynor.
 * 
 * @author Jordan / Apollo <citellumrsps@gmail.com>
 * @author Feather RuneScape 2011
 */
public class WiseOldMan extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Greetings, " + player.getDisplayName() + "." },
				IS_NPC, npcId, 9827);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"I'd like to buy a Quest Point Cape." }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			// Zenevivia has dialogue regarding the Quest Cape.
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(11571).name,
							"Still doing quests, eh? Bah, you adventurers!" },
					IS_NPC, 11571, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Zinny still hasn't given up on that topic.",
							"Now, Quest Capes cost 99,000 coins." }, IS_NPC,
					npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendDialogue(SEND_2_OPTIONS, player.getDisplayName(), "Yes.", "Actually, forget it.");
			stage = 4;
		} else if (stage == 4) {
			if (componentId == 1) {
				// TODO add a check if player has done the quests. Then give
				// player the cape.
				end();
				player.getPackets().sendGameMessage(
						"You can't get this cape yet.");
			} else if (componentId == 2) {
				end();
			}
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
