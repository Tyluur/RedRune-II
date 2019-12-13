package plugin.command.owner;

import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/12/2017
 */
public class DebugCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int componentId = intParam(args, 1);
		player.getPackets().sendIComponentText(1149, componentId, "" + componentId);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("dbg");
	}
}
