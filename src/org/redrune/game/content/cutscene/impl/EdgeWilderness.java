package org.redrune.game.content.cutscene.impl;

import org.redrune.game.content.cutscene.Cutscene;
import org.redrune.game.content.cutscene.actions.CutsceneAction;
import org.redrune.game.content.cutscene.actions.LookCameraAction;
import org.redrune.game.content.cutscene.actions.PosCameraAction;
import org.redrune.game.entity.actor.player.Player;

import java.util.ArrayList;

public class EdgeWilderness extends Cutscene {

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();

		actionsList.add(new PosCameraAction(80, 75, 5000, 6, 6, -1));
		actionsList.add(new LookCameraAction(30, 75, 1000, 6, 6, 10));
		actionsList.add(new PosCameraAction(30, 75, 5000, 3, 3, 10));

		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

}
