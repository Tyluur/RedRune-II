package plugin.command.owner;

import org.redrune.game.content.entity.actor.player.dialogue.DialogueHandler;
import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class ReloadDialoguesCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		DialogueHandler.reload();
		player.getPackets().sendGameMessage("All dialogues have been reloaded.");
	}
	
	@Override
	public String[] identifiers() {
		return arguments("rld");
	}
}
