package com.rs.game.content.dialogue;

public class WorldMap extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_2_OPTIONS, "Open World Map?",
				"Yes.", "No.");
	}
	@Override
	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_2_OPTIONS && componentId == 1) {
			player.getPackets().sendWindowsPane(755, 0);
			int posHash = player.getX() << 14 | player.getY();
			player.getPackets().sendGlobalConfig(622, posHash);
			player.getPackets().sendGlobalConfig(674, posHash);
		} else
			end();
	}

	@Override
	public void finish() {

	}

}
