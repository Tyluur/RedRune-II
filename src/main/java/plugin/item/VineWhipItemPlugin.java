package plugin.item;

import org.redrune.game.content.plugin.type.ItemOnItemPlugin;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class VineWhipItemPlugin implements ItemPlugin, ItemOnItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		if (!player.getAttributes().isCanPvp()) {
			if (player.getInventory().getFreeSlots() > 1) {
				player.getInventory().deleteItem(21371, 1);
				player.getInventory().addItem(4151, 1);
				player.getInventory().addItem(21369, 1);
				player.getPackets().sendMessage("You split the vine and whip apart.");
			} else {
				player.getPackets().sendMessage("You need two inventory spaces to do this.");
			}
		} else {
			player.getPackets().sendMessage("You can not do this in the wilderness.");
		}
		return true;
	}
	
	@Override
	public boolean handleItemOnItem(Player player, Item used, Item with) {
		if (!player.getInventory().containsItems(new int[] { 4151, 21369 }, new int[] { 1, 1 })) {
			return false;
		}
		player.getInventory().deleteItem(21369, 1);
		player.getInventory().deleteItem(4151, 1);
		player.getInventory().addItem(21371, 1);
		player.getPackets().sendMessage("You have successfully combined a vine and a whip");
		return true;
	}
	
	@Override
	public void register() {
		registerItem(21371, "Split");
		registerItemOnItemIds(21369, 4151);
	}
}
