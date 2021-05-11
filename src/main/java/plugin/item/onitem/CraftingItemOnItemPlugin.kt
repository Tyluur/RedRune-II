package plugin.item.onitem

import game.content.entity.actor.player.skills.crafting.GemCutting
import game.content.entity.actor.player.skills.crafting.GemCutting.Gem
import game.content.entity.item.InventoryOptionsHandler
import game.content.plugin.type.ItemOnItemPlugin
import game.entity.actor.player.Player
import game.entity.item.Item
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class CraftingItemOnItemPlugin : ItemOnItemPlugin {
    override fun handleItemOnItem(player: Player, used: Item, with: Item): Boolean {
        if (InventoryOptionsHandler.contains(1755, Gem.OPAL.uncut, used, with)) {
            GemCutting.cut(player, Gem.OPAL)
        } else if (InventoryOptionsHandler.contains(1755, Gem.JADE.uncut, used, with)) {
            GemCutting.cut(player, Gem.JADE)
        } else if (InventoryOptionsHandler.contains(1755, Gem.RED_TOPAZ.uncut, used, with)) {
            GemCutting.cut(player, Gem.RED_TOPAZ)
        } else if (InventoryOptionsHandler.contains(1755, Gem.SAPPHIRE.uncut, used, with)) {
            GemCutting.cut(player, Gem.SAPPHIRE)
        } else if (InventoryOptionsHandler.contains(1755, Gem.EMERALD.uncut, used, with)) {
            GemCutting.cut(player, Gem.EMERALD)
        } else if (InventoryOptionsHandler.contains(1755, Gem.RUBY.uncut, used, with)) {
            GemCutting.cut(player, Gem.RUBY)
        } else if (InventoryOptionsHandler.contains(1755, Gem.DIAMOND.uncut, used, with)) {
            GemCutting.cut(player, Gem.DIAMOND)
        } else if (InventoryOptionsHandler.contains(1755, Gem.DRAGONSTONE.uncut, used, with)) {
            GemCutting.cut(player, Gem.DRAGONSTONE)
        } else if (InventoryOptionsHandler.contains(1755, Gem.ONYX.uncut, used, with)) {
            GemCutting.cut(player, Gem.ONYX)
        }
        return true
    }

    override fun register() {
        Arrays.stream(Gem.values()).forEach { gem: Gem -> registerItemOnItemIds(gem.uncut, 1755) }
    }
}