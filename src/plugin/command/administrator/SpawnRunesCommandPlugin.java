package plugin.command.administrator;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
@CommandManifest(description = "Spawns all the runes you'll ever need")
public class SpawnRunesCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		final int amount = 10_000;
		for (int i = 554; i <= 566; i++) {
			player.getInventory().addItem(i, amount);
		}
		player.getInventory().addItem(9075, amount);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("runes");
	}
}
