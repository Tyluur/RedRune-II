package game.content.entity.actor.player.cutscene.actions;

import game.entity.actor.mask.Animation;
import game.entity.actor.npc.NPC;
import game.entity.actor.player.Player;

public class NPCAnimationAction extends CutsceneAction {
	
	private final Animation anim;
	
	public NPCAnimationAction(int cachedObjectIndex, Animation anim, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.anim = anim;
	}
	
	@Override
	public void process(Player player, Object[] cache) {
		NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextAnimation(anim);
	}
	
}
