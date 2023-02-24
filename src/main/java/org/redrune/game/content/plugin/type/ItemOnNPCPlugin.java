package org.redrune.game.content.plugin.type;

import org.redrune.game.content.plugin.Plugin;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
public interface ItemOnNPCPlugin extends Plugin {

    /**
     * Handles the interaction of the plugin
     *
     * @param player The player
     * @param item   The item
     * @param npc    The npc
     */
    boolean handle(Player player, Item item, NPC npc);

    /**
     * Registers this plugin to the repository
     *
     * @param key   The id of the item
     * @param npcId The npc ids that are relevant
     */
    default void registerItemOnNPCPlugin(int key, int npcId) {
        PluginRepository.registerEntityOnPlugin(this, key, npcId);
    }
}
