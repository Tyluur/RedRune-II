package org.redrune.game.content.dialogue.impl;

import org.redrune.game.content.dialogue.Dialogue;

public class SimpleMessage extends Dialogue {
	
	@Override
	public void start() {
		if (parameters.length == 1) {
			sendDialogue(SEND_1_TEXT_INFO, (String) parameters[0]);
		} else if (parameters.length == 2) {
			sendDialogue(SEND_2_TEXT_INFO, (String) parameters[0], (String) parameters[1]);
		} else if (parameters.length == 3) {
			sendDialogue(SEND_3_TEXT_INFO, (String) parameters[0], (String) parameters[1], (String) parameters[2]);
		} else {
			sendDialogue(SEND_4_TEXT_INFO, (String) parameters[0], (String) parameters[1], (String) parameters[2], (String) parameters[3]);
		}
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}
	
	@Override
	public void finish() {
	
	}
	
}
