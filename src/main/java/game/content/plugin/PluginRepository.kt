package game.content.plugin

import com.github.michaelbull.logging.InlineLogger
import cache.codec.loaders.ItemDefinitions
import game.content.plugin.combat.RangeWeaponPlugin
import game.content.plugin.combat.SpecialAttackPlugin
import game.content.plugin.combat.spell.SpellPlugin
import game.content.plugin.type.*
import game.entity.`object`.WorldObject
import game.entity.actor.npc.NPC
import game.entity.actor.player.Player
import game.entity.item.Item
import utility.constants.MagicConstants.MagicBook
import utility.functions.Misc
import plugin.command.CommandManifest
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import java.util.regex.Pattern

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/30/2017
 */
object PluginRepository {
    /**
     * The map of interface plugins
     */
    private val INTERFACE_PLUGINS: MutableMap<Int, MutableList<InterfacePlugin>> = HashMap()

    /**
     * The map of npc plugins
     */
    private val NPC_PLUGINS: MutableMap<Int, MutableMap<String, NPCPlugin>> = HashMap()

    /**
     * The map of object plugins
     */
    private val OBJECT_PLUGINS: MutableMap<Int, MutableMap<String, ObjectPlugin>> = HashMap()

    /**
     * The map of command plugins
     */
    private val COMMAND_PLUGINS: MutableMap<String, CommandPlugin> = HashMap()

    /**
     * The map of item plugins
     */
    private val ITEM_PLUGINS: MutableMap<Int, MutableMap<String, ItemPlugin>> = HashMap()

    /**
     * The map of special attack plugins
     */
    private val SPECIAL_PLUGINS: MutableMap<Int, SpecialAttackPlugin> = HashMap()

    /**
     * The map of range plugins
     */
    private val RANGE_PLUGINS: MutableMap<String, RangeWeaponPlugin> = HashMap()

    /**
     * The map of item on item  plugins
     */
    private val ITEM_ON_ITEM_PLUGINS: MutableMap<Int, MutableMap<Int, ItemOnItemPlugin>> = HashMap()

    /**
     * The map of item on object plugins
     */
    private val ITEM_ON_OBJECT_PLUGINS: MutableMap<Int, MutableMap<Int, ItemOnObjectPlugin>> = HashMap()

    /**
     * The map of item on object plugins
     */
    private val ITEM_ON_NPC_PLUGINS: MutableMap<Int, MutableMap<Int, ItemOnNPCPlugin>> = HashMap()

    /**
     * The map of item on object plugins
     */
    private val ITEM_ON_PLAYER_PLUGINS: MutableMap<Int, ItemOnPlayerPlugin> = HashMap()

    /**
     * The map of spell plugins
     */
    private val SPELL_PLUGINS: MutableMap<MagicBook, MutableMap<Int, SpellPlugin>> = HashMap()

    /**
     * The list of all plugins
     */
    private val PLUGIN_COUNT = AtomicInteger()

    /**
     * Reloads all plugins
     */
    fun reload() {
        PLUGIN_COUNT.set(0)
        registerAll()
    }

    /**
     * Registers all the plugins
     */
    fun registerAll() {
        Misc.getClasses("plugin").stream().filter { obj: Any? -> Plugin::class.java.isInstance(obj) }
            .forEach { clazz: Any ->
                val plugin = clazz as Plugin
                plugin.register()
                PLUGIN_COUNT.incrementAndGet()
            }
        logger.info { ("Registered ${PLUGIN_COUNT.toInt()} plugins") }
    }

    /**
     * Gets the amount of spells registered
     */
    private val spellCount: Int
        private get() {
            var count = 0
            for ((_, value) in SPELL_PLUGINS) {
                for ((key, value1) in value) {
                    count++
                }
            }
            return count
        }

    /**
     * Registers a plugin with integer keys
     *
     * @param plugin
     * The plugin
     * @param keys
     * The keys
     */
    fun register(plugin: Plugin?, vararg keys: Int) {
        if (plugin is InterfacePlugin) {
            for (key in keys) {
                var pluginList = INTERFACE_PLUGINS[key]
                if (pluginList == null) {
                    pluginList = ArrayList()
                }
                pluginList.add(plugin)
                INTERFACE_PLUGINS[key] = pluginList
            }
        }
        if (plugin is SpecialAttackPlugin) {
            for (key in keys) {
                SPECIAL_PLUGINS[key] = plugin
            }
        }
    }

    /**
     * Registers a plugin with string keys
     *
     * @param plugin
     * The plugin
     * @param keys
     * The keys
     */
    fun register(plugin: Plugin?, vararg keys: String) {
        if (plugin is CommandPlugin) {
            for (key in keys) {
                COMMAND_PLUGINS[key] = plugin
            }
        }
        if (plugin is RangeWeaponPlugin) {
            for (key in keys) {
                RANGE_PLUGINS[key] = plugin
            }
        }
    }

    /**
     * Registers a spell plugin
     */
    @JvmStatic
    fun register(spellPlugin: SpellPlugin, book: MagicBook, spellId: Int) {
        var spellPluginMap = SPELL_PLUGINS[book]
        if (spellPluginMap == null) {
            spellPluginMap = HashMap()
        }
        spellPluginMap[spellId] = spellPlugin
        SPELL_PLUGINS[book] = spellPluginMap
    }

    /**
     * Registers a plugin that is dependent on [ClickOption]s
     *
     * @param plugin
     * The plugin
     * @param key
     * The key of the plugin
     * @param options
     * The options to register for the plugin
     */
    @JvmStatic
    fun registerOptionPlugin(plugin: Plugin?, key: Int, vararg options: String) {
        if (plugin is NPCPlugin) {
            val pluginMap: MutableMap<String, NPCPlugin>
            pluginMap = if (NPC_PLUGINS.containsKey(key)) {
                NPC_PLUGINS[key]!!
            } else {
                HashMap()
            }
            for (option in options) {
                pluginMap[option] = plugin
            }
            NPC_PLUGINS[key] = pluginMap
        }
        if (plugin is ObjectPlugin) {
            val pluginMap: MutableMap<String, ObjectPlugin>
            pluginMap = if (OBJECT_PLUGINS.containsKey(key)) {
                OBJECT_PLUGINS[key]!!
            } else {
                HashMap()
            }
            for (option in options) {
                pluginMap[option] = plugin
            }
            OBJECT_PLUGINS[key] = pluginMap
        }
        if (plugin is ItemPlugin) {
            val pluginMap: MutableMap<String, ItemPlugin>
            pluginMap = if (ITEM_PLUGINS.containsKey(key)) {
                ITEM_PLUGINS[key]!!
            } else {
                HashMap()
            }
            for (option in options) {
                pluginMap[option] = plugin
            }
            ITEM_PLUGINS[key] = pluginMap
        }
    }

    /**
     * Registers an entity plugin that is used on another entity of the same type
     *
     * @param plugin
     * The plugin instance
     * @param key
     * The key of the plugin
     * @param withs
     * The with ids
     */
    @JvmStatic
    fun registerEntityOnPlugin(plugin: Plugin?, key: Int, vararg withs: Int) {
        if (plugin is ItemOnItemPlugin) {
            var plugins = ITEM_ON_ITEM_PLUGINS[key]
            if (plugins == null) {
                plugins = HashMap()
            }
            for (with in withs) {
                plugins[with] = plugin
            }
            ITEM_ON_ITEM_PLUGINS[key] = plugins
        }
        if (plugin is ItemOnObjectPlugin) {
            var plugins = ITEM_ON_OBJECT_PLUGINS[key]
            if (plugins == null) {
                plugins = HashMap()
            }
            for (with in withs) {
                plugins[with] = plugin
            }
            ITEM_ON_OBJECT_PLUGINS[key] = plugins
        }
        if (plugin is ItemOnNPCPlugin) {
            var plugins = ITEM_ON_NPC_PLUGINS[key]
            if (plugins == null) {
                plugins = HashMap()
            }
            for (with in withs) {
                plugins[with] = plugin
            }
            ITEM_ON_NPC_PLUGINS[key] = plugins
        }
        if (plugin is ItemOnPlayerPlugin) {
            ITEM_ON_PLAYER_PLUGINS[key] = plugin
        }
    }

    /**
     * Handles the interface interaction with the best plugin
     *
     * @param player
     * The player clicking the interface
     * @param interfaceId
     * The id of the interface
     * @param componentId
     * The component id of the interface
     * @param itemId
     * The item id on the interface, -1 if none.
     * @param slotId
     * The slot id on the interface, -1 if none.
     * @param packetId
     * The packet id of the click, different ids are used for different options
     * @return The plugin that handled the interface
     */
    @JvmStatic
    fun handleInterface(
        player: Player?,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): InterfacePlugin? {
        val interfacePlugins = INTERFACE_PLUGINS[interfaceId]
            ?: return null
        for (plugin in interfacePlugins) {
            if (plugin.handle(player, interfaceId, componentId, itemId, slotId, packetId)) {
                return plugin
            }
        }
        return null
    }

    /**
     * Gets a special plugin by the id of a weapon
     */
    @JvmStatic
    fun getSpecialPlugin(weaponId: Int): Optional<SpecialAttackPlugin> {
        return Optional.ofNullable(SPECIAL_PLUGINS[weaponId])
    }

    /**
     * Finds an `Optional` `RangeWeaponPlugin` instance of a weapon
     */
    @JvmStatic
    fun getRangeWeapon(weaponId: Int): Optional<RangeWeaponPlugin> {
        val name = if (weaponId == -1) "unarmed" else ItemDefinitions.getItemDefinitions(weaponId).name.toLowerCase()
        for ((specialName, value) in RANGE_PLUGINS) {
            val regex = specialName.replace("\\*".toRegex(), ".*")
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(name)
            if (matcher.find()) {
                return Optional.of(value)
            }
        }
        return Optional.empty()
    }

    /**
     * Gets a spell plugin from the map
     */
    @JvmStatic
    fun getSpellPlugin(book: MagicBook, spellId: Int): Optional<SpellPlugin> {
        val spellPluginMap = SPELL_PLUGINS[book]
            ?: return Optional.empty()
        return Optional.ofNullable(spellPluginMap[spellId])
    }

    /**
     * Handles the npc interaction with the right plugin
     *
     * @param player
     * The player
     * @param npc
     * The npc
     * @param option
     * The option clicked
     */
    @JvmStatic
    fun handleNPC(player: Player?, npc: NPC, option: String): Boolean {
        val pluginMap = NPC_PLUGINS[npc.id]
            ?: return false
        val plugin = pluginMap[option] ?: return false
        return plugin.handle(player, npc, option)
    }

    /**
     * Handles the object interaction with the right plugin
     *
     * @param player
     * The player
     * @param object
     * The object
     * @param option
     * The option clicked
     */
    @JvmStatic
    fun handleObject(player: Player?, `object`: WorldObject, option: String): Boolean {
        val pluginMap = OBJECT_PLUGINS[`object`.id]
            ?: return false
        val plugin = pluginMap[option] ?: return false
        return plugin.handle(player, `object`, option)
    }

    /**
     * Handles the item interaction with the right plugin
     *
     * @param player
     * The player
     * @param item
     * The item clicked
     * @param slotId
     * The slot the item came from
     * @param option
     * The option clicked as a string
     */
    @JvmStatic
    fun handleItem(player: Player?, item: Item, slotId: Int, option: String): Boolean {
        val pluginMap = ITEM_PLUGINS[item.id]
            ?: return false
        val plugin = pluginMap[option] ?: return false
        return plugin.handle(player, item, slotId, option)
    }

    /**
     * Handles the item on item logic
     *
     * @param player
     * The player
     * @param used
     * The item used
     * @param with
     * The item used with
     */
    @JvmStatic
    fun handleItemOnItem(player: Player?, used: Item, with: Item): Boolean {
        var itemOnPluginMap: Map<Int, ItemOnItemPlugin>? = ITEM_ON_ITEM_PLUGINS[used.id]
        if (itemOnPluginMap == null) {
            itemOnPluginMap = ITEM_ON_ITEM_PLUGINS[with.id]
        }
        if (itemOnPluginMap == null) {
            return false
        }
        var plugin = itemOnPluginMap[used.id]
        if (plugin == null) {
            plugin = itemOnPluginMap[with.id]
        }
        return plugin?.handleItemOnItem(player, used, with) ?: false
    }

    /**
     * Handles the usage of a plugin for an item on an object interaction
     *
     * @param player
     * The player
     * @param item
     * The item used
     * @param object
     * The object the item was used on
     */
    @JvmStatic
    fun handleItemOnObject(player: Player?, item: Item, `object`: WorldObject): Boolean {
        val itemOnPluginMap = ITEM_ON_OBJECT_PLUGINS[item.id]
            ?: return false
        val plugin = itemOnPluginMap[`object`.id] ?: return false
        return plugin.handle(player, item, `object`)
    }

    /**
     * Handles the usage of a plugin for an item on an npc interaction
     *
     * @param player
     * The player
     * @param item
     * The item used
     * @param npc
     * The npc the item was used on
     */
    @JvmStatic
    fun handleItemOnNPC(player: Player?, item: Item, npc: NPC): Boolean {
        val itemOnPluginMap = ITEM_ON_NPC_PLUGINS[item.id]
            ?: return false
        val plugin = itemOnPluginMap[npc.id] ?: return false
        return plugin.handle(player, item, npc)
    }

    /**
     * Handles the usage of a plugin for an item on an npc interaction
     *
     * @param player
     * The player
     * @param item
     * The item used
     * @param partner
     * The player the item was used on
     */
    @JvmStatic
    fun handleItemOnPlayer(player: Player?, item: Item, partner: Player?): Boolean {
        val plugin = ITEM_ON_PLAYER_PLUGINS[item.id] ?: return false
        return plugin.handle(player, item, partner)
    }

    /**
     * Handles a command request
     */
    @JvmStatic
    fun handleCommand(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        if (args.isEmpty()) {
            return
        }
        val name = args[0]
        val command = COMMAND_PLUGINS[name]
        if (command == null) {
            CommandPlugin.sendResponse(player, "Could not find command by name '$name' - try again...", console)
            return
        }
        // verifying parameters
        val manifest: CommandManifest? = command.manifest
        if (command.clientCommandOnly() && !clientCommand) {
            CommandPlugin.sendResponse(player, "Unexpected command entry type, please report this on forums.", false)
            return
        }
        if (!command.rightRequired.playerHasRights(player)) {
            CommandPlugin.sendResponse(player, "You do not have the rights to use this command.", console)
            return
        }
        try {
            command.handle(player, args, console, clientCommand)
        } catch (e: Throwable) {
            player.packets.sendMessage("There was an error processing that command, try again...")
            e.printStackTrace()
        }
    }

    /**
     * Sends the player a message that they entered an unexpected parameter type
     *
     * @param player
     * The player
     * @param name
     * The name of the command
     * @param manifest
     * The `CommandManifest` object
     * @param console
     * If the command was entered via console
     */
    private fun sendUnexpectedType(player: Player, name: String, manifest: CommandManifest, console: Boolean) {
        if (!console) {
            val message = StringBuilder("Command '$name' usage -> ::$name ")
            for (clazz in manifest.types) {
                message.append(Misc.getSimplifiedType(clazz.simpleName)).append(" ")
            }
            CommandPlugin.sendResponse(player, message.toString(), false)
        } else {
            val messages: MutableList<String> = ArrayList()
            messages.add("Invalid command parameters...")
            val usageLine = StringBuilder("----> Expected Usage: $name ")
            for (clazz in manifest.types) {
                usageLine.append(Misc.getSimplifiedType(clazz.simpleName)).append(" ")
            }
            messages.add(usageLine.toString())
            messages.forEach(Consumer { message: String? -> CommandPlugin.sendResponse(player, message, true) })
        }
    }

    /**
     * Gets the commands
     */
    val commands: Collection<CommandPlugin>
        get() = COMMAND_PLUGINS.values

    private val logger = InlineLogger()
}