package plugin.command.server_moderator;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.game.world.punishment.PunishmentHandler;
import com.rs.game.world.punishment.PunishmentType;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
@CommandManifest(description = "Removes an address mute for a player", types = { String.class })
public class RemoveAddressMuteCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		String name = getCompleted(args, 1);
		PunishmentHandler.removePunishment(player, name, PunishmentType.ADDRESS_MUTE);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("unipmute");
	}
}
