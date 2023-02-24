package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;
import org.redrune.game.global.WorldTile;

public class ClimbNoEmoteStairs extends Dialogue {

    private WorldTile upTile;

    private WorldTile downTile;

    // uptile, downtile, climbup message, climbdown message, emoteid
    @Override
    public void start() {
        upTile = (WorldTile) parameters[0];
        downTile = (WorldTile) parameters[1];
        sendDialogue(SEND_3_LARGE_OPTIONS, "What would you like to do?", (String) parameters[2], (String) parameters[3], "Never mind.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        if (interfaceId == SEND_3_LARGE_OPTIONS && componentId == 2) {
            player.useStairs(-1, upTile, 0, 1);
        } else if (interfaceId == SEND_3_LARGE_OPTIONS && componentId == 3) {
            player.useStairs(-1, downTile, 0, 1);
        }
        end();
    }

    @Override
    public void finish() {

    }

}
