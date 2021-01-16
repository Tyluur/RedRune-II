package org.redrune.game.content.plugin.type;

import org.redrune.game.content.plugin.Plugin;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.object.WorldObject;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
public interface ItemOnObjectPlugin extends Plugin {
	
	/**
	 * Handles the interaction of the plugin
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item
	 * @param object
	 * 		The object
	 */
	boolean handle(Player player, Item item, WorldObject object);
	
	/**
	 * Registers this plugin to the repository
	 *
	 * @param key
	 * 		The id of the item
	 * @param objectId
	 * 		The object id that is relevant
	 */
	default void registerItemOnObjectPlugins(int key, int objectId) {
		PluginRepository.registerEntityOnPlugin(this, key, objectId);
	}
}
