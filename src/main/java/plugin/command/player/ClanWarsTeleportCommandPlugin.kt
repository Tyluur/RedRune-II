package plugin.command.player

import org.redrune.game.content.entity.actor.combat.function.Magic
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import plugin.command.CommandManifest

@CommandManifest(description = "Teleports you to the clan wars area in the wilderness.")
class ClanWarsTeleportCommandPlugin : CommandPlugin() {

    override fun handle(
        player: Player,
        args: Array<out String>,
        console: Boolean,
        clientCommand: Boolean
    ) {
        Magic.sendNormalTeleportSpell(player, 0, 0.0, WorldTile(3274, 3682, 0))
    }

    override fun identifiers() = arrayOf("cw")
}