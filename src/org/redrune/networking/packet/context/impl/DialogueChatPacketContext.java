package org.redrune.networking.packet.context.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class DialogueChatPacketContext extends PacketContext {
	
	private final int interfaceId;
	
	private final int componentId;
	
	public DialogueChatPacketContext(int interfaceId, int componentId) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
	}
	
	@Override
	public void handle(Player player) {
		player.getDialogueManager().continueDialogue(interfaceId, componentId);
	}
}
