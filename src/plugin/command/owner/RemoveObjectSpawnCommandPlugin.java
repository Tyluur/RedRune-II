package plugin.command.owner;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public class RemoveObjectSpawnCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		player.putAttribute("removing_objects", !player.getTemporaryAttribute("removing_objects", false));
		player.getPackets().sendGameMessage("You are now " + (player.getTemporaryAttribute("removing_objects", false) ? "removing" : "examining") + " objects.");
	}
	
	@Override
	public String[] identifiers() {
		return arguments("rmospns");
	}
}
