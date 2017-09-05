package plugin.command.owner;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public class RemoveObjectSpawnCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		player.putAttribute("removing_objects", !player.getAttribute("removing_objects", false));
		player.getPackets().sendGameMessage("You are now " + (player.getAttribute("removing_objects", false) ? "removing" : "examining") + " objects.");
	}
	
	@Override
	public String[] identifiers() {
		return arguments("rmospns");
	}
}
