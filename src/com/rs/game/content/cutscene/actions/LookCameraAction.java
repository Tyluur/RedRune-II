package com.rs.game.content.cutscene.actions;

import com.rs.game.content.cutscene.Cutscene;
import com.rs.game.entity.actor.player.Player;

public class LookCameraAction extends CutsceneAction {
	
	private int viewLocalX;
	
	private int viewLocalY;
	
	private int viewZ;
	
	private int speed;
	
	private int speed2;
	
	public LookCameraAction(int viewLocalX, int viewLocalY, int viewZ, int actionDelay) {
		this(viewLocalX, viewLocalY, viewZ, -1, -1, actionDelay);
	}
	
	public LookCameraAction(int viewLocalX, int viewLocalY, int viewZ, int speed, int speed2, int actionDelay) {
		super(-1, actionDelay);
		this.viewLocalX = viewLocalX;
		this.viewLocalY = viewLocalY;
		this.viewZ = viewZ;
		this.speed = speed;
		this.speed2 = speed2;
	}
	
	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		player.getPackets().sendCameraLook(scene.getLocalX(player, viewLocalX), scene.getLocalY(player, viewLocalY), viewZ, speed, speed2);
	}
	
}
