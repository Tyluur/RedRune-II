package game.content.entity.actor.player.cutscene.actions;

import game.entity.actor.mask.Graphics;
import game.entity.actor.player.Player;

public class PlayerGraphicAction extends CutsceneAction {
	
	private final Graphics gfx;
	
	public PlayerGraphicAction(Graphics gfx, int actionDelay) {
		super(-1, actionDelay);
		this.gfx = gfx;
	}
	
	@Override
	public void process(Player player, Object[] cache) {
		player.setNextGraphics(gfx);
	}
	
}
