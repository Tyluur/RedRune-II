package org.redrune.net.packet.context.impl;

import org.redrune.game.content.entity.actor.player.event.npc.NPCInteractionEvent;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.net.packet.context.PacketContext;
import org.redrune.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class NPCInteractionPacketContext extends PacketContext {
	
	private final NPC npc;
	
	private final ClickOption option;
	
	private final boolean running;
	
	public NPCInteractionPacketContext(NPC npc, ClickOption option, boolean running) {
		this.npc = npc;
		this.option = option;
		this.running = running;
	}
	
	@Override
	public void handle(Player player) {
		if (running) {
			player.setRunModeOn(true);
		}
		switch (option) {
			case FIRST:
			case SECOND:
			case THIRD:
			case FOURTH:
				player.getEventManager().start(new NPCInteractionEvent(npc, option));
				break;
		}
		
	}
}
