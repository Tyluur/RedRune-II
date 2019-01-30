package plugin.inter;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.utility.constants.PacketConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class MusicInterfacePlugin implements InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 1) {
			if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
				player.getMusicsManager().playAnotherMusic(slotId / 2);
			} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
				player.getMusicsManager().addToPlayList(slotId / 2);
			} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
				player.getMusicsManager().removeFromPlayList(slotId / 2);
			}
		} else if (componentId == 4) {
			player.getMusicsManager().addPlayingMusicToPlayList();
		} else if (componentId == 10) {
			player.getMusicsManager().switchPlayListOn();
		} else if (componentId == 11) {
			player.getMusicsManager().clearPlayList();
		} else if (componentId == 13) {
			player.getMusicsManager().switchShuffleOn();
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(187);
	}
}
