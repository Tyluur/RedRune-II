package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.content.action.interaction.PlayerCombatAction;
import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.event.context.NPCEventContext;
import org.redrune.game.content.event.context.NodeReachEventContext;
import org.redrune.game.content.event.impl.NPCEvent;
import org.redrune.game.content.event.impl.NodeReachEvent;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.FaceLocationUpdate;
import org.redrune.game.world.World;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.rs.InteractionOption;

import static org.redrune.utility.rs.InteractionOption.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class NPCInteractionPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The packet id for option
	 */
	private static final int FIRST_NPC_OPTION = 29, SECOND_NPC_OPTION = 10, THIRD_NPC_OPTION = 69, FOURTH_NPC_OPTION = 61, ATTACK_NPC_OPTION = 70, LAST_NPC_OPTION = 27;
	
	@Override
	public int[] bindings() {
		return arguments(FIRST_NPC_OPTION, SECOND_NPC_OPTION, THIRD_NPC_OPTION, FOURTH_NPC_OPTION, ATTACK_NPC_OPTION, LAST_NPC_OPTION);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int index = packet.readLEShortA();
		boolean forceRun = packet.readByteC() == 1;
		
		if (index < 0) {
			System.out.println("Invalid npc index found: " + index);
			return;
		}
		
		NPC npc = World.get().getNpcs().get(index);
		
		// for some reason this npc was found, we still shouldn't interact with it...
		if (npc == null || !npc.isRenderable()) {
			return;
		}
		InteractionOption option = getOptionByOpcode(packet.getOpcode());
		if (option == null) {
			System.out.println("Unable to identify interaction option for opcode " + packet.getOpcode());
			return;
		}
		// different options handled differently
		switch (option) {
			case ATTACK_OPTION:
				// stop everything
				player.stop(true, true, true, false);
				// face the player
				player.getUpdateMasks().register(new FaceLocationUpdate(player, npc.getLocation()));
				// make sure we aren't at multi
				
				if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
					if (player.getAttackedBy() != npc && player.getAttackedByDelay() > System.currentTimeMillis()) {
						player.getTransmitter().sendMessage("You are already in combat.");
						return;
					}
					if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > System.currentTimeMillis()) {
						player.getTransmitter().sendMessage("This npc is already in combat.");
						return;
					}
				}
				// make sure we can fight in the activity
				if (player.getManager().getActivities().handleNodeInteraction(npc, InteractionOption.ATTACK_OPTION)) {
					player.getManager().getActions().startAction(new PlayerCombatAction(npc));
				}
				break;
			case EXAMINE:
				player.getTransmitter().sendMessage(npc.toString(), true);
				break;
			default:
				player.getMovement().reset(forceRun);
				EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(npc, () -> {
					// executing the npc interaction event on arrival
					EventRepository.executeEvent(player, NPCEvent.class, new NPCEventContext(npc, option));
				}));
				break;
		}
	}
	
	/**
	 * Gets the {@link InteractionOption} {@code Object} for the option clicked on the {@code NPC}
	 *
	 * @param opcode
	 * 		The option's opcode
	 */
	private InteractionOption getOptionByOpcode(int opcode) {
		switch (opcode) {
			case ATTACK_NPC_OPTION:
				return ATTACK_OPTION;
			case FIRST_NPC_OPTION:
				return FIRST_OPTION;
			case SECOND_NPC_OPTION:
				return SECOND_OPTION;
			case THIRD_NPC_OPTION:
				return THIRD_OPTION;
			case FOURTH_NPC_OPTION:
				return FOURTH_OPTION;
			case LAST_NPC_OPTION:
				return EXAMINE;
			default:
				return null;
		}
	}
}
