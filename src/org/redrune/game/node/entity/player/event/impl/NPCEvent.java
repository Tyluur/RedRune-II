package org.redrune.game.node.entity.player.event.impl;

import org.redrune.game.content.dialogue.DialogueRepository;
import org.redrune.game.module.ModuleRepository;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.context.NPCEventContext;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class NPCEvent extends Event<NPCEventContext> {
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public NPCEvent(NPCEventContext context) {
		super(context);
	}
	
	@Override
	public boolean canStart(Player player) {
		return !player.getManager().getLocks().isLocked(LockType.NPC_INTERACTION);
	}
	
	@Override
	public void run(Player player) {
		NPC npc = getContext().getNpc();
		InteractionOption option = getContext().getOption();
		
		player.turnTo(npc);
		npc.startPlayerInteraction(player);
		
		if (option == InteractionOption.FIRST_OPTION && DialogueRepository.handleNPC(player, npc)) {
			return;
		}
		if (ModuleRepository.handle(player, npc, option)) {
			return;
		}
		player.getTransmitter().sendMessage("Nothing interesting happens.");
		// TODO npc dialogues next
	}
}
