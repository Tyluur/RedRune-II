package com.rs.game.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

public class SimpleNPCMessage extends Dialogue {
	
	@Override
	public void start() {
		int npcId = (Integer) parameters[0];
		String message = (String) parameters[1];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), message }, IS_NPC, npcId, 9827);
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}
	
	@Override
	public void finish() {
	
	}
	
}
