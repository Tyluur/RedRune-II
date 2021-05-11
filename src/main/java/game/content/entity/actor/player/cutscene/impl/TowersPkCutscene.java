package game.content.entity.actor.player.cutscene.impl;

import game.content.entity.actor.player.cutscene.Cutscene;
import game.content.entity.actor.player.cutscene.actions.CutsceneAction;
import game.content.entity.actor.player.cutscene.actions.LookCameraAction;
import game.content.entity.actor.player.cutscene.actions.PosCameraAction;
import game.entity.actor.player.Player;

import java.util.ArrayList;

public class TowersPkCutscene extends Cutscene {

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();

		actionsList.add(new PosCameraAction(getX(player, player.getX() - 5), getY(player, player.getY() + 7), 8000, 6, 6, -1));
		actionsList.add(new LookCameraAction(getX(player, player.getX()), getY(player, player.getY() + 7), 6000, 6, 6, 10));

		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

}
