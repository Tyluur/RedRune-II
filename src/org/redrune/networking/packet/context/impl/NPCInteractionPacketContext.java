package org.redrune.networking.packet.context.impl;

import org.redrune.game.GameFlags;
import org.redrune.game.content.entity.actor.player.event.npc.NPCInteractionEvent;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.utility.game.ClickOption;
import org.redrune.utility.game.repository.npc.characteristic.NPCCharacteristicRepository;
import org.redrune.utility.game.repository.npc.spawn.NPCSpawnRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
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
			case EXAMINE:
				if (player.getTemporaryAttribute("removing_npcs", false)) {
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
}
