package plugin.item;

import org.redrune.game.content.entity.actor.player.skills.runecrafting.Runecrafting;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
public class RunecraftingPouchItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		int itemId = item.getId();
		int pouch = -1;
		if (itemId == 5509) {
			pouch = 0;
		} else if (itemId == 5510) {
			pouch = 1;
		} else if (itemId == 5512) {
			pouch = 2;
		} else if (itemId == 5514) {
			pouch = 3;
		}
		if (pouch == -1) {
			return false;
		}
		switch (option) {
			case "Fill":
				Runecrafting.fillPouch(player, pouch);
				break;
			case "Empty":
				Runecrafting.emptyPouch(player, pouch);
				break;
			case "Check":
				Runecrafting.checkPouch(player, pouch);
				break;
		}
		return true;
	}
	
	@Override
	public void register() {
		for (int itemId = 5509; itemId <= 5514; itemId++) {
			registerItem(itemId, "Fill");
			registerItem(itemId, "Empty");
			registerItem(itemId, "Check");
		}
	}
}
