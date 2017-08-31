package com.rs.game.content.dialogue;

import com.rs.game.entity.WorldTile;

public class Crate extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_2_OPTIONS, "Oh, Would you like to go to Glacors?",
				"Yes, please, I'd love to go.", "No thanks"); // Change options
																// maybe?
	}

	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_2_OPTIONS && componentId == 1) {
			player.setNextWorldTile(new WorldTile(4188, 5720, 0)); // Coordinates
																	// you can
																	// change
			sendDialogue(SEND_2_TEXT_CHAT, "",
					"*The Magic crate teleport's you.", // You can change txt
					"You feel powerful, as you teleport!");
		}
	}

	@Override
	public void finish() {

	}

}