package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;

public class DismissD extends Dialogue {
	
	@Override
	public void start() {
		this.sendDialogue(SEND_2_OPTIONS, "Dismiss Familiar?", "Yes.", "No.");
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == 1) {
			if (player.getFamiliar() != null) {
				player.getFamiliar().sendDeath(player);
			}
		}
		end();
	}
	
	@Override
	public void finish() {
	
	}
	
}
