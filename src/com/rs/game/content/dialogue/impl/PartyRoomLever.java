package com.rs.game.content.dialogue.impl;

import com.rs.game.content.PartyRoom;
import com.rs.game.content.dialogue.Dialogue;

public class PartyRoomLever extends Dialogue {
	
	@Override
	public void start() {
		sendDialogue(SEND_3_OPTIONS, SEND_DEFAULT_OPTIONS_TITLE, "Balloon Bonanza (1000 coins).", "Nightly Dance (500 coins).", "No action.");
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == 2) {
			PartyRoom.purchase(player, true);
		} else if (componentId == 3) {
			PartyRoom.purchase(player, false);
		}
		end();
	}
	
	@Override
	public void finish() {
		
	}
}
