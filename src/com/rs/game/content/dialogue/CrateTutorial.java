package com.rs.game.content.dialogue;

import com.rs.game.entity.actor.mask.Animation;


public class CrateTutorial extends Dialogue {

	@Override
	public void start() {
		if (!player.getInventory().containsItem(1265, 1)) {
			sendDialogue(SEND_1_TEXT_INFO,
					"-- You find a bronze pick axe. --");
			player.getInventory().addItem(1265, 1);
	         player.setNextAnimation(new Animation(881));
			player.getPrayer().setPrayerBook(true);
		} else {
			sendDialogue(SEND_1_TEXT_INFO,
					"<col=ff0000>-- You already have a bronze pick axe. --" );
			}	
	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}

	@Override
	public void finish() {
		
	}
}
	