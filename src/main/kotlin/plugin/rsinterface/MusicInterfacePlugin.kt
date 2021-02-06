package plugin.rsinterface;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.utility.constants.PacketConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
public class MusicInterfacePlugin implements InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 1) {
			if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
				player.getMusicManager().playAnotherMusic(slotId / 2);
			} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
				player.getMusicManager().addToPlayList(slotId / 2);
			} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
				player.getMusicManager().removeFromPlayList(slotId / 2);
			}
		} else if (componentId == 4) {
			player.getMusicManager().addPlayingMusicToPlayList();
		} else if (componentId == 10) {
			player.getMusicManager().switchPlayListOn();
		} else if (componentId == 11) {
			player.getMusicManager().clearPlayList();
		} else if (componentId == 13) {
			player.getMusicManager().switchShuffleOn();
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(187);
	}
}
