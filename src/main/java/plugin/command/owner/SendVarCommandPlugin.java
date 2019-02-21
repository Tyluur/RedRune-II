package plugin.command.owner;

import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-29
 */
public class SendVarCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int id = intParamOrDefault(args, 1, 0);
		int value = intParamOrDefault(args, 2, 0);
		player.getVarManager().sendVar(id, value);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("sendvar");
	}
}
