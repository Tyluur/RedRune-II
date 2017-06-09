package org.redrune.game.module.interaction.object;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.module.type.ObjectInteractionModule;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.FaceLocationUpdate;
import org.redrune.game.node.entity.player.render.flag.impl.ForceMovement;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public class WildernessDitchInteractionModule implements ObjectInteractionModule {
	
	@Override
	public int[] objectSubscriptionIds() {
		return arguments(1444, 1441, 1443, 1442, 1440, 65076, 65077, 65078, 65079, 65080, 65081, 65082, 65083, 65084, 65085, 65086, 65087);
	}
	
	@Override
	public boolean handle(Player player, GameObject object, InteractionOption option) {
		final boolean leaving = player.getLocation().getY() > object.getLocation().getY();
		final Location from = new Location(player.getLocation());
		final Location toLoc = new Location(player.getLocation().getX(), object.getLocation().getY() + (leaving ? -1 : 2), player.getLocation().getPlane());
		
		player.getManager().getLocks().lockAll();
		player.sendAnimation(6132);
		player.getUpdateMasks().register(new ForceMovement(player, new int[] { toLoc.getX(), toLoc.getY(), 33, 60, leaving ? ForceMovement.SOUTH : ForceMovement.NORTH }));
		
		SystemManager.getScheduler().schedule(new ScheduledTask(2, 1, false) {
			@Override
			public Runnable getTask() {
				return () -> {
					player.moveTo(toLoc);
					player.getUpdateMasks().register(new FaceLocationUpdate(player, from));
					SystemManager.getScheduler().schedule(new ScheduledTask(1, 1, false) {
						@Override
						public Runnable getTask() {
							return () -> {
								player.getManager().getLocks().unlockAll();
								stop();
							};
						}
					});
				};
			}
		});
		return true;
	}
}
