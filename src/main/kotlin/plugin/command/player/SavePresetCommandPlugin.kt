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

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Copies another player", types = [String::class])
class SavePresetCommandPlugin : CommandPlugin() {
    override fun handle(player: Player, args: Array<String>, console: Boolean, clientCommand: Boolean) {
        val username = getCompleted(args, 1)
        val p2 = World.getPlayerByDisplayName(username)
        if (p2 == null) {
            player.packets.sendMessage("Couldn't find player $username.")
            return
        }
        val items = p2.equipment.items.itemsCopy
        for (i in items.indices) {
            if (items[i] == null) {
                continue
            }
            val skillRequirements = items[i]!!.definitions.wearingSkillRequirements
            var hasRequirements = true
            if (skillRequirements != null) {
                for (skillId in skillRequirements.keys) {
                    if (skillId > 24 || skillId < 0) {
                        continue
                    }
                    val level = skillRequirements[skillId]!!
                    if (level < 0 || level > 120) {
                        continue
                    }
                    if (player.skills.getLevelForXp(skillId) < level) {
                        if (hasRequirements) {
                            player.packets.sendMessage("You are not high enough level to use this item.")
                        }
                        hasRequirements = false
                        val name = SkillConstants.SKILL_NAME[skillId].toLowerCase()
                        player.packets.sendMessage("You need to have a" + (if (name.startsWith("a")) "n" else "") + " " + name + " level of " + level + ".")
                    }
                }
            }
            if (!hasRequirements) {
                return
            }
            player.equipment.items[i] = items[i]
            player.equipment.refresh(i)
        }
        player.appearance.generateAppearanceData()
    }

    override fun identifiers(): Array<String> {
        return arguments("copy")
    }
}