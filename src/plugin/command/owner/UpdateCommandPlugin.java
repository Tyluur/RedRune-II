package plugin.command.owner;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.game.world.World;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Updates the server after x seconds", types = { Integer.class})
public class UpdateCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int delay = intParam(args, 1);
		World.safeShutdown(delay);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("update", "shutdown");
	}
}
