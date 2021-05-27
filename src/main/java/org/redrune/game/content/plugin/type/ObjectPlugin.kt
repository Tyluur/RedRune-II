package org.redrune.game.content.plugin.type;

import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.game.content.plugin.Plugin;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
public interface ObjectPlugin extends Plugin {
	
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
	boolean handle(Player player, WorldObject object, String option);
	
	/**
	 * Registers the first option for a variable amount of object ids
	 */
	default void registerSpecifiedOptionVarags(ClickOption option, int... objectIds) {
		for (int objectId : objectIds) {
			registerSpecifiedOption(option, objectId);
		}
	}
	
	/**
	 * Registers the specified option for an object, this is used when we don't know the option name for an object
	 *
	 * @param option
	 * 		The option
	 * @param objectId
	 * 		The object id
	 */
	default void registerSpecifiedOption(ClickOption option, int objectId) {
		ObjectDefinitions definitions = ObjectDefinitions.getObjectDefinitions(objectId);
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
		String optionName = definitions.getOption(optionSlot);
		registerObject(objectId, optionName);
	}
	
	/**
	 * Registers this plugin into the repository
	 *
	 * @param objectId
	 * 		The id of the object
	 * @param option
	 * 		The option that will be used
	 */
	default void registerObject(int objectId, String option) {
		PluginRepository.registerOptionPlugin(this, objectId, option);
	}
	
}
