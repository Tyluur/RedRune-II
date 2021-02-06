package plugin.rsinterface;

import org.redrune.game.content.entity.actor.player.skills.crafting.JewelrySmithing;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
public class JewellerySmithingInterfacePlugin implements InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		JewelrySmithing.handleButtonClick(player, componentId, packetId == 14 ? 1 : packetId == 67 ? 5 : 10);
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(675);
	}
}
