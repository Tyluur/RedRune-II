package org.redrune.network.rs666.packet.incoming.impl;

import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.event.context.NPCEventContext;
import org.redrune.game.content.event.context.NodeReachEventContext;
import org.redrune.game.content.event.impl.NPCEvent;
import org.redrune.game.content.event.impl.NodeReachEvent;
import org.redrune.game.world.World;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.tool.Misc;
import org.redrune.utility.repository.EventRepository;
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
		InteractionOption option = getOptionByOpcode(packet.getOpcode());
		if (option == null) {
			LOGGER.severe("Unable to identify interaction option for opcode " + packet.getOpcode());
			return;
		}
		if (option != InteractionOption.EXAMINE) {
			player.getMovement().reset(forceRun);
			EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(npc, () -> {
				// executing the npc interaction event on arrival
				EventRepository.executeEvent(player, NPCEvent.class, new NPCEventContext(npc, option));
			}));
		} else {
			player.getTransmitter().sendMessage(npc.toString(), true);
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
			case LAST_NPC_OPTION:
				return InteractionOption.EXAMINE;
			default:
				return null;
		}
	}
}
