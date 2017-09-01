package com.rs.game.content.cutscene.actions;

import com.rs.game.content.cutscene.Cutscene;
import com.rs.game.entity.actor.player.Player;

public class DestroyCachedObjectAction extends CutsceneAction {

	public DestroyCachedObjectAction(int cachedObjectIndex, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
	}

	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		scene.destroyCache(cache[getCachedObjectIndex()]);
	}

}
