package plugin.command.player

import org.redrune.game.content.entity.actor.player.market.ShopRepository
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import plugin.command.CommandManifest

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Opens a shop by its id", types = [Int::class])
class OpenShopCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val identifier = intParam(args, 1)
        ShopRepository.open(player, identifier)
    }

    override fun identifiers(): Array<String> {
        return arguments("openshop", "shop")
    }
}