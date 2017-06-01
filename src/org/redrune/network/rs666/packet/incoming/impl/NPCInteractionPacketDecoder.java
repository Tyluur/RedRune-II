package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.module.ModuleRepository;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.context.NodeInteractionEventContext;
import org.redrune.game.node.entity.player.event.impl.NodeInteractionEvent;
import org.redrune.game.world.World;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.InteractionOption;

import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class NPCInteractionPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The packet id for option
	 */
	private static final int FIRST_NPC_OPTION = 29, SECOND_NPC_OPTION = 10, THIRD_NPC_OPTION = 69, FOURTH_NPC_OPTION = 61, ATTACK_NPC_OPTION = 70, LAST_NPC_OPTION = 27;
	
	/**
	 * The logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(NPCInteractionPacketDecoder.class);
	
	@Override
	public int[] bindings() {
		return arguments(FIRST_NPC_OPTION, SECOND_NPC_OPTION, THIRD_NPC_OPTION, FOURTH_NPC_OPTION, ATTACK_NPC_OPTION, LAST_NPC_OPTION);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int index = packet.readLEShortA();
		boolean forceRun = packet.readByteC() == 1;
		
		if (index < 0) {
			LOGGER.severe("Invalid npc index found: " + index);
			return;
		}
		
		NPC npc = World.get().getNpcs().get(index);
		
		// for some reason this npc was found, we still shouldn't interact with it...
		if (npc == null || !npc.isRenderable()) {
			return;
		}
		
		if (packet.getOpcode() != LAST_NPC_OPTION) {
			InteractionOption option = getOptionByOpcode(packet.getOpcode());
			if (option == null) {
				LOGGER.severe("Unable to identify interaction option for opcode " + packet.getOpcode());
				return;
			}
			player.getWalkingQueue().reset(forceRun);
			player.getManager().getEvents().addEvent(new NodeInteractionEvent(new NodeInteractionEventContext(npc, () -> {
				player.turnTo(npc);
				npc.startPlayerInteraction(player);
				
				ModuleRepository.handle(player, npc, option);
				player.getTransmitter().sendMessage("option=" + option + ", " + npc, true);
			})));
		} else {
			// TODO: npc examine click
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
				return InteractionOption.ATTACK_OPTION;
			case FIRST_NPC_OPTION:
				return InteractionOption.FIRST_OPTION;
			case SECOND_NPC_OPTION:
				return InteractionOption.SECOND_OPTION;
			case THIRD_NPC_OPTION:
				return InteractionOption.THIRD_OPTION;
			case FOURTH_NPC_OPTION:
				return InteractionOption.FOURTH_OPTION;
			default:
				return null;
		}
	}
}
