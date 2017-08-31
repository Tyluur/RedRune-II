package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * The Feather Dialogue for Sir Vant.
 * 
 * @author Gircat <gircat101@gmail.com>
 * @author Feather RuneScape 2011
 */
public class SirVant extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { player.getDisplayName(),
				"Hello. My name's " + player.getDisplayName() + "."  }, IS_PLAYER,
				player.getIndex(), 9827);
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Wait a moment - there's a dragon coming!" }, IS_NPC, npcId, 9827);
			stage = 1;
		} 
	}

	@Override
	public void finish() {
		
	}

}
