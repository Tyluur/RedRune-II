package plugin.command.owner;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public class RemoveNPCSpawnCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
			player.putAttribute("removing_npcs", !player.getAttribute("removing_npcs", false));
			player.getPackets().sendGameMessage("You are now " + (player.getAttribute("removing_npcs", false) ? "removing" : "examining") + " npcs.");
	}
	
	@Override
	public String[] identifiers() {
		return arguments("rspns");
	}
}
