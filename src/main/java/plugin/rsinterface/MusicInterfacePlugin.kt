package plugin.rsinterface

import game.content.plugin.type.InterfacePlugin
import utility.constants.PacketConstants
import game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/31/2017
 */
class MusicInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        if (componentId == 1) {
            if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                player.musicManager.playAnotherMusic(slotId / 2)
            } else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
                player.musicManager.addToPlayList(slotId / 2)
            } else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
                player.musicManager.removeFromPlayList(slotId / 2)
            }
        } else if (componentId == 4) {
            player.musicManager.addPlayingMusicToPlayList()
        } else if (componentId == 10) {
            player.musicManager.switchPlayListOn()
        } else if (componentId == 11) {
            player.musicManager.clearPlayList()
        } else if (componentId == 13) {
            player.musicManager.switchShuffleOn()
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(187)
    }
}