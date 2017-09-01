package com.rs.game.content.cutscene.impl;

import com.rs.game.content.cutscene.Cutscene;
import com.rs.game.content.cutscene.actions.CutsceneAction;
import com.rs.game.content.cutscene.actions.MovePlayerAction;
import com.rs.game.entity.actor.player.Player;

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
		actionsList.add(new MovePlayerAction(10, 0, 0, Player.WALK_MOVE_TYPE, 0)); // out
		
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

}
