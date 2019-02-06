package plugin.command.owner;

import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/12/2017
 */
public class DebugCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			player.getBank().addItem(Misc.random(1, Integer.MAX_VALUE), 1, false);
		}
		System.out.println("Took " + (System.currentTimeMillis() - start));
	}
	
	@Override
	public String[] identifiers() {
		return arguments("dbg");
	}
}
