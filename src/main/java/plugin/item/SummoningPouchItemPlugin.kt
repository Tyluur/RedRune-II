package plugin.item

import game.content.entity.actor.player.skills.summoning.Summoning
import game.content.plugin.type.ItemPlugin
import game.entity.actor.player.Player
import game.entity.item.Item
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class SummoningPouchItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        val pouch: Summoning.Pouches = Summoning.Pouches.forId(item.id) ?: return false
        Summoning.spawnFamiliar(player, pouch)
        return true
    }

    override fun register() {
        Arrays.stream(Summoning.Pouches.values())
            .forEach { pouch: Summoning.Pouches ->
                registerItem(
                    pouch.pouchId,
                    "Summon"
                )
            }
    }
}