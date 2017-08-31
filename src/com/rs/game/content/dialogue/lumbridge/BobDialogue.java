package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;
import com.rs.utility.game.player.ShopsHandler;

/**
 * Dialogue for the shop Bob found in Lumbridge.
 * 
 * @author Jordan / Apollo
 * @author Feather RuneScape 2011
 */
public class BobDialogue extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello, what can I do for you?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(SEND_2_OPTIONS, player.getDisplayName(), "I'd like to trade.", "Can you repair my items for me?");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == 1) {
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Great! I buy and sell pickaxes and hatchets.",
						"There are plenty to choose from." }, IS_NPC, npcId,
						9827);
				stage = 2;
			} else if (componentId == 2) {
				sendEntityDialogue(
						SEND_2_TEXT_CHAT,
						new String[] {
								NPCDefinitions.getNPCDefinitions(npcId).name,
								"Of course I can, though materials may cost you. Just ",
								"hand me the item and I'll take a look." },
						IS_NPC, npcId, 9827);
				stage = 3;
			}
		} else if (stage == 2) {
			ShopsHandler.openShop(player, 1);
			end();
		} else if (stage == 3) {
			player.getPackets()
					.sendGameMessage("Item repair is not yet added.");
			end();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}