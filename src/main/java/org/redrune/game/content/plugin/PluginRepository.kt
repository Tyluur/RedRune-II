package org.redrune.game.content.plugin;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.game.content.plugin.combat.spell.SpellPlugin;
import org.redrune.game.content.plugin.type.*;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.utility.ReflectionUtils;
import org.redrune.utility.constants.MagicConstants.MagicBook;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.ClickOption;
import plugin.command.CommandManifest;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/30/2017
 */
public class PluginRepository {
	
	/**
	 * The map of interface plugins
	 */
	private static final Map<Integer, List<InterfacePlugin>> INTERFACE_PLUGINS = new HashMap<>();
	
	/**
	 * The map of npc plugins
	 */
	private static final Map<Integer, Map<String, NPCPlugin>> NPC_PLUGINS = new HashMap<>();
	
	/**
	 * The map of object plugins
	 */
	private static final Map<Integer, Map<String, ObjectPlugin>> OBJECT_PLUGINS = new HashMap<>();
	
	/**
	 * The map of command plugins
	 */
	private static final Map<String, CommandPlugin> COMMAND_PLUGINS = new HashMap<>();
	
	/**
	 * The map of item plugins
	 */
	private static final Map<Integer, Map<String, ItemPlugin>> ITEM_PLUGINS = new HashMap<>();
	
	/**
	 * The map of special attack plugins
	 */
	private static final Map<Integer, SpecialAttackPlugin> SPECIAL_PLUGINS = new HashMap<>();
	
	/**
	 * The map of range plugins
	 */
	private static final Map<String, RangeWeaponPlugin> RANGE_PLUGINS = new HashMap<>();
	
	/**
	 * The map of item on item  plugins
	 */
	private static final Map<Integer, Map<Integer, ItemOnItemPlugin>> ITEM_ON_ITEM_PLUGINS = new HashMap<>();
	
	/**
	 * The map of item on object plugins
	 */
	private static final Map<Integer, Map<Integer, ItemOnObjectPlugin>> ITEM_ON_OBJECT_PLUGINS = new HashMap<>();
	
	/**
	 * The map of item on object plugins
	 */
	private static final Map<Integer, Map<Integer, ItemOnNPCPlugin>> ITEM_ON_NPC_PLUGINS = new HashMap<>();
	
	/**
	 * The map of item on object plugins
	 */
	private static final Map<Integer, ItemOnPlayerPlugin> ITEM_ON_PLAYER_PLUGINS = new HashMap<>();
	
	/**
	 * The map of spell plugins
	 */
	private static final Map<MagicBook, Map<Integer, SpellPlugin>> SPELL_PLUGINS = new HashMap<>();
	
	/**
	 * The list of all plugins
	 */
	private static final AtomicInteger PLUGIN_COUNT = new AtomicInteger();
	
	/**
	 * Reloads all plugins
	 */
	public static void reload() {
		PLUGIN_COUNT.set(0);
		registerAll();
	}
	
	/**
	 * Registers all the plugins
	 */
	public static void registerAll() {
		Misc.getClasses("plugin").stream().filter(Plugin.class::isInstance).forEach(clazz -> {
			Plugin plugin = (Plugin) clazz;
			plugin.register();
			PLUGIN_COUNT.incrementAndGet();
		});
		System.out.println("Registered " + PLUGIN_COUNT.intValue() + " plugins");
	}
	
	/**
	 * Gets the amount of spells registered
	 */
	private static int getSpellCount() {
		int count = 0;
		for (Entry<MagicBook, Map<Integer, SpellPlugin>> entry : SPELL_PLUGINS.entrySet()) {
			for (Entry<Integer, SpellPlugin> pluginEntry : entry.getValue().entrySet()) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Registers a plugin with integer keys
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
		if (plugin instanceof SpecialAttackPlugin) {
			for (int key : keys) {
				SPECIAL_PLUGINS.put(key, (SpecialAttackPlugin) plugin);
			}
		}
	}
	
	/**
	 * Registers a plugin with string keys
	 *
	 * @param plugin
	 * 		The plugin
	 * @param keys
	 * 		The keys
	 */
	public static void register(Plugin plugin, String... keys) {
		if (plugin instanceof CommandPlugin) {
			for (String key : keys) {
				COMMAND_PLUGINS.put(key, (CommandPlugin) plugin);
			}
		}
		if (plugin instanceof RangeWeaponPlugin) {
			for (String key : keys) {
				RANGE_PLUGINS.put(key, (RangeWeaponPlugin) plugin);
			}
		}
	}
	
	/**
	 * Registers a spell plugin
	 */
	public static void register(SpellPlugin spellPlugin, MagicBook book, int spellId) {
		Map<Integer, SpellPlugin> spellPluginMap = SPELL_PLUGINS.get(book);
		if (spellPluginMap == null) {
			spellPluginMap = new HashMap<>();
		}
		spellPluginMap.put(spellId, spellPlugin);
		SPELL_PLUGINS.put(book, spellPluginMap);
	}
	
	/**
	 * Registers a plugin that is dependent on {@link ClickOption}s
	 *
	 * @param plugin
	 * 		The plugin
	 * @param key
	 * 		The key of the plugin
	 * @param options
	 * 		The options to register for the plugin
	 */
	public static void registerOptionPlugin(Plugin plugin, int key, String... options) {
		if (plugin instanceof NPCPlugin) {
			Map<String, NPCPlugin> pluginMap;
			if (NPC_PLUGINS.containsKey(key)) {
				pluginMap = NPC_PLUGINS.get(key);
			} else {
				pluginMap = new HashMap<>();
			}
			for (String option : options) {
				pluginMap.put(option, (NPCPlugin) plugin);
			}
			NPC_PLUGINS.put(key, pluginMap);
		}
		if (plugin instanceof ObjectPlugin) {
			Map<String, ObjectPlugin> pluginMap;
			if (OBJECT_PLUGINS.containsKey(key)) {
				pluginMap = OBJECT_PLUGINS.get(key);
			} else {
				pluginMap = new HashMap<>();
			}
			for (String option : options) {
				pluginMap.put(option, (ObjectPlugin) plugin);
			}
			OBJECT_PLUGINS.put(key, pluginMap);
		}
		if (plugin instanceof ItemPlugin) {
			Map<String, ItemPlugin> pluginMap;
			if (ITEM_PLUGINS.containsKey(key)) {
				pluginMap = ITEM_PLUGINS.get(key);
			} else {
				pluginMap = new HashMap<>();
			}
			for (String option : options) {
				pluginMap.put(option, (ItemPlugin) plugin);
			}
			ITEM_PLUGINS.put(key, pluginMap);
		}
	}
	
	/**
	 * Registers an entity plugin that is used on another entity of the same type
	 *
	 * @param plugin
	 * 		The plugin instance
	 * @param key
	 * 		The key of the plugin
	 * @param withs
	 * 		The with ids
	 */
	public static void registerEntityOnPlugin(Plugin plugin, int key, int... withs) {
		if (plugin instanceof ItemOnItemPlugin) {
			Map<Integer, ItemOnItemPlugin> plugins = ITEM_ON_ITEM_PLUGINS.get(key);
			if (plugins == null) {
				plugins = new HashMap<>();
			}
			for (int with : withs) {
				plugins.put(with, (ItemOnItemPlugin) plugin);
			}
			ITEM_ON_ITEM_PLUGINS.put(key, plugins);
		}
		if (plugin instanceof ItemOnObjectPlugin) {
			Map<Integer, ItemOnObjectPlugin> plugins = ITEM_ON_OBJECT_PLUGINS.get(key);
			if (plugins == null) {
				plugins = new HashMap<>();
			}
			for (int with : withs) {
				plugins.put(with, (ItemOnObjectPlugin) plugin);
			}
			ITEM_ON_OBJECT_PLUGINS.put(key, plugins);
		}
		if (plugin instanceof ItemOnNPCPlugin) {
			Map<Integer, ItemOnNPCPlugin> plugins = ITEM_ON_NPC_PLUGINS.get(key);
			if (plugins == null) {
				plugins = new HashMap<>();
			}
			for (int with : withs) {
				plugins.put(with, (ItemOnNPCPlugin) plugin);
			}
			ITEM_ON_NPC_PLUGINS.put(key, plugins);
		}
		if (plugin instanceof ItemOnPlayerPlugin) {
			ITEM_ON_PLAYER_PLUGINS.put(key, (ItemOnPlayerPlugin) plugin);
		}
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
	 * @return The plugin that handled the interface
	 */
	public static InterfacePlugin handleInterface(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		List<InterfacePlugin> interfacePlugins = INTERFACE_PLUGINS.get(interfaceId);
		if (interfacePlugins == null) {
			return null;
		}
		for (InterfacePlugin plugin : interfacePlugins) {
			if (plugin.handle(player, interfaceId, componentId, itemId, slotId, packetId)) {
				return plugin;
			}
		}
		return null;
	}
	
	/**
	 * Gets a special plugin by the id of a weapon
	 */
	public static Optional<SpecialAttackPlugin> getSpecialPlugin(int weaponId) {
		return Optional.ofNullable(SPECIAL_PLUGINS.get(weaponId));
	}
	
	/**
	 * Finds an {@code Optional} {@code RangeWeaponPlugin} instance of a weapon
	 */
	public static Optional<RangeWeaponPlugin> getRangeWeapon(int weaponId) {
		String name = weaponId == -1 ? "unarmed" : ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		for (Entry<String, RangeWeaponPlugin> entry : RANGE_PLUGINS.entrySet()) {
			String specialName = entry.getKey();
			String regex = specialName.replaceAll("\\*", ".*");
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(name);
			if (matcher.find()) {
				return Optional.of(entry.getValue());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets a spell plugin from the map
	 */
	public static Optional<SpellPlugin> getSpellPlugin(MagicBook book, int spellId) {
		Map<Integer, SpellPlugin> spellPluginMap = SPELL_PLUGINS.get(book);
		if (spellPluginMap == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(spellPluginMap.get(spellId));
	}
	
	/**
	 * Handles the npc interaction with the right plugin
	 *
	 * @param player
	 * 		The player
	 * @param npc
	 * 		The npc
	 * @param option
	 * 		The option clicked
	 */
	public static boolean handleNPC(Player player, NPC npc, String option) {
		Map<String, NPCPlugin> pluginMap = NPC_PLUGINS.get(npc.getId());
		if (pluginMap == null) {
			return false;
		}
		NPCPlugin plugin = pluginMap.get(option);
		if (plugin == null) {
			return false;
		}
		return plugin.handle(player, npc, option);
	}
	
	/**
	 * Handles the object interaction with the right plugin
	 *
	 * @param player
	 * 		The player
	 * @param object
	 * 		The object
	 * @param option
	 * 		The option clicked
	 */
	public static boolean handleObject(Player player, WorldObject object, String option) {
		Map<String, ObjectPlugin> pluginMap = OBJECT_PLUGINS.get(object.getId());
		if (pluginMap == null) {
			return false;
		}
		ObjectPlugin plugin = pluginMap.get(option);
		if (plugin == null) {
			return false;
		}
		return plugin.handle(player, object, option);
	}
	
	/**
	 * Handles the item interaction with the right plugin
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item clicked
	 * @param slotId
	 * 		The slot the item came from
	 * @param option
	 * 		The option clicked as a string
	 */
	public static boolean handleItem(Player player, Item item, int slotId, String option) {
		Map<String, ItemPlugin> pluginMap = ITEM_PLUGINS.get(item.getId());
		if (pluginMap == null) {
			return false;
		}
		ItemPlugin plugin = pluginMap.get(option);
		if (plugin == null) {
			return false;
		}
		return plugin.handle(player, item, slotId, option);
	}
	
	/**
	 * Handles the item on item logic
	 *
	 * @param player
	 * 		The player
	 * @param used
	 * 		The item used
	 * @param with
	 * 		The item used with
	 */
	public static boolean handleItemOnItem(Player player, Item used, Item with) {
		Map<Integer, ItemOnItemPlugin> itemOnPluginMap = ITEM_ON_ITEM_PLUGINS.get(used.getId());
		if (itemOnPluginMap == null) {
			itemOnPluginMap = ITEM_ON_ITEM_PLUGINS.get(with.getId());
		}
		if (itemOnPluginMap == null) {
			return false;
		}
		ItemOnItemPlugin plugin = itemOnPluginMap.get(used.getId());
		if (plugin == null) {
			plugin = itemOnPluginMap.get(with.getId());
		}
		if (plugin == null) {
			return false;
		}
		return plugin.handleItemOnItem(player, used, with);
	}
	
	/**
	 * Handles the usage of a plugin for an item on an object interaction
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item used
	 * @param object
	 * 		The object the item was used on
	 */
	public static boolean handleItemOnObject(Player player, Item item, WorldObject object) {
		Map<Integer, ItemOnObjectPlugin> itemOnPluginMap = ITEM_ON_OBJECT_PLUGINS.get(item.getId());
		if (itemOnPluginMap == null) {
			return false;
		}
		ItemOnObjectPlugin plugin = itemOnPluginMap.get(object.getId());
		if (plugin == null) {
			return false;
		}
		return plugin.handle(player, item, object);
	}
	
	/**
	 * Handles the usage of a plugin for an item on an npc interaction
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item used
	 * @param npc
	 * 		The npc the item was used on
	 */
	public static boolean handleItemOnNPC(Player player, Item item, NPC npc) {
		Map<Integer, ItemOnNPCPlugin> itemOnPluginMap = ITEM_ON_NPC_PLUGINS.get(item.getId());
		if (itemOnPluginMap == null) {
			return false;
		}
		ItemOnNPCPlugin plugin = itemOnPluginMap.get(npc.getId());
		if (plugin == null) {
			return false;
		}
		return plugin.handle(player, item, npc);
	}
	/**
	 * Handles the usage of a plugin for an item on an npc interaction
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item used
	 * @param partner
	 * 		The player the item was used on
	 */
	public static boolean handleItemOnPlayer(Player player, Item item, Player partner) {
		ItemOnPlayerPlugin plugin = ITEM_ON_PLAYER_PLUGINS.get(item.getId());
		if (plugin == null) {
			return false;
		}
		return plugin.handle(player, item, partner);
	}
	
	/**
	 * Handles a command request
	 */
	public static void handleCommand(Player player, String[] args, boolean console, boolean clientCommand) {
		if (args.length == 0) {
			return;
		}
		String name = args[0];
		CommandPlugin command = COMMAND_PLUGINS.get(name);
		if (command == null) {
			CommandPlugin.sendResponse(player, "Could not find command by name '" + name + "' - try again...", console);
			return;
		}
		// verifying parameters
		final CommandManifest manifest = command.getManifest();
		if (command.clientCommandOnly() && !clientCommand) {
			CommandPlugin.sendResponse(player, "Unexpected command entry type, please report this on forums.", false);
			return;
		}
		if (!command.getRightRequired().playerHasRights(player)) {
			CommandPlugin.sendResponse(player, "You do not have the rights to use this command.", console);
			return;
		}
		try {
			command.handle(player, args, console, clientCommand);
		} catch (Throwable e) {
			player.getPackets().sendMessage("There was an error processing that command, try again...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the player a message that they entered an unexpected parameter type
	 *
	 * @param player
	 * 		The player
	 * @param name
	 * 		The name of the command
	 * @param manifest
	 * 		The {@code CommandManifest} object
	 * @param console
	 * 		If the command was entered via console
	 */
	private static void sendUnexpectedType(Player player, String name, CommandManifest manifest, boolean console) {
		if (!console) {
			StringBuilder message = new StringBuilder("Command '" + name + "' usage -> " + "::" + "" + name + " ");
			for (Class clazz : manifest.types()) {
				message.append(Misc.getSimplifiedType(clazz.getSimpleName())).append(" ");
			}
			CommandPlugin.sendResponse(player, message.toString(), false);
		} else {
			List<String> messages = new ArrayList<>();
			messages.add("Invalid command parameters...");
			StringBuilder usageLine = new StringBuilder("----> Expected Usage: " + name + " ");
			for (Class clazz : manifest.types()) {
				usageLine.append(Misc.getSimplifiedType(clazz.getSimpleName())).append(" ");
			}
			messages.add(usageLine.toString());
			messages.forEach(message -> CommandPlugin.sendResponse(player, message, true));
		}
	}
	
	/**
	 * Gets the commands
	 */
	public static Collection<CommandPlugin> getCommands() {
		return COMMAND_PLUGINS.values();
	}
	
}