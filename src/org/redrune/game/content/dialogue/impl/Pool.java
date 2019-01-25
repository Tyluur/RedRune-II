package org.redrune.game.content.dialogue.impl;

import org.redrune.game.content.dialogue.Dialogue;
import org.redrune.game.global.WorldTile;

public class Pool extends Dialogue {
	
	@Override
	public void start() {
		sendDialogue(SEND_2_OPTIONS, "Would you like to step inside the pool?", "Yes, please, I'd love to go.", "No thanks"); // Change options
		// maybe?
	}
	
	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_2_OPTIONS && componentId == 1) {
			player.setNextWorldTile(new WorldTile(2598, 4689, 0)); // Coordinates
			// you can
			// change
			sendDialogue(SEND_2_TEXT_CHAT, "", "*You Step into the pool...", // You
					// can
					// change
					// txt
					"You feel powerful, as you teleports you!");
		}
	}
	
	@Override
	public void finish() {
	
	}
	
}