package game.content.plugin.type;

import game.content.plugin.Plugin;
import game.content.plugin.PluginRepository;
import game.entity.actor.player.Player;
import game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
public interface ItemOnItemPlugin extends Plugin {
	
	/**
	 * Handles the interaction of an item on an item
	 *
	 * @param player
	 * 		The player
	 * @param used
	 * 		The item used
	 * @param with
	 * 		The item used with
	 */
	boolean handleItemOnItem(Player player, Item used, Item with);
	
	/**
	 * The registration of the item on item is done here, supporting backwards compatibility
	 *
	 * @param itemId
	 * 		The item id used
	 * @param withIds
	 * 		The item ids of the items used
	 */
	default void registerItemOnItemIds(int itemId, int... withIds) {
		PluginRepository.registerEntityOnPlugin(this, itemId, withIds);
		for (int with : withIds) {
			PluginRepository.registerEntityOnPlugin(this, with, itemId);
		}
	}
	
}
