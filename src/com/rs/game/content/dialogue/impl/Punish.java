package com.rs.game.content.dialogue.impl;

import com.rs.game.content.dialogue.Dialogue;

public class Punish extends Dialogue {
	
	@Override
	public void start() {
		if (player.getRights() == 1) {
			sendDialogue(SEND_4_OPTIONS, "Punish", "Mute", "Kick", "Jail", "Nevermind");
		} else if (player.getRights() == 2) {
			sendDialogue(SEND_5_OPTIONS, "Punish", "Mute", "Kick", "Jail", "Ban", "Nevermind");
		}
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_2_OPTIONS && componentId == 1) {
			player.getPackets().sendWindowsPane(755, 0);
			int posHash = player.getX() << 14 | player.getY();
			player.getPackets().sendGlobalConfig(622, posHash);
			player.getPackets().sendGlobalConfig(674, posHash);
		} else {
			end();
		}
	}
	
	@Override
	public void finish() {
	
	}
	
}
