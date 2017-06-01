package org.redrune.game.module.command.owner;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.Animation;
import org.redrune.game.node.entity.player.render.flag.impl.Graphic;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.backend.Priority;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "You never know what this will do!")
public class DebugCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("dbg");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		try {
			
			Animation MODERN_ANIM = new Animation(8939, 0, false, Priority.HIGHEST);
			Graphic MODERN_GRAPHIC = new Graphic(1576, 0, 0, false);
			
			player.getUpdateMasks().register(MODERN_ANIM);
			player.getUpdateMasks().register(MODERN_GRAPHIC);
			
			SystemManager.getScheduler().schedule(new ScheduledTask(3, 1, false, () -> {
				player.getUpdateMasks().register(new Animation(-1));
				player.getUpdateMasks().register(new Graphic(-1));
				player.putAttribute(AttributeKey.TELEPORT_LOCATION, Location.create(Integer.parseInt(args[1]), Integer.parseInt(args[2]), 0));
			}));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}