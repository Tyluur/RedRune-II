package org.redrune.game.content.cutscene.actions;

import org.redrune.game.content.cutscene.Cutscene;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.MagicConstants;

public class MovePlayerAction extends CutsceneAction {
	
	private int x, y, plane, movementType;
	
	public MovePlayerAction(int x, int y, boolean run, int actionDelay) {
		this(x, y, -1, run ? MagicConstants.RUN_MOVE_TYPE : MagicConstants.WALK_MOVE_TYPE, actionDelay);
	}
	
	public MovePlayerAction(int x, int y, int plane, int movementType, int actionDelay) {
		super(-1, actionDelay);
		this.x = x;
		this.y = y;
		this.plane = plane;
		this.movementType = movementType;
	}
	
	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		if (movementType == MagicConstants.TELE_MOVE_TYPE) {
			player.setNextWorldTile(new WorldTile(scene.getBaseX() + x, scene.getBaseY() + y, plane));
			return;
		}
		player.setRunModeOn(movementType == MagicConstants.RUN_MOVE_TYPE);
		player.addWalkSteps(scene.getBaseX() + x, scene.getBaseY() + y);
	}
	
}
