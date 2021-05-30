package org.redrune.game.content.plugin.type

import org.redrune.cache.loaders.ObjectDefinitions
import org.redrune.game.content.plugin.Plugin
import org.redrune.game.content.plugin.PluginRepository
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
interface ObjectPlugin : Plugin {
    /**
     * Handles the interaction with the object
     *
     * @param player
     * The player
     * @param object
     * The object
     * @param option
     * The option clicked
     */
    fun handle(player: Player, `object`: WorldObject, option: String): Boolean

    /**
     * Registers the specified option for a variable amount of object ids
     */
    fun registerSpecifiedOptionVarags(option: ClickOption, vararg objectIds: Int) {
        for (objectId in objectIds) {
            registerSpecifiedOption(option, objectId)
        }
    }

    /**
     * Registers the specified option for an object, this is used when we don't know the option name for an object
     *
     * @param option
     * The option
     * @param objectId
     * The object id
     */
    fun registerSpecifiedOption(option: ClickOption, objectId: Int) {
        val definitions: ObjectDefinitions = ObjectDefinitions.getObjectDefinitions(objectId)
            ?: throw IllegalStateException("Unable to identify definitions for object $objectId")
        val optionSlot: Int = when (option) {
            ClickOption.FIRST -> 1
            ClickOption.SECOND -> 2
            ClickOption.THIRD -> 3
            ClickOption.FOURTH -> 4
            else -> throw IllegalStateException()
        }

        val optionName = definitions.getOption(optionSlot)

        registerObject(objectId, optionName)
    }

    /**
     * Registers this plugin into the repository
     *
     * @param objectId
     * The id of the object
     * @param option
     * The option that will be used
     */
    fun registerObject(objectId: Int, option: String) {
        PluginRepository.registerOptionPlugin(this, objectId, option)
    }
}