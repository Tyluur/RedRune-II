package org.redrune.game.plugin.type;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.plugin.Plugin;
import org.redrune.game.plugin.PluginRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
public abstract class ObjectPlugin extends Plugin {
	
	/**
	 * Registers this plugin into the repository
	 *
	 * @param objectId
	 * 		The id of the object
	 * @param option
	 * 		The option that will be used
	 */
	public void register(int objectId, String option) {
		PluginRepository.registerOptionPlugin(this, objectId, option);
	}
	
	/**
	 * Handles the interaction with the object
	 *
	 * @param player
	 * 		The player
	 * @param object
	 * 		The object
	 * @param option
	 * 		The option clicked
	 */
	public abstract boolean handle(Player player, WorldObject object, String option);
	
}
