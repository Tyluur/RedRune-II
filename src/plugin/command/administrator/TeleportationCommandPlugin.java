package plugin.command.administrator;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Teleports you to coordinates")
public class TeleportationCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		if (clientCommand) {
			args = args[1].split(",");
			int plane = Integer.valueOf(args[0]);
			int x = Integer.valueOf(args[1]) << 6 | Integer.valueOf(args[3]);
			int y = Integer.valueOf(args[2]) << 6 | Integer.valueOf(args[4]);
			player.setNextWorldTile(new WorldTile(x, y, plane));
		} else {
			player.resetWalkSteps();
			int x = intParamOrDefault(args, 1, player.getX());
			int y = intParamOrDefault(args, 2, player.getY());
			int plane = intParamOrDefault(args, 3, player.getPlane());
			
			player.setNextWorldTile(new WorldTile(x, y, plane));
		}
	}
	
	@Override
	public String[] identifiers() {
		return arguments("tele");
	}
}
