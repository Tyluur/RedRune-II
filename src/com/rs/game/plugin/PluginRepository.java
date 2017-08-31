package com.rs.game.plugin;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.inter.InterfacePlugin;
import com.rs.utility.Misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/30/2017
 */
public class PluginRepository {
	
	/**
	 * The map of interface plugins
	 */
	private static final Map<Integer, List<InterfacePlugin>> INTERFACE_PLUGINS = new HashMap<>();
	
	/**
	 * Regusters a plugin
	 *
	 * @param plugin
	 * 		The plugin
	 * @param keys
	 * 		The keys
	 */
	public static void register(Plugin plugin, int... keys) {
		if (plugin instanceof InterfacePlugin) {
			for (int key : keys) {
				List<InterfacePlugin> pluginList = INTERFACE_PLUGINS.get(key);
				if (pluginList == null) {
					pluginList = new ArrayList<>();
				}
				pluginList.add((InterfacePlugin) plugin);
				INTERFACE_PLUGINS.put(key, pluginList);
			}
		}
	}
	
	/**
	 * Registers all the plugins
	 */
	public static void registerAll() {
		Misc.getClasses("plugin").stream().filter(Plugin.class::isInstance).forEach(clazz -> ((Plugin) clazz).register());
		System.out.println("Registered " + INTERFACE_PLUGINS.size() + " interface plugins,");
	}
	
	/**
	 * Handles the interface interaction with the best plugin
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
	public static boolean handleInterface(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		List<InterfacePlugin> interfacePlugins = INTERFACE_PLUGINS.get(interfaceId);
		if (interfacePlugins == null) {
			return false;
		}
		for (InterfacePlugin plugin : interfacePlugins) {
			if (plugin.handle(player, interfaceId, componentId, itemId, slotId, packetId)) {
				return true;
			}
		}
		return false;
	}
	
}
