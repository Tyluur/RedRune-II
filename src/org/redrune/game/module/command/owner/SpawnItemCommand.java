package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "Spawns an item by its id", types = { Integer.class, Integer.class })
public class SpawnItemCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("item");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.getInventory().addItem(intParam(args, 1), intParam(args, 2));
	}
}
