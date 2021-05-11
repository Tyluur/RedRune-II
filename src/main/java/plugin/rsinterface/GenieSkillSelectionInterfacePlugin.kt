package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-01-29
 */
class GenieSkillSelectionInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        return true
    }

    override fun register() {
        registerInterfacePlugin(SKILL_SELECTION_INTERFACE_ID)
    }

    companion object {
        /**
         * The interface id for the skill selection interface
         */
        private const val SKILL_SELECTION_INTERFACE_ID = 1139
        fun displayInterface(player: Player) {
            player.varManager.sendVar(261, 0)
            player.interfaceManager.sendInterface(SKILL_SELECTION_INTERFACE_ID)
        }
    }
}