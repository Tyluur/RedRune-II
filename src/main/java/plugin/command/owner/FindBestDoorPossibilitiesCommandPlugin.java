package plugin.command.owner;

import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-20
 */
@CommandManifest(description = "Toggles door finding ")
public class FindBestDoorPossibilitiesCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		player.putTemporaryAttribute("door_finding", !player.getTemporaryAttribute("door_finding", false));
		player.getPackets().sendMessage("You are now " + (player.getTemporaryAttribute("door_finding", false) ? "finding" : "not finding") + " possible doors.");
	}
	
	@Override
	public String[] identifiers() {
		return arguments("toggledoorfind");
	}
}
