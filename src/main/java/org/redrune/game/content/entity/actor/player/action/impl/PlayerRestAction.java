package org.redrune.game.content.entity.actor.player.action.impl;

import org.redrune.game.content.entity.actor.player.action.Action;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.functions.Misc;

public class PlayerRestAction extends Action {
	
	private static final int[][] REST_DEFS = { { 5713, 1549, 5748 }, { 11786, 1550, 11788 }, { 5713, 1551, 2921 }
	};
	
	private int index;
	
	@Override
	public boolean start(Player player) {
		if (!process(player)) {
			return false;
		}
		index = Misc.random(REST_DEFS.length);
		player.getAttributes().setResting(true);
		player.setNextAnimation(new Animation(REST_DEFS[index][0]));
		player.getAppearance().setRenderEmote(REST_DEFS[index][1]);
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		if (player.getPoisonManager().isPoisoned()) {
			player.getPackets().sendMessage("You can't rest while you're poisoned.");
			return false;
		}
		if (player.getAttackedByDelay() + 10000 > Misc.currentTimeMillis()) {
			player.getPackets().sendMessage("You can't rest until 10 seconds after the end of combat.");
			return false;
		}
		return true;
	}
	
	@Override
	public int processWithDelay(Player player) {
		return 0;
	}
	
	@Override
	public void stop(Player player) {
		player.getAttributes().setResting(false);
		player.setNextAnimation(new Animation(REST_DEFS[index][2]));
		player.getEmotesManager().setNextEmoteEnd();
		player.getAppearance().setRenderEmote(-1);
	}
	
}
