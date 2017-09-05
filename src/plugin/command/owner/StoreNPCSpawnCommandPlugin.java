package plugin.command.owner;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.utility.Misc.Direction;
import com.rs.utility.repo.npc.spawn.NPCSpawnRepository;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Stores an npc spawn", types = { Integer.class })
public class StoreNPCSpawnCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int npcId = intParam(args, 1);
		String directionName = stringParamOrDefault(args, 2, "north").toUpperCase();
		Direction direction = Direction.valueOf(directionName);
		NPCSpawnRepository.addSpawn(npcId, player.getWorldTile(), direction);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("n");
	}
}
