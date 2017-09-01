package com.rs.game.plugin.type;

import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.Plugin;
import com.rs.game.plugin.PluginRepository;
import com.rs.utility.game.ClickOption;

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
	 * @param options
	 * 		The options that will be used
	 */
	public void register(int npcId, ClickOption... options) {
		PluginRepository.registerOptionablePlugin(this, npcId, options);
	}
	
	/**
	 * Handling the npc interaction
	 *
	 * @param player
	 * 		The player
	 * @param npc
	 * 		The npc
	 * @param option
	 * 		The option clicked on the npc
	 */
	public abstract void handle(Player player, NPC npc, ClickOption option);
	
}
