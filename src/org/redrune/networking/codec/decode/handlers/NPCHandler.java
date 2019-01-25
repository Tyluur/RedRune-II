package org.redrune.networking.codec.decode.handlers;

import org.redrune.game.GameFlags;
import org.redrune.game.content.skills.fishing.Fishing;
import org.redrune.game.content.skills.fishing.Fishing.FishingSpots;
import org.redrune.game.content.skills.thieving.PickPocketAction;
import org.redrune.game.content.skills.thieving.PickPocketableNPC;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.RouteEvent;
import org.redrune.game.plugin.PluginRepository;
import org.redrune.game.global.World;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.game.ClickOption;
import org.redrune.utility.game.repository.npc.characteristic.NPCCharacteristicRepository;
import org.redrune.utility.game.repository.npc.spawn.NPCSpawnRepository;

import static org.redrune.utility.game.ClickOption.*;

public class NPCHandler {
	
	/**
	 * Decodes the npc stream and passes the npc to the correct handler
	 *
	 * @param player
	 * 		The player
	 * @param stream
	 * 		The stream
	 * @param option
	 * 		The option clicked, used for handler identification
	 */
	public static void decodeNPCStream(final Player player, InputStream stream, ClickOption option) {
		boolean running = stream.readByte128() == 1;
		int npcIndex = stream.readUnsignedShort128();
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || player.getLocks().isInteractionLocked()) {
			return;
		}
		if (running) {
			player.setRun(true);
		}
		switch (option) {
			case FIRST:
				handleOption1(player, npc);
				break;
			case SECOND:
				handleOption2(player, npc);
				break;
			case THIRD:
				handleOption3(player, npc);
				break;
			case FOURTH:
				handleOption4(player, npc);
				break;
			case EXAMINE:
				if (player.getAttribute("removing_npcs", false)) {
					NPCSpawnRepository.removeSpawn(npc);
					npc.finish();
					return;
				}
				player.getPackets().sendNPCMessage(0, npc, NPCCharacteristicRepository.getExamine(npc.getId()));
				if (GameFlags.debugMode) {
					System.out.println("Examined npc [" + npc + "]");
				}
				break;
		}
		
	}
	
	/**
	 * Handles the interaction with an npc using the {@link ClickOption#FIRST} option
	 *
	 * @param player
	 * 		The player
	 * @param npc
	 * 		The npc we wish to interact with
	 */
	private static void handleOption1(final Player player, final NPC npc) {
		final String option = npc.getDefinitions().getOption(1);
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
			if (!player.getControllerManager().canEntityClick(npc, FIRST)) {
				return;
			}
			if (PluginRepository.handleNPC(player, npc, option)) {
				return;
			}
			if (npc.getDefinitions().getName().contains("Banker") || npc.getDefinitions().getName().contains("banker")) {
				player.getBank().openBank();
			} else {
				player.getPackets().sendGameMessage("Nothing interesting happens...");
				if (GameFlags.debugMode) {
					System.out.println("No plugin registered for option " + option + " on npc " + npc);
				}
			}
		}, true));
	}
	
	/**
	 * Handles the interaction with an npc using the {@link ClickOption#SECOND} option
	 *
	 * @param player
	 * 		The player
	 * @param npc
	 * 		The npc we wish to interact with
	 */
	private static void handleOption2(final Player player, final NPC npc) {
		final String option = npc.getDefinitions().getOption(2);
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
			if (!player.getControllerManager().canEntityClick(npc, SECOND)) {
				return;
			}
			if (PluginRepository.handleNPC(player, npc, option)) {
				return;
			}
			if (npc.getDefinitions().getName().contains("Banker") || npc.getDefinitions().getName().contains("banker") || npc.getId() == 13455) {
				player.getBank().openBank();
			} else {
				player.getPackets().sendGameMessage("Nothing interesting happens...");
				if (GameFlags.debugMode) {
					System.out.println("No plugin registered for option " + option + " on npc " + npc);
				}
			}
		}, true));
	}
	
	/**
	 * Handles the interaction with an npc using the {@link ClickOption#THIRD} option
	 *
	 * @param player
	 * 		The player
	 * @param npc
	 * 		The npc we wish to interact with
	 */
	private static void handleOption3(final Player player, final NPC npc) {
		final String option = npc.getDefinitions().getOption(3);
		player.stopAll();
		player.setRouteEvent(new RouteEvent(npc, () -> {
			npc.resetWalkSteps();
			player.faceEntity(npc);
			npc.faceEntity(player);
			if (!player.getControllerManager().canEntityClick(npc, THIRD)) {
				return;
			}
			if (PluginRepository.handleNPC(player, npc, option)) {
				return;
			}
			player.getPackets().sendGameMessage("Nothing interesting happens...");
			if (GameFlags.debugMode) {
				System.out.println("No plugin registered for option " + option + " on npc " + npc);
			}
		}, true));
	}
	
	/**
	 * Handles the interaction with an npc using the {@link ClickOption#FOURTH} option
	 *
	 * @param player
	 * 		The player
	 * @param npc
	 * 		The npc we wish to interact with
	 */
	private static void handleOption4(final Player player, final NPC npc) {
		final String option = npc.getDefinitions().getOption(4);
		player.stopAll();
		player.setRouteEvent(new RouteEvent(npc, () -> {
			npc.resetWalkSteps();
			player.faceEntity(npc);
			npc.faceEntity(player);
			if (!player.getControllerManager().canEntityClick(npc, FOURTH)) {
				return;
			}
			if (PluginRepository.handleNPC(player, npc, option)) {
				return;
			}
			player.getPackets().sendGameMessage("Nothing interesting happens...");
			if (GameFlags.debugMode) {
				System.out.println("No plugin registered for option " + option + " on npc " + npc);
			}
		}, true));
	}
}
