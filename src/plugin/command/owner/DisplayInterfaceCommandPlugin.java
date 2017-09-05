package plugin.command.owner;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Displays an interface by its id", types = { Integer.class })
public class DisplayInterfaceCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int interfaceId = intParam(args, 1);
		player.getInterfaceManager().sendInterface(interfaceId);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("inter", "interface");
	}
}
