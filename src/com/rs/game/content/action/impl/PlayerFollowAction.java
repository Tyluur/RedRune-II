package com.rs.game.content.action.impl;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.content.action.Action;
import com.rs.utility.Misc;

public class PlayerFollowAction extends Action {

	private Player target;

	public PlayerFollowAction(Player target) {
		this.target = target;
	}

	@Override
	public boolean start(Player player) {
		player.setNextFaceActor(target);
		if (checkAll(player))
			return true;
		player.setNextFaceActor(null);
		return false;
	}

	private boolean checkAll(Player player) {
		if (player.isDead() || player.hasFinished() || target.isDead()
				|| target.hasFinished())
			return false;
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		int size = target.getSize();
		int maxDistance = 16;
		if (player.getPlane() != target.getPlane()
				|| distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance)
			return false;
		if (player.getFreezeDelay() >= Misc.currentTimeMillis())
			return true;
		maxDistance = 0;
		if ((!player.clipedProjectile(target, true))
				|| distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance) {
			if (player.hasWalkSteps())
				player.resetWalkSteps();
			player.addWalkStepsInteract(target.getX(), target.getY(),
					player.getRun() ? 2 : 1, size, true);
			// }
			return true;
		} else {
			player.resetWalkSteps();
			int lastFaceEntity = target.getLastFaceEntity();
			if (lastFaceEntity >= 32768) {
				lastFaceEntity -= 32768;
				if (lastFaceEntity == player.getIndex())
					player.addWalkSteps(target.getLastWorldTile().getX(),
							target.getLastWorldTile().getY(), size, true);
			}
		}

		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		return 0;
	}

	@Override
	public void stop(Player player) {
		player.setNextFaceActor(null);

	}

}
