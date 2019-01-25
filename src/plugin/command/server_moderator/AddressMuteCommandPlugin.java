package plugin.command.server_moderator;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.CommandPlugin;
import org.redrune.game.global.punishment.PunishmentHandler;
import org.redrune.game.global.punishment.PunishmentType;
import org.redrune.utility.game.InputEvent;
import org.redrune.utility.game.InputEvent.InputEventType;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
@CommandManifest(description = "Mutes a player by their address", types = { String.class })
public class AddressMuteCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		String name = getCompleted(args, 1);
		player.getPackets().requestClientInput(new InputEvent("Enter Duration (HRS (0 = inf)):", InputEventType.INTEGER) {
			@Override
			public void handleInput() {
				PunishmentHandler.addPunishment(player, name, getInput(), PunishmentType.ADDRESS_MUTE);
			}
		});
	}
	
	@Override
	public String[] identifiers() {
		return arguments("ipmute");
	}
}
