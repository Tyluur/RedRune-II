package plugin.command.owner;

import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.game.repository.object.door.DoorRepository;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-20
 */
@CommandManifest(description = "Reloads all doors from file")
public class ReloadDoorsCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		DoorRepository.initialize();
		player.getPackets().sendMessage("Reloaded all game doors");
	}
	
	@Override
	public String[] identifiers() {
		return arguments("rldrs", "rldoors");
	}
}
