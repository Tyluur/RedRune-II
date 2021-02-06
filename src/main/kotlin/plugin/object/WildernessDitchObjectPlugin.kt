package plugin.object;

import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.content.plugin.type.ObjectPlugin;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.ForceMovement;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-31
 */
public class WildernessDitchObjectPlugin implements ObjectPlugin {
	
	@Override
	public boolean handle(Player player, WorldObject object, String option) {
		performJump(player, object, null);
		return true;
	}
	
	@Override
	public void register() {
		for (int i = 1440; i <= 1444; i++) {
			registerSpecifiedOption(ClickOption.FIRST, i);
		}
	}
	
	public static void performJump(Player player, WorldObject object, final Runnable onJump) {
		player.getLocks().lock();
		player.setNextAnimation(new Animation(6132));
		final boolean in = player.getY() < object.getY();
		final WorldTile toTile = new WorldTile(player.getX(), in ? object.getY() + 2 : object.getY() - 1, object.getPlane());
		player.setNextForceMovement(new ForceMovement(new WorldTile(player), 1, toTile, 2, in ? ForceMovement.NORTH : ForceMovement.SOUTH));
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				WorldTile faceTile = new WorldTile(player.getX(), player.getY() + (in ? 1 : -1), player.getPlane());
				player.setNextFaceWorldTile(faceTile);
				player.setNextWorldTile(toTile);
				player.getLocks().unlock();
				if (onJump != null) {
					onJump.run();
				}
			}
		}, 2);
	}
}
