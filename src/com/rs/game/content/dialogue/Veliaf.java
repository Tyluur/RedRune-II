package com.rs.game.content.dialogue;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.entity.WorldTile;
import com.rs.game.content.Magic;
import com.rs.utility.game.player.ShopsHandler;

public class Veliaf extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello devious one. What can I get for you?", },
				IS_NPC, npcId, 1569);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"I'd like to browse Evelestium's armours" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendDialogue(SEND_5_OPTIONS, "What shop would you like to visit?",
					"Pure Armours", "Zerk. Pure Armours", "Main Armours 1",
					"Main Armours 2", "More Options");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 1) {
				ShopsHandler.openShop(player, 32);
				end();
			} else if (componentId == 2) {
				ShopsHandler.openShop(player, 33);
				end();
			} else if (componentId == 3) {
				ShopsHandler.openShop(player, 35);
				end();
			} else if (componentId == 4) {
				ShopsHandler.openShop(player, 36);
				end();
			} else if (componentId == 5) {
				stage = 3;
				sendDialogue(SEND_5_OPTIONS,
						"What shop would you like to visit?", "Main Armours 3",
						"Skiller - 1", "Skiller - 2", "Skiller - 3",
						"More Options");
			}
		} else if (stage == 3) {
			if (componentId == 1) {
				end();
			} else if (componentId == 2) {
				ShopsHandler.openShop(player, 45);
				end();
			} else if (componentId == 3)
				end();
			else if (componentId == 4)
				end();
			else if (componentId == 5) {
				stage = 4;
				sendDialogue(SEND_5_OPTIONS,
						"What shop would you like to visit?",
						"Statius's Armours", "Vesta's Armours",
						"Zuriel's Armours", "Morrigan's Armours",
						"More Options");
			}
		} else if (stage == 4) {
			if (componentId == 1) {
				ShopsHandler.openShop(player, 37);
				end();
			} else if (componentId == 2) {
				ShopsHandler.openShop(player, 38);
				end();
			} else if (componentId == 3) {
				ShopsHandler.openShop(player, 39);
				end();
			} else if (componentId == 4) {
				ShopsHandler.openShop(player, 40);
				end();
			} else if (componentId == 5) {
				stage = 5;
				sendDialogue(SEND_5_OPTIONS,
						"What shop would you like to visit?", "Necklaces",
						"Rings", "Teleportation 1", "Teleportation 2",
						"More Options");
			}
		} else if (stage == 5) {
			if (componentId == 1) {
				ShopsHandler.openShop(player, 75);
				end();
			} else if (componentId == 2) {
				ShopsHandler.openShop(player, 76);
				end();
			} else if (componentId == 3) {
				ShopsHandler.openShop(player, 77);
				end();
			} else if (componentId == 4) {
				ShopsHandler.openShop(player, 78);
				end();
			} else if (componentId == 5) {
				stage = 6;
				sendDialogue(SEND_2_OPTIONS, "Now what would you like to do?",
						"I'm done viewing armours, where's the weapons?",
						"Nevermind");
			}
		} else if (stage == 6) {
			if (componentId == 1) {
				end();
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3162,
						3485, 0));
			} else if (componentId == 2) {
				end();
			}
		}
	}

	@Override
	public void finish() {

	}

	private void teleportPlayer(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
	}
}