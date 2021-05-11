package game.content.entity.actor.player.skills.agility;

import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.ForceMovement;
import game.entity.actor.player.Player;
import game.entity.object.WorldObject;
import game.global.WorldTile;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 3/31/2016
 */
public class Shortcuts {

	public static void handleEdgevilleUnderwallTunnel(Player player, WorldObject object) {
		final boolean toGe = object.getId() == 9311;
		player.getLocks().lock();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				switch(ticksPassed) {
					case 1:
						player.setNextAnimation(new Animation(2589));
						player.setNextForceMovement(new ForceMovement(toGe ? new WorldTile(3139, 3516, 0) : new WorldTile(3143, 3514, 0), 1, toGe ? ForceMovement.EAST : ForceMovement.WEST));
						break;
					case 2:
						player.setNextAnimation(new Animation(2590));
						player.setNextWorldTile(new WorldTile(3141, 3515, 0));
						break;
					case 3:
						player.setNextAnimation(new Animation(2591));
						player.setNextWorldTile(toGe ? new WorldTile(3143, 3514, 0) : new WorldTile(3139, 3516, 0));
						player.setNextForceMovement(new ForceMovement(toGe ? new WorldTile(3144, 3514, 0) : new WorldTile(3138, 3516, 0), 1, toGe ? ForceMovement.EAST : ForceMovement.WEST));
						break;
					case 4:
						player.setNextWorldTile(toGe ? new WorldTile(3144, 3514, 0) : new WorldTile(3138, 3516, 0));
						player.getLocks().unlock();
						stop();
						break;
				}
			}
		}, 1, 1);
	}

}
