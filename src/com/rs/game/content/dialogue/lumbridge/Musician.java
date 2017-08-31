package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * The Feather Dialogue for Musician's around the world.
 * 
 * @author Gircat <gircat101@gmail.com>
 * @author Feather RuneScape 2011
 */

public class Musician extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello, " +player.getDisplayName() + "." }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Would you like to hear some of the finest music",
							"around the land of Feather?" }, IS_NPC, npcId, 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Sure! That sounds relaxing." }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 2;
		} else if (stage == 2) {
			end();
			player.sendMessage("<col=ff0033>Sorry, that feature isn't added yet.");
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}
}