package plugin.command.player;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.game.world.World;
import com.rs.utility.Misc;
import com.rs.utility.constants.ColorConstants;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Yells a message to everyone online", types = { String.class })
public class YellCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		String message = getCompleted(args, 1);
		if (message == null || message.equalsIgnoreCase("null")) {
			return;
		}
		sendYellMessage(player, message);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("yell");
	}
	
	/**
	 * Sends a yell message
	 *
	 * @param player
	 * 		The player yelling
	 * @param message
	 * 		The message
	 */
	public static void sendYellMessage(Player player, String message) {
		message = Misc.fixChatMessage(message.replaceAll("<", "")).trim();
		
		StringBuilder tag = new StringBuilder();
		
		tag.append("[<col=" + ColorConstants.BLUE + ">RR</col>] ");
		tag.append(player.getDisplayName()).append(": ").append(message);
		for (Player pl : World.getPlayers()) {
			if (pl == null) {
				continue;
			}
			pl.getPackets().sendGameMessage(tag.toString());
		}
	}
}
