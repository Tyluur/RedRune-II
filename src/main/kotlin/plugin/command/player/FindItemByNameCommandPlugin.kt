package plugin.command.player

import plugin.command.CommandManifest
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.global.World
import org.redrune.utility.constants.SkillConstants
import plugin.command.player.YellCommandPlugin
import org.redrune.utility.functions.Misc
import org.redrune.game.entity.actor.player.data.PlayerRight
import org.redrune.utility.constants.InterfaceConstants
import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.ColorConstants
import java.util.*
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Finds an item by the name we want", types = [String::class])
class FindItemByNameCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        // the identifier to search by
        val identifier = args[1].replace("_".toRegex(), " ")
        // the option flag
        val optionFlag = stringParamOrDefault(args, 2, null)

        // the list of items that were found
        val found: MutableList<String> = ArrayList()
        for (itemId in 0 until Misc.getItemDefinitionsSize()) {
            // the definition instance
            val definition = ItemDefinitions.getItemDefinitions(itemId)
            if (definition == null) {
                println("Item #$itemId had no definitions.")
                continue
            }
            // the name of the item
            val name = definition.name.toLowerCase()
            var added = false
            // the name has the identifier we want
            if (name.contains(identifier)) {
                // we only want to find items by the identifier
                if (optionFlag == null) {
                    added = true
                } else {
                    // we found that the definition has the option we want
                    if (definition.hasOption(optionFlag)) {
                        added = true
                    }
                }
            }
            // the item was not added because it was not found by filter
            if (!added) {
                continue
            }
            found.add(
                "[<col=FF0000>" + itemId + "</col>] <col=" + ColorConstants.LIGHT_BLUE + ">" + definition.name + "</col> inventory=" + Arrays.toString(
                    definition.inventoryOptions
                ) + " ground=" + Arrays.toString(definition.groundOptions)
            )
        }
        // shows the entries found
        found.forEach(Consumer { entry: String? -> player.packets.sendConsoleMessage(entry) })
        // sends a response with the amount of entries found
        sendResponse(player, "Found " + found.size + " items by name '" + identifier + "'.", console)
    }

    override fun identifiers(): Array<String> {
        return arguments("itemn")
    }
}