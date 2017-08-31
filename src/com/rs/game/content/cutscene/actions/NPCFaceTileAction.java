package com.rs.game.content.cutscene.actions;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.content.cutscene.Cutscene;

public class NPCFaceTileAction extends CutsceneAction {

	private int x, y;

	public NPCFaceTileAction(int cachedObjectIndex, int x, int y,
			int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.x = x;
		this.y = y;
	}

	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextFaceWorldTile(new WorldTile(scene.getBaseX() + x, scene
				.getBaseY() + y, npc.getPlane()));
	}

}
