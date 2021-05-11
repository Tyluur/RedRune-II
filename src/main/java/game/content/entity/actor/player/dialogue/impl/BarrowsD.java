package game.content.entity.actor.player.dialogue.impl;

import game.content.entity.actor.player.dialogue.Dialogue;
import game.global.WorldTile;

public class BarrowsD extends Dialogue {
	
	@Override
	public void start() {
		sendDialogue(SEND_1_TEXT_CHAT, "", "You've found a hidden tunnel, do you want to enter?");
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue(SEND_2_LARGE_OPTIONS, DEFAULT_OPTION, "Yes, I'm fearless.", "No way, that looks scary!");
		} else if (stage == 0) {
			if (componentId == 2) {
				player.setNextWorldTile(new WorldTile(3551, 9692, 0));
				player.getTemporaryAttributes().put("lootedChest", Boolean.FALSE);
			}
			end();
		}
	}
	
	@Override
	public void finish() {
	
	}
	
}
