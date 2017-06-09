package org.redrune.game.module.command.moderator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
@CommandManifest(description = "Shows your location coordinates")
public class MyPositionCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("pos");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.getTransmitter().sendMessage("Location[" + player.getLocation().toString() + "]");
		player.getTransmitter().sendMessage("My Regions:" + (player.getMapRegionsIds()));
	}
}
