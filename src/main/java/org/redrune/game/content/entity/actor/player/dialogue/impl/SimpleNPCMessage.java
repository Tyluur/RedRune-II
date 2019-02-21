package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;

public class SimpleNPCMessage extends Dialogue {
	
	@Override
	public void start() {
		int npcId = getParam(0);
		String[] messages = new String[parameters.length - 1];
		for (int i = 0; i < messages.length; i++) {
			messages[i] = (String) parameters[i + 1];
		}
		npc(npcId, 9827, messages);
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}
	
	@Override
	public void finish() {
	
	}
	
}
