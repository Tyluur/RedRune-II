package plugin.command.server_moderator;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Tells you your position")
public class MyPositionCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		String loc = player.getWorldTile().toString();
		player.getPackets().sendMessage(loc);
		System.out.println(loc);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("pos", "mypos");
	}
}
