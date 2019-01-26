package org.redrune.game.content.plugin.type;

import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.Plugin;
import org.redrune.game.content.plugin.PluginRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public abstract class NPCPlugin extends Plugin {
	
	/**
	 * Registers this plugin into the repository
	 *
	 * @param npcId
	 * 		The id of the npc
	 * @param option
	 * 		The option that will be used
	 */
	public void register(int npcId, String option) {
		PluginRepository.registerOptionPlugin(this, npcId, option);
	}
	
	/**
	 * Handling the npc interaction
	 *
	 * @param player
	 * 		The player
	 * @param npc
	 * 		The npc
	 * @param option
	 * 		The option we clicked
	 */
	public abstract boolean handle(Player player, NPC npc, String option);
	
}
