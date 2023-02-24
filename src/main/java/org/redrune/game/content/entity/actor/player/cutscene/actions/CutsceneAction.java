package org.redrune.game.content.entity.actor.player.cutscene.actions;

import org.redrune.game.entity.actor.player.Player;

public abstract class CutsceneAction {

    private final int actionDelay; // -1 for no delay
    private final int cachedObjectIndex;

    public CutsceneAction(int cachedObjectIndex, int actionDelay) {
        this.cachedObjectIndex = cachedObjectIndex;
        this.actionDelay = actionDelay;
    }

    public abstract void process(Player player, Object[] cache);

    public int getActionDelay() {
        return actionDelay;
    }

    public int getCachedObjectIndex() {
        return cachedObjectIndex;
    }

}
