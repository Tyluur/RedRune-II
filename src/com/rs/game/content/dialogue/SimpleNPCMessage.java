package com.rs.game.content.dialogue;

import com.rs.cache.loaders.NPCDefinitions;

public class SimpleNPCMessage extends Dialogue {

	private int npcId;
	private String message;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		message = (String) parameters[1];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						message }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}

	@Override
	public void finish() {

	}

}
