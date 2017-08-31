package com.rs.game.content.dialogue;

import com.rs.game.entity.WorldTile;
import com.rs.game.content.Magic;

/*
 *
 *@author Ashton
 *
 */
public class TeleportBosses extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Boss Teleports", "Corporeal Beast",
				"Tormented Demons", "God Wars Dungeon", "Barrows",
				"Chaos Elemental");
	}

	public void run(int interfaceId, int componentId) {
		if (componentId == 1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2966,
					4383, 0));
		} else if (componentId == 2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2562,
					5739, 0));
		} else if (componentId == 3) {
			/*
			 * Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(x, y,
			 * 0));
			 */
			player.getPackets().sendGameMessage(
					"God Wars Dungeon is coming soon.");
		} else if (componentId == 4) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3565,
					3289, 0));
		} else if (componentId == 5) {
			/*
			 * Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(x, y,
			 * 0));
			 */
			player.getPackets().sendGameMessage(
					"Chaos Elemental is coming soon.");
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}
}
