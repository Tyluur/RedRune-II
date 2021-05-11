package game.content.entity.actor.player.cutscene.impl;

import game.content.entity.actor.player.cutscene.Cutscene;
import game.content.entity.actor.player.cutscene.actions.CutsceneAction;
import game.content.entity.actor.player.cutscene.actions.MovePlayerAction;
import game.entity.actor.player.Player;
import utility.constants.MagicConstants;

import java.util.ArrayList;

public class NewStartTutorial extends Cutscene {

	private static final int Ozan = 1;

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
