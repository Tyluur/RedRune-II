package plugin.command.owner;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.game.world.region.RegionManager;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Spawns an object on your position", types = { Integer.class })
public class SpawnObjectCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int id = intParam(args, 1);
		int type = intParamOrDefault(args, 2, 10);
		int rotation = intParamOrDefault(args, 3, 0);
		WorldObject object = new WorldObject(id, type, rotation, player);
		
		RegionManager.spawnObject(object);
		
		player.getPackets().sendGameMessage("Spawned Object: " + object);
		System.out.println("Spawned Object: " + object);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("obj", "spawnobject");
	}
}
