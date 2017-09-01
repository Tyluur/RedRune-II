package plugin.command.owner;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.utility.Misc;
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
