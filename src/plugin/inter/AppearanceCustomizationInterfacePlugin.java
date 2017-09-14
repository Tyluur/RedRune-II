package plugin.inter;

import com.rs.game.content.actor.player.design.PlayerDesign;
import com.rs.game.content.PlayerLook;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class AppearanceCustomizationInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == 1028) {
			PlayerDesign.handle(player, componentId, slotId);
		} else if (interfaceId == 900) {
			PlayerLook.handleMageMakeOverButtons(player, componentId);
		} else if (interfaceId == 309) {
			PlayerLook.handleHairdresserSalonButtons(player, componentId, slotId);
		} else if (interfaceId == 729) {
			PlayerLook.handleThessaliasMakeOverButtons(player, componentId, slotId);
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(1028, 900, 309, 729);
	}
}
