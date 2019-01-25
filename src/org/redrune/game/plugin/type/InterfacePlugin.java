package org.redrune.game.plugin.type;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.Plugin;
import org.redrune.game.plugin.PluginRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/30/2017
 */
public abstract class InterfacePlugin extends Plugin {
	
	/**
	 * Handles the interface interaction
	 *
	 * @param player
	 * 		The player clicking the interface
	 * @param interfaceId
	 * 		The id of the interface
	 * @param componentId
	 * 		The component id of the interface
	 * @param itemId
	 * 		The item id on the interface, -1 if none.
	 * @param slotId
	 * 		The slot id on the interface, -1 if none.
	 * @param packetId
	 * 		The packet id of the click, different ids are used for different options
	 * @return {@code True} if it was handled successfully
	 */
	public abstract boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId);
	
	/**
	 * Handles the registration of an interface plugin
	 *
	 * @param interfaceIds
	 * 		The id of the interfaces that will be registered
	 */
	protected void registerInterfacePlugin(int... interfaceIds) {
		PluginRepository.register(this, interfaceIds);
	}
}
