package plugin.command.player

import org.redrune.game.content.plugin.type.CommandPlugin
import org.redrune.game.entity.actor.player.Player
import plugin.command.CommandManifest


/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Lists the commands available.")
class ListCommandsCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
/*      val messages: MutableList<String> = ArrayList()

        val commandModulesSet: Set<CommandPlugin> = LinkedHashSet<CommandPlugin>(
            PluginRepository.getCommands().stream().filter { commandModule: CommandPlugin ->
                commandModule.rightRequired.playerHasRights(player)
            }.collect(Collectors.toList())
        )


        val commandModules: List<CommandPlugin> = ArrayList(commandModulesSet)
        commandModules.sort(Comparator.comparingInt { o: CommandPlugin -> o.rightRequired.ordinal })
        var lastRight: PlayerRight? = null

        for (command in commandModules) {
            // skips commands that are only for console
            // or commands with no manifest [only i should know about these]
            if (command.clientCommandOnly() || command.manifest == null) {
                continue
            }
            val manifest = command.manifest
            val bldr = StringBuilder()
            val identifiers = command.identifiers()

            // adds the identifiers of the command to the string
            for (i in identifiers.indices) {
                val identifier = identifiers[i]
                bldr.append(identifier).append(if (i == identifiers.size - 1) " -> " else ", ")
            }
            val types: Array<Class<*>> = manifest?.types()
            if (types != null) {
                for (i in types.indices) {
                    val simpleName = Misc.getSimplifiedType(types[i].simpleName)
                    val last = i == types.size - 1
                    if (i == 0) {
                        bldr.append("[")
                        bldr.append(simpleName).append("").append(if (last) "" else ", ")
                        if (i == types.size - 1) {
                            bldr.append("]")
                        }
                    } else if (i == types.size - 1) {
                        bldr.append(simpleName).append("").append(if (last) "" else ", ")
                        bldr.append("]")
                    } else {
                        bldr.append(types[i].javaClass.simpleName)
                    }
                }
                bldr.append(" ")
            }

            // how the command should be executed, with info from the manifest if it exists
            val message =
                "::" + bldr.toString() + if (manifest == null) "" else if (manifest.description() == "") "" else manifest.description()

            // adds the message to the list, and adds right required formatting
            if (lastRight == null || lastRight !== command.rightRequired) {
                if (lastRight != null) {
                    messages.add("")
                }
                messages.add("[" + command.rightRequired.name + "]")
                lastRight = command.rightRequired
            }
            messages.add(message)
        }
        InterfaceConstants.sendQuestScroll(player, "Commands", *messages.toTypedArray())*/
    }

    override fun identifiers(): Array<String> {
        return arguments("commands", "cmds")
    }
}