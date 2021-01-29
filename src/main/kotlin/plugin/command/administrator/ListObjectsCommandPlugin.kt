package plugin.command.administrator;

import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.map.region.RegionManager;
import plugin.command.CommandManifest;

import java.util.List;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-21
 */
@CommandManifest(description = "Lists all objects in your region")
public class ListObjectsCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		List<WorldObject> objects = RegionManager.getRegion(player.getRegionId()).getObjects();
		if (objects == null) {
			sendResponse(player, "Unable to get objects list", console);
			return;
		}
		for (WorldObject object : objects) {
			sendResponse(player, object.toString(), console);
		}
	}
	
	@Override
	public String[] identifiers() {
		return arguments("listobjs");
	}
}
