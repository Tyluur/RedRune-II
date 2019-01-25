package plugin.command.administrator;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.CommandPlugin;
import org.redrune.game.global.punishment.PunishmentHandler;
import org.redrune.game.global.punishment.PunishmentType;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
@CommandManifest(description = "Removes an address ban for a player", types = { String.class })
public class RemovePlayerAddressBanCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		String name = getCompleted(args, 1);
		PunishmentHandler.removePunishment(player, name, PunishmentType.ADDRESS_BAN);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("unipban");
	}
}
