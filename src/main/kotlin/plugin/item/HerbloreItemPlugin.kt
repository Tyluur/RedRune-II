package plugin.item

import org.redrune.game.content.entity.actor.player.skills.herblore.HerbCleaning
import org.redrune.game.content.plugin.type.ItemPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import java.util.*
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class HerbloreItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        HerbCleaning.clean(player, item, slotId)
        return true
    }

    override fun register() {
        Arrays.stream(HerbCleaning.Herbs.values())
            .forEach(Consumer<HerbCleaning.Herbs> { herb: HerbCleaning.Herbs ->
                registerItem(
                    herb.herbId,
                    "Clean"
                )
            })
    }
}