package org.redrune.game.content.plugin;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.game.content.plugin.combat.spell.SpellPlugin;
import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.game.content.plugin.type.NPCPlugin;
import org.redrune.game.content.plugin.type.ObjectPlugin;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.MagicConstants.MagicBook;
import org.redrune.utility.game.ClickOption;
import plugin.command.CommandManifest;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * The map of special attack plugins
	 */
	private static final Map<Integer, SpecialAttackPlugin> SPECIAL_PLUGINS = new HashMap<>();
	
	/**
	 * The map of range plugins
	 */
	private static final Map<String, RangeWeaponPlugin> RANGE_PLUGINS = new HashMap<>();
	
	/**
	 * The map of spell plugins
	 */
	private static final Map<MagicBook, Map<Integer, SpellPlugin>> SPELL_PLUGINS = new HashMap<>();
	
	/**
	 * Reloads all plugins
	 */
	public static void reload() {
		SPECIAL_PLUGINS.clear();
		RANGE_PLUGINS.clear();
		SPELL_PLUGINS.clear();
		COMMAND_PLUGINS.clear();
		INTERFACE_PLUGINS.clear();
		NPC_PLUGINS.clear();
		OBJECT_PLUGINS.clear();
		registerAll();
	}
	
	/**
	 * Registers all the plugins
	 */
	public static void registerAll() {
		Misc.getClasses("plugin").stream().filter(Plugin.class::isInstance).forEach(clazz -> ((Plugin) clazz).register());
		System.out.println("Registered " + SPECIAL_PLUGINS.size() + " special plugins, " + RANGE_PLUGINS.size() + " range plugins, " + getSpellCount() + " spell plugins, " + COMMAND_PLUGINS.size() + " command plugins, " + INTERFACE_PLUGINS.size() + " interface plugins, " + NPC_PLUGINS.size() + " npc plugins, and " + OBJECT_PLUGINS.size() + " object plugins.");
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
		} else if (plugin instanceof SpecialAttackPlugin) {
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
		} else if (plugin instanceof RangeWeaponPlugin) {
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
		} else if (plugin instanceof ObjectPlugin) {
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
		
		if (manifest != null && manifest.types().length > 0) {
			final Class[] types = manifest.types();
			
			for (int i = 0; i < types.length; i++) {
				String argumentEntry = Misc.getArrayEntry(args, i + 1);
				Class typeExpected = types[i];
				Class typeEntered = argumentEntry == null ? null : Misc.getClassType(argumentEntry);
				
				if (!Objects.equals(typeExpected, typeEntered)) {
					sendUnexpectedType(player, name, manifest, console);
					return;
				}
			}
		}
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
			StringBuilder usageLine = new StringBuilder("---->Expected Usage: " + name + " ");
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