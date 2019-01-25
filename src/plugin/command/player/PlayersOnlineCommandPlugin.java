package plugin.command.player;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.CommandPlugin;
import org.redrune.game.global.World;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Shows the players online")
public class PlayersOnlineCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		sendResponse(player, "There are currently " + World.getPlayers().size() + " players online.", console);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("players");
	}
}
