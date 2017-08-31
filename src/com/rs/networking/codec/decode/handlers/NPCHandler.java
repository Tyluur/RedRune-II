package com.rs.networking.codec.decode.handlers;

import com.rs.game.GameFlags;
import com.rs.game.content.PlayerLook;
import com.rs.game.content.dialogue.FremennikShipmaster;
import com.rs.game.content.skills.fishing.Fishing;
import com.rs.game.content.skills.fishing.Fishing.FishingSpots;
import com.rs.game.content.skills.thieving.PickPocketAction;
import com.rs.game.content.skills.thieving.PickPocketableNPC;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.ForceTalk;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.game.entity.actor.npc.impl.slayer.Strykewyrm;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.RouteEvent;
import com.rs.game.world.World;
import com.rs.networking.io.InputStream;
import com.rs.utility.game.npc.NPCExamines;
import com.rs.utility.game.player.ShopsHandler;

public class NPCHandler {
	
	int npcId;
	
	public NPC npc;
	
	public static NPC findNPC(int id) {
		for (NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id) {
				continue;
			}
			return npc;
		}
		return null;
	}
	
	public static void handleExamine(final Player player, InputStream stream) {
		boolean running = stream.readByte128() == 1;
		// System.out.println(running);
		int npcIndex = stream.readUnsignedShort128();
		final NPC npc = World.getNPCs().get(npcIndex);
		player.getPackets().sendNPCMessage(0, npc, NPCExamines.getExamine(npc.getId()));
		if (player.getRights() == 2) {
			player.getPackets().sendGameMessage("<col=00a0ff>examined npc : " + npc.getId() + ", " + npc.getName() + ", at " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
		}
	}
	
	public static void handleOption1(final Player player, InputStream stream) {
		@SuppressWarnings("unused") boolean unknown = stream.readByte128() == 1;
		int npcIndex = stream.readUnsignedShort128();
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId())) {
			return;
		}
		player.stopAll();
		player.setRouteEvent(new RouteEvent(npc, () -> {
			npc.resetWalkSteps();
			player.faceEntity(npc);
			FishingSpots spot = FishingSpots.forId(npc.getId() | 1 << 24);
			if (spot != null) {
				player.getActionManager().setAction(new Fishing(spot, npc));
				return;
			}
			// its a spot, they wont face us
			npc.faceEntity(player);
			if (!player.getControlerManager().processNPCClick1(npc)) {
				return;
			}
			if (npc.getId() == 648) {
				player.getDialogueManager().startDialogue("KingRoald", npc.getId());
			} else if (npc.getId() == 1569) {
				player.getDialogueManager().startDialogue("Veliaf", npc.getId());
			} else if (npc.getId() == 3777) {
				player.getDialogueManager().startDialogue("DoomSayer", npc.getId());
			} else if (npc.getId() == 12379) {
				player.getDialogueManager().startDialogue("GrimReaper", npc.getId());
			} else if (npc.getId() == 13727) {
				player.getDialogueManager().startDialogue("Xuan", npc.getId());
			} else if (npc.getId() == 3709) {
				player.getDialogueManager().startDialogue("MrEx", npc.getId());
			} else if (npc.getId() == 8443) {
				player.getDialogueManager().startDialogue("Lucien", npc.getId());
			} else if (npc.getId() == 798) {
				player.getDialogueManager().startDialogue("XPRate", npc.getId());
			} else if (npc.getId() == 5913) {
				player.getDialogueManager().startDialogue("Aubury", npc.getId());
			} else if (npc.getId() == 945) {
				player.getDialogueManager().startDialogue("RuneScapeGuide", npc.getId());
			} else if (npc.getId() == 948) {
				player.getDialogueManager().startDialogue("MiningInstructor", npc.getId());
			} else if (npc.getDefinitions().name.contains("Bob")) {
				player.getDialogueManager().startDialogue("BobDialogue", npc.getId());
			} else if (npc.getDefinitions().name.contains("Lumbridge Sage")) {
				player.getDialogueManager().startDialogue("LumbridgeSage", npc.getId());
			} else if (npc.getDefinitions().name.contains("Musician")) {
				player.getDialogueManager().startDialogue("Musician", npc.getId());
			} else if (npc.getId() == 949) {
				player.getDialogueManager().startDialogue("QuestGuide", npc.getId());
			} else if (npc.getDefinitions().name.contains("Cook")) {
				player.getDialogueManager().startDialogue("LumbridgeCook", npc.getId());
			} else if (npc.getDefinitions().name.contains("Father Aereck")) {
				player.getDialogueManager().startDialogue("FatherAereck", npc.getId());
			} else if (npc.getDefinitions().name.contains("Doomsayer")) {
				player.getDialogueManager().startDialogue("DoomSayer", npc.getId());
			} else if (npc.getDefinitions().name.contains("Man")) {
				player.getDialogueManager().startDialogue("LumbridgeMan", npc.getId());
			} else if (npc.getDefinitions().name.contains("Grand Exchange Tutor")) {
				player.getDialogueManager().startDialogue("GrandExchangeTutor", npc.getId());
			} else if (npc.getDefinitions().name.contains("Frog")) {
				player.getDialogueManager().startDialogue("Frog", npc.getId());
			} else if (npc.getDefinitions().name.contains("Sir Vant")) {
				player.getDialogueManager().startDialogue("SirVant", npc.getId());
			} else if (npc.getDefinitions().name.contains("Lowe")) {
				player.getDialogueManager().startDialogue("Lowe", npc.getId());
			} else if (npc.getDefinitions().name.contains("Horvik")) {
				player.getDialogueManager().startDialogue("Horvik", npc.getId());
			} else if (npc.getDefinitions().name.contains("Border Guard")) {
				player.getDialogueManager().startDialogue("BorderGuard", npc.getId());
			} else if (npc.getDefinitions().name.contains("Rewards mystic")) {
				player.getDialogueManager().startDialogue("RewardsMysticDialogue", npc.getId());
			} else if (npc.getDefinitions().name.contains("Hans")) {
				player.getDialogueManager().startDialogue("Hans", npc.getId());
			} else if (npc.getDefinitions().name.contains("Wise Old Man")) {
				player.getDialogueManager().startDialogue("WiseOldMan", npc.getId());
			}  else if (npc.getId() == 13930) {
				player.getDialogueManager().startDialogue("Ariane", npc.getId());
			} else if (npc.getDefinitions().name.contains("Hairdresser")) {
				player.getDialogueManager().startDialogue("Hairdresser", npc.getId());
			} else if (npc.getId() == 1263) {
				player.getDialogueManager().startDialogue("Wizard", npc.getId());
			} else if (npc.getDefinitions().name.contains("Ranged instructor")) {
				player.getDialogueManager().startDialogue("RangedInstructor", npc.getId());
			} else if (npc.getId() == 3373) {
				player.getDialogueManager().startDialogue("Max", npc.getId());
			} else if (npc.getId() == 9462) {
				Strykewyrm.handleStomping(player, npc);
			} else if (npc.getId() == 9707) {
				player.getDialogueManager().startDialogue("FremennikShipmaster", npc.getId(), true);
			} else if (npc.getId() == 9708) {
				player.getDialogueManager().startDialogue("FremennikShipmaster", npc.getId(), false);
			} else if (npc.getId() == 15513) {
				player.getDialogueManager().startDialogue("RoyalGuard", npc.getId());
			} else if (npc.getId() == 8461) {
				player.getDialogueManager().startDialogue("Turael", npc.getId());
			} else if (npc.getId() == 650) {
				ShopsHandler.openShop(player, 24);
			} else if (npc.getId() == 6537) {
				ShopsHandler.openShop(player, 19);
			} else if (npc.getId() == 6537) {
				ShopsHandler.openShop(player, 22);
			} else if (npc.getId() == 564) {
				ShopsHandler.openShop(player, 30);
			} else if (npc.getId() == 2253) {
				ShopsHandler.openShop(player, 26);
			} else if (npc.getId() == 2830) {
				ShopsHandler.openShop(player, 29);
			} else if (npc.getId() == 576) {
				ShopsHandler.openShop(player, 22);
			} else if (npc.getId() == 948) {
				ShopsHandler.openShop(player, 23);
			} else if (npc.getId() == 445) {
				ShopsHandler.openShop(player, 23);
			} else if (npc.getId() == 637) {
				ShopsHandler.openShop(player, 25);
			} else if (npc.getId() == 2732) {
				ShopsHandler.openShop(player, 26);
			} else if (npc.getId() == 4906) {
				ShopsHandler.openShop(player, 27);
			} else if (npc.getId() == 548) {
				PlayerLook.openThessaliasMakeOver(player);
			} else if (npc.getId() == 2676) {
				player.getDialogueManager().startDialogue("MakeOverMage", npc.getId(), 0);
			} else if (npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")) {
				player.getBank().openBank();
			} else {
				player.sendMessage("Nothing interesting happens...");
				if (GameFlags.debugMode) {
					System.out.println("First clicked npc [" + npc + "]");
				}
			}
		}, true));
	}
	
	public static void handleOption2(final Player player, InputStream stream) {
		@SuppressWarnings("unused") boolean unknown = stream.readByte128() == 1;
		int npcIndex = stream.readUnsignedShort128();
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId())) {
			return;
		}
		player.stopAll();
		player.setRouteEvent(new RouteEvent(npc, () -> {
			npc.resetWalkSteps();
			player.faceEntity(npc);
			FishingSpots spot = FishingSpots.forId(npc.getId() | (2 << 24));
			if (spot != null) {
				player.getActionManager().setAction(new Fishing(spot, npc));
				return;
			}
			npc.faceEntity(player);
			PickPocketableNPC pocket = PickPocketableNPC.get(npc.getId());
			if (pocket != null) {
				player.getActionManager().setAction(new PickPocketAction(npc, pocket));
				return;
			}
			if (npc instanceof Familiar) {
				if (player.getFamiliar() != npc) {
					player.getPackets().sendGameMessage("That isn't your familiar.");
					return;
				}
				if (npc.getDefinitions().hasOption("store")) {
					player.getFamiliar().store();
				} else if (npc.getDefinitions().hasOption("cure")) {
					if (!player.getPoisonManager().isPoisoned()) {
						player.getPackets().sendGameMessage("Your aren't poisoned or diseased.");
						return;
					} else {
						player.getFamiliar().drainSpecial(2);
						player.addPoisonImmune(120);
					}
				}
				return;
			}
			if (!player.getControlerManager().processNPCClick2(npc)) {
				return;
			} else if (npc.getId() == 8461) {
				player.getInterfaceManager().sendInterface(Integer.valueOf("583"));
			} else if (npc.getId() == 8461) {
				player.getDialogueManager().startDialogue("Turael", npc.getId());
			} else if (npc.getId() == 9707) {
				FremennikShipmaster.sail(player, true);
			} else if (npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")) {
				player.getBank().openBank();
			} else if (npc.getId() == 550) {
				ShopsHandler.openShop(player, 3);
			} else if (npc.getId() == 549) {
				ShopsHandler.openShop(player, 4);
			} else if (npc.getId() == 13727) {
				ShopsHandler.openShop(player, 13727);
			} else if (npc.getId() == 8228) {
				player.getInterfaceManager().sendInterface(811);
			} else if (npc.getId() == 9708) {
				FremennikShipmaster.sail(player, false);
			} else if (npc.getId() == 13455) {
				player.getBank().openBank();
			} else if (npc.getId() == 5913) {
				ShopsHandler.openShop(player, 2);
			} else if (npc.getId() == 9711) {
				ShopsHandler.openShop(player, 89);
			} else if (npc.getId() == 519) {
				ShopsHandler.openShop(player, 1);
			} else if (npc.getId() == 3777) {
				player.getInterfaceManager().sendInterface(Integer.valueOf("583"));
			} else if (npc.getId() == 520) {
				ShopsHandler.openShop(player, 2);
			} else if (npc.getId() == 14057) {
				player.getDialogueManager().startDialogue("Velio", npc.getId());
				// player.sm("Starting Velio Dialogue");
			} else if (npc.getId() == 14078) {
				player.getDialogueManager().startDialogue("Varnis", npc.getId());
			} else if (npc.getId() == 2676) {
				PlayerLook.openMageMakeOver(player);
			} else if (npc.getId() == 598) {
				PlayerLook.openHairdresserSalon(player);
			} else {
				player.sendMessage("Nothing interesting happens...");
				if (GameFlags.debugMode) {
					System.out.println("Second clicked npc [" + npc + "]");
				}
			}
		}, true));
	}
	
	public static void handleOption3(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId())) {
			return;
		}
		player.stopAll();
		if (forceRun) {
			player.setRun(true);
		}
		player.setRouteEvent(new RouteEvent(npc, () -> {
			npc.resetWalkSteps();
			if (!player.getControlerManager().processNPCClick3(npc)) {
				return;
			}
			player.faceEntity(npc);
			npc.faceEntity(player);
			if ((npc.getId() == 8462 || npc.getId() == 8464 || npc.getId() == 1597 || npc.getId() == 1598 || npc.getId() == 7780 || npc.getId() == 8467 || npc.getId() == 9084)) {
				ShopsHandler.openShop(player, 29);
			} else if (npc.getId() == 548) {
				PlayerLook.openThessaliasMakeOver(player);
			} else if (npc.getId() == 5913) {
				npc.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
				npc.setNextAnimation(new Animation(1818));
				npc.faceEntity(player);
				World.sendProjectile(npc, player, 110, 1, 1, 1, 1, 1, 1);
				player.setNextGraphics(new Graphics(110));
				player.setNextWorldTile(new WorldTile(2910, 4832, 0));
			} else {
				player.sendMessage("Nothing interesting happens...");
				if (GameFlags.debugMode) {
					System.out.println("Third clicked npc [" + npc + "]");
				}
			}
			
		}, true));
		if (player.getRights() == 2) {
			player.getPackets().sendGameMessage("Third click - " + npc.getId() + ", " + npc.getName() + ", at " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
		}
	}
}
