package plugin.command.owner;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/12/2017
 */
public class DebugCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		player.getInterfaceManager().sendInterface(1139);
		player.getPackets().sendVarp(261, intParamOrDefault(args, 1, 0));
	}
	
	@Override
	public String[] identifiers() {
		return arguments("dbg");
	}
}
