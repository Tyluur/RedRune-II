package plugin.command.owner

import game.content.plugin.type.CommandPlugin
import game.entity.actor.player.Player
import utility.functions.Misc
import utility.game.repository.npc.spawn.NPCSpawnRepository
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Stores an npc spawn", types = [Int::class])
class StoreNPCSpawnCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val npcId = intParam(args, 1)
        val directionName = stringParamOrDefault(args, 2, "north").toUpperCase()
        val direction: Misc.FaceDirection = Misc.FaceDirection.valueOf(directionName)
        NPCSpawnRepository.addSpawn(npcId, player.worldTile, direction)
    }

    override fun identifiers(): Array<String> {
        return arguments("n")
    }
}