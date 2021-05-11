package game.content.entity.actor.player.cutscene.actions;

import game.content.entity.actor.player.cutscene.Cutscene;
import game.entity.actor.npc.NPC;
import game.entity.actor.player.Player;
import game.global.WorldTile;
import utility.constants.MagicConstants;

public class MoveNPCAction extends CutsceneAction {
	
	private final int x;
	private final int y;
	private final int plane;
	private final int movementType;
	
	public MoveNPCAction(int cachedObjectIndex, int x, int y, boolean run, int actionDelay) {
		this(cachedObjectIndex, x, y, -1, run ? MagicConstants.RUN_MOVE_TYPE : MagicConstants.WALK_MOVE_TYPE, actionDelay);
	}
	
	public MoveNPCAction(int cachedObjectIndex, int x, int y, int plane, int movementType, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.x = x;
		this.y = y;
		this.plane = plane;
		this.movementType = movementType;
	}
	
	@Override
	public void process(Player player, Object[] cache) {
		NPC npc = (NPC) cache[getCachedObjectIndex()];
		Cutscene scene = (Cutscene) cache[0];
		if (movementType == MagicConstants.TELE_MOVE_TYPE) {
			npc.setNextWorldTile(new WorldTile(scene.getBaseX() + x, scene.getBaseY() + y, plane));
			return;
		}
		npc.setRunModeOn(movementType == MagicConstants.RUN_MOVE_TYPE);
		npc.addWalkSteps(scene.getBaseX() + x, scene.getBaseY() + y);
	}
	
}
