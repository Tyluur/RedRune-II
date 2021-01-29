package plugin.command.owner;

import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Stores an object spawn at our current location", types = { Integer.class })
public class StoreObjectSpawnCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
/*		int id = intParam(args, 1);
		int type = intParamOrDefault(args, 2, 10);
		int rotation = intParamOrDefault(args, 3, 0);
		if (type > 22 || type < 0) {
			type = 10;
		}
		WorldObject object = new WorldObject(id, type, rotation, player.getX(), player.getY(), player.getPlane());
		RegionManager.spawnObject(object);
		
		try {
			ObjectSpawns.dumpObjectSpawn(object.getIds(), type, rotation, player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		player.getPackets().sendGameMessage("Dumped Object: " + object + "", true);*/
		player.getPackets().sendMessage("Todo");
	}
	
	@Override
	public String[] identifiers() {
		return arguments("storeobj", "storeo");
	}
}
