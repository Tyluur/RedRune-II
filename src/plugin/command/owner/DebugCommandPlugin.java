package plugin.command.owner;

import com.rs.game.content.actor.player.design.PlayerDesign;
import com.rs.game.content.PlayerLook;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/12/2017
 */
public class DebugCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int type = intParam(args, 1);
		switch (type) {
			case 0:
				PlayerDesign.open(player);
				break;
			case 1:
				PlayerLook.openMageMakeOver(player);
				break;
			case 2:
				PlayerLook.openHairdresserSalon(player);
				break;
			case 3:
				PlayerLook.openThessaliasMakeOver(player);
				break;
		}
	}
	
	@Override
	public String[] identifiers() {
		return arguments("dbg");
	}
}
