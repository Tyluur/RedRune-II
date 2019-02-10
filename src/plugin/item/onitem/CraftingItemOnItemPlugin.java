package plugin.item.onitem;

import org.redrune.game.content.entity.actor.player.skills.crafting.GemCutting;
import org.redrune.game.content.entity.actor.player.skills.crafting.GemCutting.Gem;
import org.redrune.game.content.plugin.type.ItemOnItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.util.Arrays;

import static org.redrune.game.content.entity.item.InventoryOptionsHandler.contains;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class CraftingItemOnItemPlugin implements ItemOnItemPlugin {
	
	@Override
	public boolean handleItemOnItem(Player player, Item used, Item with) {
		if (contains(1755, Gem.OPAL.getUncut(), used, with)) {
			GemCutting.cut(player, Gem.OPAL);
		} else if (contains(1755, Gem.JADE.getUncut(), used, with)) {
			GemCutting.cut(player, Gem.JADE);
		} else if (contains(1755, Gem.RED_TOPAZ.getUncut(), used, with)) {
			GemCutting.cut(player, Gem.RED_TOPAZ);
		} else if (contains(1755, Gem.SAPPHIRE.getUncut(), used, with)) {
			GemCutting.cut(player, Gem.SAPPHIRE);
		} else if (contains(1755, Gem.EMERALD.getUncut(), used, with)) {
			GemCutting.cut(player, Gem.EMERALD);
		} else if (contains(1755, Gem.RUBY.getUncut(), used, with)) {
			GemCutting.cut(player, Gem.RUBY);
		} else if (contains(1755, Gem.DIAMOND.getUncut(), used, with)) {
			GemCutting.cut(player, Gem.DIAMOND);
		} else if (contains(1755, Gem.DRAGONSTONE.getUncut(), used, with)) {
			GemCutting.cut(player, Gem.DRAGONSTONE);
		} else if (contains(1755, Gem.ONYX.getUncut(), used, with)) {
			GemCutting.cut(player, Gem.ONYX);
		}
		return true;
	}
	
	@Override
	public void register() {
		Arrays.stream(Gem.values()).forEach(gem -> registerItemOnItemIds(gem.getUncut(), 1755));
	}
}
