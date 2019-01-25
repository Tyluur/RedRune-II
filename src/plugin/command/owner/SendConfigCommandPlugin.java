package plugin.command.owner;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
@CommandManifest(types = { Integer.class, Integer.class})
public class SendConfigCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int configId = intParam(args, 1);
		int configValue = intParam(args, 2);
		player.getPackets().sendConfig(configId, configValue);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("sendconfig");
	}
}
