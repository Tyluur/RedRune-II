package plugin.item

import org.redrune.game.content.entity.item.Foods
import org.redrune.game.content.entity.item.Foods.Food
import org.redrune.game.content.plugin.type.ItemPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-07
 */
class FoodConsumptionItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        Foods.eat(player, item, slotId)
        return true
    }

    override fun register() {
        Arrays.stream(Food.values()).forEach { food: Food -> registerItem(food.id, "Eat") }
    }
}