package com.rs.game.content.cutscene.actions;

import com.rs.game.entity.actor.mask.ForceTalk;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;

public class NPCForceTalkAction extends CutsceneAction {
	
	private String text;
	
	public NPCForceTalkAction(int cachedObjectIndex, String text, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.text = text;
	}
	
	@Override
	public void process(Player player, Object[] cache) {
		NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextForceTalk(new ForceTalk(text));
	}
	
}
