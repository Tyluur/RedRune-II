package plugin.command.owner;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.game.world.region.RegionManager;
import com.rs.utility.game.object.ObjectSpawns;
import plugin.command.CommandManifest;

import java.io.IOException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Stores an object spawn at our current location", types = { Integer.class })
public class StoreObjectSpawnCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int id = intParam(args, 1);
		int type = intParamOrDefault(args, 2, 10);
		int rotation = intParamOrDefault(args, 3, 0);
		if (type > 22 || type < 0) {
			type = 10;
		}
		WorldObject object = new WorldObject(id, type, rotation, player.getX(), player.getY(), player.getPlane());
		RegionManager.spawnObject(object);
		
		try {
			ObjectSpawns.dumpObjectSpawn(object.getId(), type, rotation, player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		player.getPackets().sendGameMessage("Dumped Object: " + object + "", true);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("storeobj", "storeo");
	}
}
