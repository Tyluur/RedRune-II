package plugin.command.owner

import org.redrune.game.content.plugin.type.CommandPlugin
import plugin.command.CommandManifest
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Stores an object spawn at our current location", types = [Int::class])
class StoreObjectSpawnCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
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
        player.packets.sendMessage("Todo")
    }

    override fun identifiers(): Array<String> {
        return arguments("storeobj", "storeo")
    }
}