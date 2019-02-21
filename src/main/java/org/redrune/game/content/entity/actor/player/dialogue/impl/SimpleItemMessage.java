package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Apr 25, 2015
 */
public class SimpleItemMessage extends Dialogue {
	
	@Override
	public void start() {
		int itemId = (Integer) parameters[0];
		int itemAmount = -1;
		if (parameters.length > 2 && parameters[1] instanceof Integer) {
			itemAmount = getParam(1);
		}
		boolean amountParam = itemAmount != -1;
		if (!amountParam) {
			itemAmount = 1;
		}
		List<String> messages = new ArrayList<>();
		for (int i = 0; i < parameters.length; i++) {
			if (amountParam && i < 2) {
				continue;
			} else if (i == 0) {
				continue;
			}
			messages.add((String) parameters[i]);
		}
		sendItemDialogue(itemId, itemAmount, messages.toArray(new String[messages.size()]));
	}
	
	@Override
	public void run(int interfaceId, int option) {
		end();
	}
	
	@Override
	public void finish() {
	}
	
}
