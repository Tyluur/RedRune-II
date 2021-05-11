package plugin.command.administrator

import game.content.plugin.type.CommandPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.player.Player
import game.global.map.region.RegionManager
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-21
 */
@CommandManifest(description = "Lists all objects in your region")
class ListObjectsCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val objects: List<WorldObject> = RegionManager.getRegion(player.regionId).objects
        if (objects == null) {
            sendResponse(player, "Unable to get objects list", console)
            return
        }
        for (`object` in objects) {
            sendResponse(player, `object`.toString(), console)
        }
    }

    override fun identifiers(): Array<String> {
        return arguments("listobjs")
    }
}