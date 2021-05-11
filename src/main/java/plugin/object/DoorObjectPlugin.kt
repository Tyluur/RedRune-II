package plugin.`object`

import game.content.entity.`object`.DoorFunctionality
import game.content.plugin.type.ObjectPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.player.Player
import utility.game.repository.`object`.door.DoorRepository

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-31
 */
class DoorObjectPlugin : ObjectPlugin {
    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        val name: String = `object`.definitions.name.toLowerCase()
        if (name.contains("trapdoor") || name.contains("trap door")) {
            player.packets.sendMessage("This doesn't seem to go anywhere.")
            return true
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
        if (`object`.id == 25341) {
            return false
        }
        DoorFunctionality.handleDoor(player, `object`)
        return true
    }

    override fun register() {
        for (objectId in DoorRepository.doorIds) {
            registerObject(objectId, "Open")
            registerObject(objectId, "Close")
        }
    }
}