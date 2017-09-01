package plugin.inter;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;
import com.rs.networking.codec.decode.WorldPacketsDecoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class PrayerInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == 271) {
			if (componentId == 8 || componentId == 42) {
				player.getPrayer().switchPrayer(slotId);
			} else if (componentId == 43 && player.getPrayer().isUsingQuickPrayer()) {
				player.getPrayer().switchSettingQuickPrayer();
			}
		} else if (interfaceId == 749) {
			if (componentId == 1) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) // activate
				{
					player.getPrayer().switchQuickPrayers();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) // switch
				{
					player.getPrayer().switchSettingQuickPrayer();
				}
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(271, 749);
	}
}
