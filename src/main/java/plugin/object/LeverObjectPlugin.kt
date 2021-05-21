package plugin.`object`

import org.redrune.game.content.entity.actor.combat.function.Magic
import org.redrune.game.content.entity.actor.player.dialogue.Dialogue
import org.redrune.game.content.plugin.type.ObjectPlugin
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile

class LeverObjectPlugin : ObjectPlugin {
    override fun handle(player: Player, `object`: WorldObject, option: String): Boolean {
        when (`object`.id) {
            // edge lever
            1814 -> {
                player.dialogueManager.startDialogue(object : Dialogue() {
                    override fun start() {
                        sendOptions("Select an Option", "West Dragons", "East Dragons", "Deserted Keep", "Cancel")
                    }

                    override fun run(interfaceId: Int, option: Int) {
                        when (option) {
                            FIRST -> Magic.pushLeverTeleport(player, WEST_DRAGONS)
                            SECOND -> Magic.pushLeverTeleport(player, EAST_DRAGONS)
                            THIRD -> Magic.pushLeverTeleport(player, WorldTile(3155, 3923, 0))
                        }
                        end()
                    }

                    override fun finish() {

                    }
                })
            }
            1815 -> {
                Magic.pushLeverTeleport(player, WorldTile(3090, 3474, 0))
            }
        }

        return true
    }

    override fun register() {
        registerObject(1814, "Pull")
        registerObject(1815, "Pull")
    }

    val EAST_DRAGONS: WorldTile = WorldTile(3348, 3675, 0)

    val WEST_DRAGONS: WorldTile = WorldTile(2975, 3602, 0)
}