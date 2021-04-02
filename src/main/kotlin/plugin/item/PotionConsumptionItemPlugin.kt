package plugin.item

import org.redrune.game.content.entity.item.Pots
import org.redrune.game.content.plugin.type.ItemPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import java.util.*
import java.util.function.Consumer
import java.util.function.IntConsumer

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class PotionConsumptionItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        Pots.pot(player, item, slotId)
        return true
    }

    override fun register() {
        Arrays.stream(Pots.Pot.values()).forEach(Consumer<Pots.Pot> { pot: Pots.Pot ->
            Arrays.stream(pot.ids).forEach(
                IntConsumer { id: Int -> registerItem(id, "Drink") })
        })
    }
}