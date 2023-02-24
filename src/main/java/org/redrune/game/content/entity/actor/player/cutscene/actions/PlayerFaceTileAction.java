package org.redrune.game.content.entity.actor.player.cutscene.actions;

import org.redrune.game.content.entity.actor.player.cutscene.Cutscene;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;

public class PlayerFaceTileAction extends CutsceneAction {

    private final int x;
    private final int y;

    public PlayerFaceTileAction(int x, int y, int actionDelay) {
        super(-1, actionDelay);
        this.x = x;
        this.y = y;
    }

    @Override
    public void process(Player player, Object[] cache) {
        Cutscene scene = (Cutscene) cache[0];
        player.setNextFaceWorldTile(new WorldTile(scene.getBaseX() + x, scene.getBaseY() + y, player.getPlane()));
    }

}
