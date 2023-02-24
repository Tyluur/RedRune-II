package plugin.item.onitem

import org.redrune.game.content.entity.actor.player.skills.fletching.Fletching
import org.redrune.game.content.entity.actor.player.skills.fletching.Fletching.Fletch
import org.redrune.game.content.plugin.type.ItemOnItemPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class FletchingItemOnItemPlugin : ItemOnItemPlugin {
    override fun handleItemOnItem(player: Player, used: Item, with: Item): Boolean {
        val fletch = Fletching.isFletching(used, with) ?: return true
        player.dialogueManager.startDialogue("FletchingD", fletch)
        return true
    }

    override fun register() {
        Arrays.stream(Fletch.values()).forEach { fletch: Fletch -> registerItemOnItemIds(fletch.id, fletch.selected) }
    }
}