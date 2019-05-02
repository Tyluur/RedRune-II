package plugin.rsinterface;

import org.redrune.game.content.entity.actor.player.design.PlayerDesign;
import org.redrune.game.content.entity.actor.player.PlayerLook;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class AppearanceCustomizationInterfacePlugin implements InterfacePlugin {
	
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
