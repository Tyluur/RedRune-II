package plugin.command.administrator;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Transforms you into an npc [-1 for human]", types = { Integer.class })
public class PNPCCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int npcId = intParam(args, 1);
		player.getAppearance().transformIntoNPC(npcId);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("pnpc");
	}
}
