package plugin.item;

import org.redrune.game.content.entity.actor.player.skills.runecrafting.Runecrafting;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
public class TalismanItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		int itemId = item.getId();
		if (itemId == 1438) {
			Runecrafting.locate(player, 3127, 3405);
		} else if (itemId == 1440) {
			Runecrafting.locate(player, 3306, 3474);
		} else if (itemId == 1442) {
			Runecrafting.locate(player, 3313, 3255);
		} else if (itemId == 1444) {
			Runecrafting.locate(player, 3185, 3165);
		} else if (itemId == 1446) {
			Runecrafting.locate(player, 3053, 3445);
		} else if (itemId == 1448) {
			Runecrafting.locate(player, 2982, 3514);
		}
		return true;
	}
	
	@Override
	public void register() {
		int[] ids = { 1438, 1440, 1442, 1444, 1446, 1448} ;
		for (int id : ids) {
			registerItem(id, "Locate");
		}
	}
}
