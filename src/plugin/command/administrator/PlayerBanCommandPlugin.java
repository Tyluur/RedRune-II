package plugin.command.administrator;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.game.world.punishment.PunishmentHandler;
import com.rs.game.world.punishment.PunishmentType;
import com.rs.utility.game.InputEvent;
import com.rs.utility.game.InputEvent.InputEventType;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
@CommandManifest(description = "Bans a player", types = { String.class })
public class PlayerBanCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		String name = getCompleted(args, 1);
		player.getPackets().requestClientInput(new InputEvent("Enter Duration (HRS (0 = inf)):", InputEventType.INTEGER) {
			@Override
			public void handleInput() {
				PunishmentHandler.addPunishment(player, name, getInput(), PunishmentType.PLAYER_BAN);
			}
		});
	}
	
	@Override
	public String[] identifiers() {
		return arguments("ban");
	}
}
