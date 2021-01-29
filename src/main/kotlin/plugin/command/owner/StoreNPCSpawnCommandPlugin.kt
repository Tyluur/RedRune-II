package plugin.command.owner;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.utility.functions.Misc.FaceDirection;
import org.redrune.utility.game.repository.npc.spawn.NPCSpawnRepository;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Stores an npc spawn", types = { Integer.class })
public class StoreNPCSpawnCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int npcId = intParam(args, 1);
		String directionName = stringParamOrDefault(args, 2, "north").toUpperCase();
		FaceDirection direction = FaceDirection.valueOf(directionName);
		NPCSpawnRepository.addSpawn(npcId, player.getWorldTile(), direction);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("n");
	}
}
