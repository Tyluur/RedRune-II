package com.rs.game.content.cutscene.actions;

import com.rs.game.content.cutscene.Cutscene;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.world.World;

public class CreateNPCAction extends CutsceneAction {

	private int id, x, y, plane;

	public CreateNPCAction(int cachedObjectIndex, int id, int x, int y, int plane, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.id = id;
		this.x = x;
		this.y = y;
		this.plane = plane;
	}

	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		if (cache[getCachedObjectIndex()] != null) {
			scene.destroyCache(cache[getCachedObjectIndex()]);
		}
		NPC npc = (NPC) (cache[getCachedObjectIndex()] = World.spawnNPC(id, new WorldTile(scene.getBaseX() + x, scene.getBaseY() + y, plane), -1, true, true));
		npc.setRandomWalk(false);
	}

}
