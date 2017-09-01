package com.rs.game.plugin.type;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.plugin.Plugin;
import com.rs.game.plugin.PluginRepository;
import com.rs.utility.game.ClickOption;

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
	 * @param options
	 * 		The options that will be used
	 */
	public void register(int objectId, ClickOption... options) {
		PluginRepository.registerOptionablePlugin(this, objectId, options);
	}
	
	/**
	 * Handles the interaction with the object
	 *
	 * @param player
	 * 		The player
	 * @param object
	 * 		The object
	 * @param option
	 * 		The option
	 */
	public abstract void handle(Player player, WorldObject object, ClickOption option);
	
}
