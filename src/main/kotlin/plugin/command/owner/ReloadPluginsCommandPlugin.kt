package plugin.command.owner;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.content.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Reloads all plugins")
public class ReloadPluginsCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		PluginRepository.reload();
		player.getPackets().sendMessage("All plugins have been reloaded.");
	}
	
	@Override
	public String[] identifiers() {
		return arguments("reloadplugins", "rlp");
	}
}
