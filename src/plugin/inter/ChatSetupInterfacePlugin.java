package plugin.inter;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class ChatSetupInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 5) {
			player.getInterfaceManager().sendSettings();
		} else if (componentId == 42) {
			player.setPrivateChatSetup(player.getPrivateChatSetup() == 0 ? 1 : 0);
		} else if (componentId >= 49 && componentId <= 61) {
			player.setPrivateChatSetup(componentId - 48);
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(982);
	}
}
