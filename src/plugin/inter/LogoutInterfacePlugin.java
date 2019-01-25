package plugin.inter;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class LogoutInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (player.getInterfaceManager().containsInventoryInter()) {
			return true;
		}
		if (componentId == 6 || componentId == 13) {
			if (player.hasFinished()) {
				return true;
			}
			player.logout(false);
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(182);
	}
}
