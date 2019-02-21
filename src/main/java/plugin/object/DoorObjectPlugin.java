package plugin.object;

import org.redrune.game.content.plugin.type.ObjectPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.utility.game.repository.object.door.DoorFunctionality;
import org.redrune.utility.game.repository.object.door.DoorRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-31
 */
public class DoorObjectPlugin implements ObjectPlugin {
	
	@Override
	public boolean handle(Player player, WorldObject object, String option) {
		
		String name = object.getDefinitions().getName().toLowerCase();
		if (name.contains("trapdoor") || name.contains("trap door")) {
			player.getPackets().sendMessage("This doesn't seem to go anywhere.");
			return true;
		}
	/*	String name = object.getName().toLowerCase();
		if (name.contains("trapdoor") || name.contains("trap door")) {
			Location destination = object.getLocation().transform(0, 6400, 0);
			if (!RegionManager.isTeleportPermitted(destination)) {
				player.getPacketDispatch().sendMessage("This doesn't seem to go anywhere.");
				return true;
			}
			player.getProperties().setTeleportLocation(destination);
			return true;
		}*/
		// mithril door
		if (object.getId() == 25341) {
			return false;
		}
		DoorFunctionality.handleDoor(player, object);
		return true;
	}
	
	@Override
	public void register() {
		for (int objectId : DoorRepository.getDoorIds()) {
			registerObject(objectId, "Open");
			registerObject(objectId, "Close");
		}
	}
}
