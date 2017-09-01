package com.rs.game.content.cutscene.impl;

import com.rs.game.content.cutscene.Cutscene;
import com.rs.game.content.cutscene.actions.CutsceneAction;
import com.rs.game.content.cutscene.actions.LookCameraAction;
import com.rs.game.content.cutscene.actions.PosCameraAction;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.player.Player;

import java.util.ArrayList;

public class NexCutScene extends Cutscene {

	private WorldTile dir;

	private int selected;

	public NexCutScene(WorldTile dir, int selected) {
		this.dir = dir;
		this.selected = selected;
	}

	@Override
	public boolean hiddenMinimap() {
		return false;
	}

	@Override
	public CutsceneAction[] getActions(Player player) {
		int xExtra = 0;
		int yExtra = 0;
		if (selected == 0) {
			yExtra -= 7;
		} else if (selected == 2) {
			yExtra += 7;
		} else if (selected == 1) {
			xExtra -= 7;
		} else {
			xExtra += 7;
		}
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		actionsList.add(new PosCameraAction(getX(player, 2925 + xExtra), getY(player, 5203 + yExtra), 2500, -1));
		actionsList.add(new LookCameraAction(getX(player, dir.getX()), getY(player, dir.getY()), 2500, 3));
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

}
