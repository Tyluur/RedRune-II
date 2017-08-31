package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * Dialogue for Frogs.
 * @author Gircat <gircat101@gmail.com>
 * @author Feather RuneScape 2011
 */
public class Frog extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_NO_CONTINUE_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Ribbet. Ribbet. Ribbet." }, IS_NPC,
				npcId, 9827);
		end();
	}

	@Override
	public void run(int interfaceId, int componentId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
}
