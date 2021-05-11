package game.content.entity.actor.player.cutscene.actions;

import game.content.entity.actor.player.cutscene.Cutscene;
import game.entity.actor.npc.NPC;
import game.entity.actor.player.Player;
import game.global.WorldTile;

public class NPCFaceTileAction extends CutsceneAction {
	
	private final int x;
	private final int y;
	
	public NPCFaceTileAction(int cachedObjectIndex, int x, int y, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextFaceWorldTile(new WorldTile(scene.getBaseX() + x, scene.getBaseY() + y, npc.getPlane()));
	}
	
}
