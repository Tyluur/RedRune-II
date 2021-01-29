package plugin.command.administrator

import org.apache.commons.cli.*
import org.redrune.cache.loaders.ObjectDefinitions
import org.redrune.game.GameFlags
import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.functions.DebugFunctions
import org.redrune.utility.functions.Misc
import plugin.command.CommandManifest
import java.util.*
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-01
 */
@CommandManifest(description = "Finds an object by the name, and other possible parameters", types = [String::class])
class FindObjectByNameCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val identifier = stringParam(args, 1).replace("_".toRegex(), " ")
        val argParameters = Arrays.copyOfRange(args, 2, args.size)
        val options = Options()
        val menuOption = Option("option", "input", true, "context menu option")
        options.addOption(menuOption)
        val parser: CommandLineParser = DefaultParser()
        val cmd: CommandLine
        cmd = try {
            parser.parse(options, argParameters)
        } catch (e: ParseException) {
            e.printStackTrace()
            return
        }
        val optionValue = cmd.getOptionValue("option")


        // the list of items that were found
        val found: MutableList<String> = ArrayList()
        for (id in 0 until Misc.getObjectDefinitionsSize()) {
            val def: ObjectDefinitions = ObjectDefinitions.getObjectDefinitions(id) ?: continue
            val objectName: String = def.name.toLowerCase()
            if (!objectName.contains(identifier)) {
                continue
            }
            val e =
                "Object{id=" + def.getId() + ", name=" + def.getName() + ", options=" + Arrays.toString(def.getOptions()) + ", sizes=[x=" + def.getSizeX() + ",y=" + def.getSizeY() + "]}"
            if (optionValue != null) {
                if (def.containsOption(optionValue)) {
                    found.add(e)
                }
            } else {
                found.add(e)
            }
        }
        // shows the entries found
        found.forEach(Consumer { entry: String? ->
            if (GameFlags.debugMode) {
                DebugFunctions.writeLogText(entry)
            }
            player.packets.sendConsoleMessage(entry)
        })
        if (GameFlags.debugMode) {
            DebugFunctions.writeLogText("--- END OF LOG ENTRY --- ")
        }
        // sends a response with the amount of entries found
        sendResponse(player, "Found " + found.size + " objects by name '" + identifier + "'.", console)
    }

    override fun identifiers(): Array<String> {
        return arguments("objn")
    }
}