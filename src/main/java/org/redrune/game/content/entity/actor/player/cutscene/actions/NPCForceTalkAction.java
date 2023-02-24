package org.redrune.game.content.entity.actor.player.cutscene.actions;

import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;

public class NPCForceTalkAction extends CutsceneAction {

    private final String text;

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
