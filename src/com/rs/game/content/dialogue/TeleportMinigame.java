package com.rs.game.content.dialogue;

import com.rs.game.entity.WorldTile;
import com.rs.game.content.Magic;

/*
 *
 *@author Ashton
 *
 */
public class TeleportMinigame extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Minigame Teleports", "Duel Arena",
				"Castle Wars", "Soul Wars", "Dominion Tower", "Dungeoneering");
	}

	public void run(int interfaceId, int componentId) {
		if (componentId == 1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3366,
					3266, 0));
		} else if (componentId == 2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2443,
					3089, 0));
		} else if (componentId == 3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1886,
					3178, 0));
		} else if (componentId == 4) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3366,
					3083, 0));
		} else if (componentId == 5) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3447,
					3697, 0));
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}
}
