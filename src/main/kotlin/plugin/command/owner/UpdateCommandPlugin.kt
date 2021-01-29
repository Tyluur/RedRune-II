package plugin.command.owner;

import org.redrune.engine.SystemManager;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Updates the server after x seconds", types = { Integer.class })
public class UpdateCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int delay = intParam(args, 1);
		SystemManager.safeShutdown(delay);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("update", "shutdown");
	}
}
