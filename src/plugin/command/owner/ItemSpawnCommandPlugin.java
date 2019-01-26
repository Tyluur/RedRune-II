package plugin.command.owner;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Spawns an item", types = { Integer.class })
public class ItemSpawnCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int itemId = intParam(args, 1);
		int amount = intParamOrDefault(args, 2, 1);
		player.getInventory().addItem(itemId, amount);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("item", "pickup");
	}
}
