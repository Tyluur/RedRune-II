package game.content.plugin.type;

import game.content.plugin.Plugin;
import game.content.plugin.PluginRepository;
import game.entity.actor.player.Player;
import game.entity.item.Item;
import game.entity.object.WorldObject;

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
