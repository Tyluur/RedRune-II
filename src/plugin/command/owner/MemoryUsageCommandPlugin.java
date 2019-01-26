package plugin.command.owner;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.utility.functions.Misc;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Shows how much memory is used.")
public class MemoryUsageCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		String info = Misc.getMemoryUsageInformation();
		System.out.println(info);
		player.getPackets().sendGameMessage(info);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("memused");
	}
}
