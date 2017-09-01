package plugin.command.owner;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.PluginRepository;
import com.rs.game.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Reloads all plugins")
public class ReloadPluginsCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		PluginRepository.reload();
	}
	
	@Override
	public String[] identifiers() {
		return arguments("reloadplugins", "rlp");
	}
}
