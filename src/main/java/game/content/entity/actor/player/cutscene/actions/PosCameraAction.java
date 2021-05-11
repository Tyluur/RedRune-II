package game.content.entity.actor.player.cutscene.actions;

import game.content.entity.actor.player.cutscene.Cutscene;
import game.entity.actor.player.Player;

public class PosCameraAction extends CutsceneAction {

	private final int moveLocalX;

	private final int moveLocalY;

	private final int moveZ;

	private final int speed;

	private final int speed2;

	public PosCameraAction(int moveLocalX, int moveLocalY, int moveZ, int actionDelay) {
		this(moveLocalX, moveLocalY, moveZ, -1, -1, actionDelay);
	}

	public PosCameraAction(int moveLocalX, int moveLocalY, int moveZ, int speed, int speed2, int actionDelay) {
		super(-1, actionDelay);
		this.moveLocalX = moveLocalX;
		this.moveLocalY = moveLocalY;
		this.moveZ = moveZ;
		this.speed = speed;
		this.speed2 = speed2;
	}

	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		player.getPackets().sendCameraPos(scene.getLocalX(player, moveLocalX), scene.getLocalY(player, moveLocalY), moveZ, speed, speed2);
	}

}
