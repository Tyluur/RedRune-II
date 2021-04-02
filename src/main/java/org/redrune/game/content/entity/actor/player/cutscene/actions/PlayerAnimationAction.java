package org.redrune.game.content.entity.actor.player.cutscene.actions;

import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;

public class PlayerAnimationAction extends CutsceneAction {

	private final Animation anim;

	public PlayerAnimationAction(Animation anim, int actionDelay) {
		super(-1, actionDelay);
		this.anim = anim;
	}

	@Override
	public void process(Player player, Object[] cache) {
		player.setNextAnimation(anim);
	}

}
