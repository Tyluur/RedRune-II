package plugin.command.administrator;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/8/2017
 */
@CommandManifest(description = "Sets your prayer book [1/2]", types = { Integer.class })
public class SetPrayerBookCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int bookId = intParam(args, 1);
		player.getPrayer().setPrayerBook(bookId == 2);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("setprayerbook");
	}
}
