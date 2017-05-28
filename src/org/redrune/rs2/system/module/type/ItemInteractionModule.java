package org.redrune.rs2.system.module.type;

import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.system.module.interaction.InteractionModule;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public interface ItemInteractionModule extends InteractionModule {
	
	/**
	 * The item ids that subscribe to this module
	 */
	int[] itemSubscriptionIds();
	
	/**
	 * Handles the interaction with an item
	 *
	 * @param player
	 * 		The player
	 * @param itemId
	 * 		The id of the item we're interacting with
	 * @param option
	 * 		The option we clicked
	 * @return {@code True} if successfully interacted.
	 */
	boolean handle(Player player, int itemId, InteractionOption option);
}
