package game.content.entity.actor.player.dialogue.impl;

import game.content.entity.actor.player.dialogue.Dialogue;

public class SimpleMessage extends Dialogue {
	
	@Override
	public void start() {
		String[] messages = new String[parameters.length];
		for (int i = 0; i < messages.length; i++) {
			messages[i] = String.valueOf(parameters[i]);
		}
		sendDialogue(messages);
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}
	
	@Override
	public void finish() {
	
	}
	
}
