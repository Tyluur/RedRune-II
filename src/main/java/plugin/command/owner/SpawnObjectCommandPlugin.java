package plugin.command.owner;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.global.map.region.RegionManager;
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
		
		player.getPackets().sendMessage("Spawned Object: " + object);
		System.out.println("Spawned Object: " + object);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("obj", "spawnobject");
	}
}
