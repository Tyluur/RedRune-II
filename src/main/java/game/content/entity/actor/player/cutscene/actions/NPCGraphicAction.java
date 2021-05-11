package game.content.entity.actor.player.cutscene.actions;

import game.entity.actor.mask.Graphics;
import game.entity.actor.npc.NPC;
import game.entity.actor.player.Player;

public class NPCGraphicAction extends CutsceneAction {
	
	private final Graphics gfx;
	
	public NPCGraphicAction(int cachedObjectIndex, Graphics gfx, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.gfx = gfx;
	}
	
	@Override
	public void process(Player player, Object[] cache) {
		NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextGraphics(gfx);
	}
	
}
