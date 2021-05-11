package org.redrune.networking.packet.context.impl

import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer
import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.context.PacketContext

/**
 * @author Tyluur <itstyluur>@icloud.com>
 * @since 2019-02-04
 */
class CapeColorCustomizationPacketContext(
    /**
     * The id of the color to use
     */
    private val colorId: Int
) : PacketContext() {
    override fun handle(player: Player) {
        if (player.temporaryAttributes["SkillcapeCustomize"] != null) {
            SkillCapeCustomizer.handleSkillCapeCustomizerColor(player, colorId)
        }
    }
}