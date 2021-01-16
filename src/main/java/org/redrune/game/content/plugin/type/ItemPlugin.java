package org.redrune.game.content.plugin.type;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.content.plugin.Plugin;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-07
 */
public interface ItemPlugin extends Plugin {
	
	/**
	 * Handles the interaction with the item
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The object
	 * @param slotId
	 * 		The slot the item is in
	 * @param option
	 * 		The option [string] clicked
	 */
	boolean handle(Player player, Item item, int slotId, String option);
	
	/**
	 * Registers the first option for a variable amount of item ids
	 */
	default void registerSpecifiedOptionVarags(ClickOption option, int... itemIds) {
		for (int objectId : itemIds) {
			registerSpecifiedOption(option, objectId);
		}
	}
	
	/**
	 * Registers the specified option for an item, this is used when we don't know the option name for an item
	 *
	 * @param option
	 * 		The option
	 * @param itemId
	 * 		The item id
	 */
	default void registerSpecifiedOption(ClickOption option, int itemId) {
		ItemDefinitions definitions = ItemDefinitions.getItemDefinitions(itemId);
		if (definitions == null) {
			throw new IllegalStateException();
		}
		int optionSlot;
		switch (option) {
			case FIRST:
				optionSlot = 1;
				break;
			case SECOND:
				optionSlot = 2;
				break;
			case THIRD:
				optionSlot = 3;
				break;
			case FOURTH:
				optionSlot = 4;
				break;
			default:
				throw new IllegalStateException();
		}
		String optionName = definitions.getInventoryOption(optionSlot);
		registerItem(itemId, optionName);
	}
	
	/**
	 * Registers this plugin into the repository
	 *
	 * @param itemId
	 * 		The id of the object
	 * @param option
	 * 		The option that will be used
	 */
	default void registerItem(int itemId, String option) {
		PluginRepository.registerOptionPlugin(this, itemId, option);
	}
	
}
