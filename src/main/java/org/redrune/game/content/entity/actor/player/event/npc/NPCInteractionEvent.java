package org.redrune.game.content.entity.actor.player.event.npc;

import org.redrune.game.GameFlags;
import org.redrune.game.content.entity.actor.player.dialogue.impl.Banker;
import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.content.entity.actor.player.skills.fishing.Fishing;
import org.redrune.game.content.entity.actor.player.skills.fishing.Fishing.FishingSpots;
import org.redrune.game.content.entity.actor.player.skills.thieving.PickPocketAction;
import org.redrune.game.content.entity.actor.player.skills.thieving.PickPocketableNPC;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.RouteEvent;
import org.redrune.utility.game.ClickOption;

import static org.redrune.utility.game.ClickOption.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-25
 */
public class NPCInteractionEvent extends Event {
	
	/**
	 * The npc to interact with
	 */
	private final NPC npc;
	
	/**
	 * The clickOption the player used on the npc
	 */
	private final ClickOption clickOption;
	
	public NPCInteractionEvent(NPC npc, ClickOption clickOption) {
		this.npc = npc;
		this.clickOption = clickOption;
	}
	
	@Override
	public void run(Player player) {
		player.setNextFaceActor(npc);
		player.setRouteEvent(new RouteEvent(npc, () -> {
			npc.resetWalkSteps();
			switch (clickOption) {
				case FIRST:
					handleFirstOption(player);
					break;
				case SECOND:
					handleSecondOption(player);
					break;
				case THIRD:
					handleThirdOption(player);
					break;
				case FOURTH:
					handleFourthOption(player);
					break;
			}
		}, true));
	}
	
	@Override
	public EventPolicy[] policies() {
		return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK);
	}
	
	/**
	 * Performs the necessary actions when the player arrives at the npc after a first option packet
	 *
	 * @param player
	 * 		The player
	 */
	private void handleFirstOption(Player player) {
		String option = npc.getDefinitions().getOption(1);
		FishingSpots spot = FishingSpots.forId(npc.getId() | 1 << 24);
		if (spot != null) {
			player.getActionManager().setAction(new Fishing(spot, npc));
			return;
		}
		player.getInteractionManager().startInteraction(npc); // if its a spot, they dont interact with players
		if (!player.getControllerManager().canEntityClick(npc, FIRST)) {
			return;
		}
		if (PluginRepository.handleNPC(player, npc, option)) {
			return;
		}
		if (npc.getDefinitions().getName().contains("Banker") || npc.getDefinitions().getName().contains("banker")) {
			player.getDialogueManager().startDialogue(Banker.class, npc.getId());
		} else {
			player.getPackets().sendMessage("Nothing interesting happens.");
			if (GameFlags.debugMode) {
				System.out.println("No plugin registered for option " + option + " on npc " + npc);
			}
		}
	}
	
	/**
	 * Performs the necessary actions when the player arrives at the npc after a second option packet
	 *
	 * @param player
	 * 		The player
	 */
	private void handleSecondOption(Player player) {
		String option = npc.getDefinitions().getOption(2);
		if (option == null) {
			throw new IllegalStateException("Unable to perform event due to undefined option. [npc=" + npc + ", clickOption=" + clickOption + "]");
		}
		FishingSpots spot = FishingSpots.forId(npc.getId() | (2 << 24));
		if (spot != null) {
			player.getActionManager().setAction(new Fishing(spot, npc));
			return;
		}
		player.getInteractionManager().startInteraction(npc); // if its a spot, they dont interact with players
		PickPocketableNPC pocket = PickPocketableNPC.get(npc.getId());
		if (pocket != null) {
			player.getActionManager().setAction(new PickPocketAction(npc, pocket));
			return;
		}
		if (npc instanceof Familiar) {
			if (player.getFamiliar() != npc) {
				player.getPackets().sendMessage("That isn't your familiar.");
				return;
			}
			if (npc.getDefinitions().hasOption("store")) {
				player.getFamiliar().store();
			} else if (npc.getDefinitions().hasOption("cure")) {
				if (!player.getPoisonManager().isPoisoned()) {
					player.getPackets().sendMessage("Your aren't poisoned or diseased.");
					return;
				} else {
					player.getFamiliar().drainSpecial(2);
					player.getAttributes().addPoisonImmune(120);
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
			player.getPackets().sendMessage("Nothing interesting happens.");
			if (GameFlags.debugMode) {
				System.out.println("No plugin registered for option " + option + " on npc " + npc);
			}
		}
	}
	
	/**
	 * Performs the necessary actions when the player arrives at the npc after a third option packet
	 *
	 * @param player
	 * 		The player
	 */
	private void handleThirdOption(Player player) {
		String option = npc.getDefinitions().getOption(3);
		if (option == null) {
			throw new IllegalStateException("Unable to perform event due to undefined option. [npc=" + npc + ", clickOption=" + clickOption + "]");
		}
		player.getInteractionManager().startInteraction(npc); // if its a spot, they dont interact with players
		if (!player.getControllerManager().canEntityClick(npc, THIRD)) {
			return;
		}
		if (PluginRepository.handleNPC(player, npc, option)) {
			return;
		}
		player.getPackets().sendMessage("Nothing interesting happens.");
		if (GameFlags.debugMode) {
			System.out.println("No plugin registered for option " + option + " on npc " + npc);
		}
	}
	
	/**
	 * Performs the necessary actions when the player arrives at the npc after a fourth option packet
	 *
	 * @param player
	 * 		The player
	 */
	private void handleFourthOption(Player player) {
		String option = npc.getDefinitions().getOption(4);
		if (option == null) {
			throw new IllegalStateException("Unable to perform event due to undefined option. [npc=" + npc + ", clickOption=" + clickOption + "]");
		}
		player.getInteractionManager().startInteraction(npc); // if its a spot, they dont interact with players
		if (!player.getControllerManager().canEntityClick(npc, FOURTH)) {
			return;
		}
		if (PluginRepository.handleNPC(player, npc, option)) {
			return;
		}
		player.getPackets().sendMessage("Nothing interesting happens.");
		if (GameFlags.debugMode) {
			System.out.println("No plugin registered for option " + option + " on npc " + npc);
		}
	}
	
}
