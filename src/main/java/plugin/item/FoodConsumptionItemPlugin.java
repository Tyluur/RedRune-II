package plugin.item;

import org.redrune.game.content.entity.item.Foods;
import org.redrune.game.content.entity.item.Foods.Food;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-07
 */
public class FoodConsumptionItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		Foods.eat(player, item, slotId);
		return true;
	}
	
	@Override
	public void register() {
		Arrays.stream(Food.values()).forEach(food -> registerItem(food.getId(), "Eat"));
	}
}
