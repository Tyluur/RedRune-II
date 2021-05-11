package game.content.plugin.type;

import game.content.plugin.Plugin;
import game.content.plugin.PluginRepository;
import game.entity.actor.npc.NPC;
import game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
public interface NPCPlugin extends Plugin {
	
	/**
	 * Registers this plugin into the repository
	 *
	 * @param npcId
	 * 		The id of the npc
	 * @param option
	 * 		The option that will be used
	 */
	default void registerNPC(int npcId, String option) {
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
	boolean handle(Player player, NPC npc, String option);
	
}
