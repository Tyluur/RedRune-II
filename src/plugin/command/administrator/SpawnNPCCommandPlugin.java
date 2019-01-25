package plugin.command.administrator;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.CommandPlugin;
import org.redrune.game.global.World;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Spawns an npc on your tile", types = { Integer.class })
public class SpawnNPCCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		World.spawnNPC(intParam(args, 1), player, -1, false, true);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("npc");
	}
}
