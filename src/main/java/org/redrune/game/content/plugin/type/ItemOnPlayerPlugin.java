package org.redrune.game.content.plugin.type;

import org.redrune.game.content.plugin.Plugin;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
public interface ItemOnPlayerPlugin extends Plugin {
	
	/**
	 * Handles the interaction of the plugin
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item
	 * @param partner
	 * 		The the partner player
	 */
	boolean handle(Player player, Item item, Player partner);
	
	/**
	 * Registers this plugin to the repository
	 *
	 * @param key
	 * 		The id of the item
	 */
	default void registerItemOnPlayerPlugin(int key) {
		PluginRepository.registerEntityOnPlugin(this, key, -1);
	}
}
