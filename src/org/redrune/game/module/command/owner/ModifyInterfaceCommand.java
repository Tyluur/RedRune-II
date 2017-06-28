package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
@CommandManifest(description = "Changes the viewable components of an interface", types = { Integer.class, Integer.class, Boolean.class})
public class ModifyInterfaceCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("modifyinterface");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.getManager().getInterfaces().sendInterfaceChange(intParam(args, 1), intParam(args, 2), boolParam(args, 3));
	}
}
