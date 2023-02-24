package plugin.rsinterface

import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.PacketConstants

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class PrayerInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int,
    ): Boolean {
        if (interfaceId == 271) {
            if (componentId == 8 || componentId == 42) {
                player.prayer.switchPrayer(slotId)
            } else if (componentId == 43 && player.prayer.isUsingQuickPrayer) {
                player.prayer.switchSettingQuickPrayer()
            }
        } else if (interfaceId == 749) {
            if (componentId == 1) {
                if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) // activate
                {
                    player.prayer.switchQuickPrayers()
                } else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) // switch
                {
                    player.prayer.switchSettingQuickPrayer()
                }
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(271, 749)
    }
}