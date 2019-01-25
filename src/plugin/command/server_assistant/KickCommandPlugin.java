package plugin.command.server_assistant;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.CommandPlugin;
import org.redrune.game.global.World;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Kicks a player from the server", types = { String.class })
public class KickCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		String name = getCompleted(args, 1);
		Player target = World.getPlayerByDisplayName(name);
		if (target == null) {
			return;
		}
		target.getSession().getChannel().close();
		World.removePlayer(target);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("kick");
	}
}
