package org.redrune.game.content.entity.actor.player.cutscene.impl;

import org.redrune.game.content.entity.actor.player.cutscene.Cutscene;
import org.redrune.game.content.entity.actor.player.cutscene.actions.CutsceneAction;
import org.redrune.game.content.entity.actor.player.cutscene.actions.LookCameraAction;
import org.redrune.game.content.entity.actor.player.cutscene.actions.PosCameraAction;
import org.redrune.game.entity.actor.player.Player;

import java.util.ArrayList;

public class DTPreview extends Cutscene {

    @Override
    public boolean hiddenMinimap() {
        return false;
    }

    @Override
    public CutsceneAction[] getActions(Player player) {
        ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
        actionsList.add(new LookCameraAction(getX(player, 3386), getY(player, 3104), 1000, 6, 6, -1));
        actionsList.add(new PosCameraAction(getX(player, 3395), getY(player, 3104), 5000, 7, 8, 5));
        actionsList.add(new LookCameraAction(getX(player, 3390), getY(player, 3115), 6000, 6, 6, -1));
        actionsList.add(new PosCameraAction(getX(player, 3395), getY(player, 3115), 6000, 7, 8, 5));
        actionsList.add(new LookCameraAction(getX(player, 3380), getY(player, 3125), 6000, 6, 6, -1));
        actionsList.add(new PosCameraAction(getX(player, 3380), getY(player, 3125), 6000, 7, 6, 5));
        actionsList.add(new LookCameraAction(getX(player, 3375), getY(player, 3125), 6000, 6, 6, -1));
        actionsList.add(new PosCameraAction(getX(player, 3375), getY(player, 3125), 6000, 7, 6, 5));
        actionsList.add(new LookCameraAction(getX(player, 3370), getY(player, 3125), 6000, 6, 6, -1));
        actionsList.add(new PosCameraAction(getX(player, 3370), getY(player, 3125), 6000, 7, 6, 5));
        actionsList.add(new LookCameraAction(getX(player, 3358), getY(player, 3120), 5500, 6, 6, -1));
        actionsList.add(new PosCameraAction(getX(player, 3358), getY(player, 3125), 5500, 7, 6, 5));
        actionsList.add(new LookCameraAction(getX(player, 3358), getY(player, 3095), 4000, 6, 6, -1));
        actionsList.add(new PosCameraAction(getX(player, 3358), getY(player, 3095), 4000, 7, 8, 5));
        actionsList.add(new LookCameraAction(getX(player, 3374), getY(player, 3084), 2500, 6, 6, -1));
        actionsList.add(new PosCameraAction(getX(player, 3374), getY(player, 3084), 2500, 7, 8, 5));
        actionsList.add(new LookCameraAction(getX(player, 3374), getY(player, 3097), 2300, 9, 9, -1));
        actionsList.add(new PosCameraAction(getX(player, 3374), getY(player, 3097), 2300, 7, 8, 5));
        return actionsList.toArray(new CutsceneAction[actionsList.size()]);
    }

}
