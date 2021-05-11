package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import game.content.entity.actor.player.skills.SkillCapeCustomizer
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class SkillcapeCustomizationInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        if (interfaceId == 20) {
            SkillCapeCustomizer.handleSkillCapeCustomizer(player, componentId)
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(20)
    }
}