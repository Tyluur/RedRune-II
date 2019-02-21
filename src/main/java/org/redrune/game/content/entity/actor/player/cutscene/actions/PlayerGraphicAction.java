package org.redrune.game.content.entity.actor.player.cutscene.actions;

import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;

public class PlayerGraphicAction extends CutsceneAction {
	
	private Graphics gfx;
	
	public PlayerGraphicAction(Graphics gfx, int actionDelay) {
		super(-1, actionDelay);
		this.gfx = gfx;
	}
	
	@Override
	public void process(Player player, Object[] cache) {
		player.setNextGraphics(gfx);
	}
	
}
