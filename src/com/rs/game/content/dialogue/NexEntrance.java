package com.rs.game.content.dialogue;

import com.rs.game.entity.WorldTile;
import com.rs.game.content.minigame.ZarosGodwars;

public final class NexEntrance extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_3_TEXT_INFO,
				"The room beyond this point is a prison!",
				"There is no way out other than death or teleport.",
				"Only those who endure dangerous encounters should proceed.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue(SEND_2_OPTIONS,
					"There are currently " + ZarosGodwars.getPlayersCount()
							+ " people fighting.<br>Do you wish to join them?",
					"Climb down.", "Stay here.");
		} else if (stage == 0) {
			if (componentId == 1) {
				player.setNextWorldTile(new WorldTile(2911, 5204, 0));
				player.getControlerManager().startControler("ZGDControler");
			}
			end();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
