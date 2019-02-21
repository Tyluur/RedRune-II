package org.redrune.game.content.entity.actor.player.cutscene.actions;

import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.player.Player;

public class PlayerForceTalkAction extends CutsceneAction {

	private String text;

	public PlayerForceTalkAction(String text, int actionDelay) {
		super(-1, actionDelay);
		this.text = text;
	}

	@Override
	public void process(Player player, Object[] cache) {
		player.setNextForceTalk(new ForceTalk(text));
	}

}
