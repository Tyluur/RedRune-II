package com.rs.game.content.dialogue;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.utility.game.player.ShopsHandler;

public class Varnis extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"How may i serv thy kind sir.", }, IS_NPC, npcId, 1569);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(
					SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"I would like to see thine wide variety of things." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendDialogue(SEND_2_OPTIONS,
					"What direction would thee like to visit?", "Armours",
					"Weapons");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 1) {
				sendDialogue(SEND_4_OPTIONS, "Armour Galore",
						"Melee Armour Shop", "Mage Armour Shop",
						"Range Armour Shop", "");
				stage = 3;
			} else if (componentId == 2) {
				sendDialogue(SEND_4_OPTIONS, "Weapons Galore",
						"Melee Weapon Shop", "Mage Weapon Shop",
						"Range Weapon Shop", "");
				stage = 4;
			}
		} else if (stage == 3) {
			if (componentId == 1) {
				ShopsHandler.openShop(player, 12);
				end();
			} else if (componentId == 2) {
				ShopsHandler.openShop(player, 13);
			} else if (componentId == 3) {
				ShopsHandler.openShop(player, 14);
				end();
			}
		} else if (stage == 4) {
			if (componentId == 1) {
				ShopsHandler.openShop(player, 15);
				end();
			} else if (componentId == 2) {
				ShopsHandler.openShop(player, 16);
				end();
			} else if (componentId == 3) {
				ShopsHandler.openShop(player, 17);
				end();
			}
		}
	}

	@Override
	public void finish() {

	}
}