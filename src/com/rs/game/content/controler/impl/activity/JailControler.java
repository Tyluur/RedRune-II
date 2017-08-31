package com.rs.game.content.controler.impl.activity;

import com.rs.game.GameConstants;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.content.controler.Controler;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.utility.Misc;

public class JailControler extends Controler {

	@Override
	public void start() {
		if (player.getJailed() > Misc.currentTimeMillis())
			player.sendRandomJail(player);
	}

	@Override
	public void process() {
		if (player.getJailed() <= Misc.currentTimeMillis()) {
			player.getControlerManager().getControler().removeControler();
			player.getPackets().sendGameMessage(
					"Your account has been unmuted.", true);
			player.setNextWorldTile(GameConstants.RESPAWN_PLAYER_LOCATION);
		}
	}

	public static void stopControler(Player p) {
		p.getControlerManager().getControler().removeControler();
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.stopAll();
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(-1));
					player.reset();
					player.setCanPvp(false);
					player.sendRandomJail(player);
					player.resetLockDelay();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {

		return false;
	}

	@Override
	public boolean logout() {

		return false;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getPackets().sendGameMessage(
				"You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getPackets().sendGameMessage(
				"You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		player.getPackets().sendGameMessage(
				"You cannot do any activities while being jailed.");
		return false;
	}

}
