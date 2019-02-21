package plugin.item;

import org.redrune.game.content.entity.item.Pots;
import org.redrune.game.content.entity.item.Pots.Pot;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class PotionConsumptionItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		Pots.pot(player, item, slotId);
		return true;
	}
	
	@Override
	public void register() {
		Arrays.stream(Pot.values()).forEach(pot -> Arrays.stream(pot.getIds()).forEach(id -> registerItem(id, "Drink")));
	}
}
