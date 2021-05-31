package plugin.command.player

import org.redrune.game.content.entity.actor.combat.function.Magic
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import plugin.command.CommandManifest

@CommandManifest(description = "Teleports you to the donator zone.")
class DonatorZoneCommandPlugin : CommandPlugin() {

    override fun handle(
        player: Player,
        args: Array<out String>,
        console: Boolean,
        clientCommand: Boolean
    ) {
        Magic.sendNormalTeleportSpell(player, 0, 0.0, WorldTile(2758, 3505, 0))
    }

    override fun identifiers() = arrayOf("dz")
}