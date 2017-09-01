package plugin.inter;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class FriendChatInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		player.getContactManager().handleFriendChatButtons(interfaceId, componentId, packetId);
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(1108, 1109, 1110);
	}
}
