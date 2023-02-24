package plugin.rsinterface

import org.redrune.game.content.entity.actor.player.dialogue.impl.SkillsDialogue
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class SkillDialogueInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int,
    ): Boolean {
        SkillsDialogue.handleSetQuantityButtons(player, componentId)
        return true
    }

    override fun register() {
        registerInterfacePlugin(916)
    }
}