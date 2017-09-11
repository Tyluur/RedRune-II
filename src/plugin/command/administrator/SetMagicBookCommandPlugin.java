package plugin.command.administrator;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
@CommandManifest(description = "Sets your magic book [1-3]", types = { Integer.class })
public class SetMagicBookCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int book = intParam(args, 1);
		if (book < 1 || book > 3) {
			player.getPackets().sendGameMessage("You can only enter a book id between [1-3]");
			return;
		}
		int bookId = book - 1;
		player.getCombatDefinitions().setSpellBook(bookId);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("setmagicbook");
	}
}
