package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import game.content.entity.actor.player.dialogue.impl.SkillsDialogue
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class SkillDialogueInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        SkillsDialogue.handleSetQuantityButtons(player, componentId)
        return true
    }

    override fun register() {
        registerInterfacePlugin(916)
    }
}