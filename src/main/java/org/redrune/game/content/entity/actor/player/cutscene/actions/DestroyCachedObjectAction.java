package org.redrune.game.content.entity.actor.player.cutscene.actions;

import org.redrune.game.content.entity.actor.player.cutscene.Cutscene;
import org.redrune.game.entity.actor.player.Player;

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
