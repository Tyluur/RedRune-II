package org.redrune.game.module.command.owner;

import org.redrune.cache.Cache;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "Debugs an interface", types = { Integer.class })
public class DebugInterfaceCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("dbi");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		int interId = intParam(args, 1);
		int length = Cache.getAmountOfComponents(interId);
		for (int index = 0; index < length; index++) {
			player.getManager().getInterfaces().sendInterfaceText(interId, index, "" + index);
		}
		player.getManager().getInterfaces().sendInterface(interId, true);
	}
}
