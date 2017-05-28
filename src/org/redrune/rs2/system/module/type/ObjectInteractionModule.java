package org.redrune.rs2.system.module.type;

import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.object.GameObject;
import org.redrune.rs2.system.module.interaction.InteractionModule;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public interface ObjectInteractionModule extends InteractionModule {
	
	/**
	 * The object ids that are subscribed to the module.
	 */
	int[] objectSubscriptionIds();
	
	/**
	 * Hanadles the interaction with the module
	 *
	 * @param player
	 * 		The player interacting
	 * @param object
	 * 		The object interacting with
	 * @param option
	 * 		The option clicked on the object
	 * @return {@code True} if the interaction was successful
	 */
	boolean handle(Player player, GameObject object, InteractionOption option);
	
}
