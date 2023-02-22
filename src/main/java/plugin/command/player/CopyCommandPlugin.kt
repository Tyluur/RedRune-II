package plugin.command.player

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Copies another player", types = [String::class])
class CopyCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val username = getCompleted(args, 1)
        val target = World.getPlayerByDisplayName(username)

        if (target == null) {
            player.packets.sendMessage("Couldn't find player $username.")
            return
        }

        System.arraycopy(
            target.inventory.items.toArray(),
            0,
            player.inventory.items.toArray(),
            0,
            player.inventory.items.toArray().size
        )

        System.arraycopy(
            target.equipment.items.toArray(),
            0,
            player.equipment.items.toArray(),
            0,
            player.equipment.items.toArray().size
        )
        player.inventory.refresh()
        player.equipment.refreshAll()
        player.skills.passLevels(target)

        player.skills.refreshAllSkills()
        player.appearance.generateAppearanceData()
    }

    override fun identifiers(): Array<String> {
        return arguments("copy")
    }
}