package com.rs.game.content.dialogue.falador;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.entity.WorldTile;
import com.rs.game.content.minigame.CastleWars;
import com.rs.game.content.Magic;
import com.rs.game.content.dialogue.Dialogue;

public class Wizard extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Howdy " + player.getDisplayName()
								+ ", is there anywhere",
						"you would like to go?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Yes there is, thanks." }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendDialogue(SEND_5_OPTIONS, "Where would you like to go?", "Nex",
					"Bandos", "Saradomin", "Tormented Demons", "More Options");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 1)
				teleportPlayer(2905, 5203, 0);
			else if (componentId == 2)
				teleportPlayer(2870, 5363, 2);
			else if (componentId == 3)
				teleportPlayer(2901, 5264, 0);
			else if (componentId == 4)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2562,
						5739, 0));
			else if (componentId == 5) {
				stage = 3;
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Duel Arena", "Gnome Agility", "Dominion Tower",
						"Barrows", "More Options");
			}
		} else if (stage == 3) {
			if (componentId == 1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3365,
						3275, 0));
				player.getControlerManager().startControler("DuelControler");
			} else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2470,
						3436, 0));
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3366,
						3083, 0));
			else if (componentId == 4)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3565,
						3289, 0));
			else if (componentId == 5) {
				stage = 4;
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Magic Bank", "Multi Area (PvP)", "Fight Pits",
						"Wests(PvP)", "More Options");
			}
		} else if (stage == 4) {
			if (componentId == 1)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2539,
						4712, 0));
			else if (componentId == 2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3240,
						3611, 0));
				player.getControlerManager().startControler("Wilderness");
			} else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2399,
						5177, 0));
			else if (componentId == 4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2984,
						3596, 0));
				player.getControlerManager().startControler("Wilderness");
			} else if (componentId == 5) {
				stage = 5;
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Easts (PvP)", "Brimhaven", "Corporeal Beast",
						"Feldip hills", "More Options");
			}
		} else if (stage == 5) {
			if (componentId == 1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3360,
						3658, 0));
				player.getControlerManager().startControler("Wilderness");
			} else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2709,
						9464, 0));
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2966,
						4383, 0));
			else if (componentId == 4) {
				player.getPackets().sendGameMessage("Disabled.");
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2570,
						2916, 0));
			} else if (componentId == 5) {
				stage = 6;
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Rock Crabs", "King Black Dragon", "Dragons", "Abyss",
						"More Options");
			}
		} else if (stage == 6) {
			if (componentId == 1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2708,
						3709, 0));
			} else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2285,
						4694, 0));
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2892,
						9784, 0));
			else if (componentId == 4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3040,
						4834, 0));
			} else if (componentId == 5) {
				stage = 7;
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Mine Essence", "Pure Training", "Dominion Tower",
						"Barrows", "More Options");
			}
		} else if (stage == 7) {
			if (componentId == 1)
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(
						2911, 4832, 0));
			else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(
						3561, 9948, 0));
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(
						3366, 3083, 0));
			else if (componentId == 4)
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(
						3565, 3289, 0));
			else if (componentId == 5) {
				stage = 8;
				sendDialogue((short) 238, "Where would you like to go?", "Zamorak", "Armadyl", "Castle Wars", "Trivia", "More Options");
			}
		} else if (stage == 8) {
			if (componentId == 1)
				teleportPlayer(2925, 5330, 2);
			else if (componentId == 2)
				teleportPlayer(2838, 5297, 2);
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, CastleWars.LOBBY);
			else if (componentId == 4)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2647, 9378, 0));
			else if (componentId == 5) {
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Nex", "Bandos", "Saradomin", "Tormented Demons",
						"Beginning Options");
				stage = 2;
			}
		}
	}

	private void teleportPlayer(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("GodWars");
	}

	@Override
	public void finish() {

	}
}
