package org.redrune.game.content.cutscene.impl;

import org.redrune.game.content.cutscene.Cutscene;
import org.redrune.game.content.cutscene.actions.CutsceneAction;
import org.redrune.game.content.cutscene.actions.MovePlayerAction;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.MagicConstants;

import java.util.ArrayList;

public class NewStartTutorial extends Cutscene {

	private static int Ozan = 1;

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		actionsList.add(new MovePlayerAction(10, 0, 0, MagicConstants.WALK_MOVE_TYPE, 0)); // out
		
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

}
