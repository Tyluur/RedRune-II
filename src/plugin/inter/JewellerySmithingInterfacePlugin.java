package plugin.inter;

import com.rs.game.content.skills.crafting.JewelrySmithing;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class JewellerySmithingInterfacePlugin extends InterfacePlugin {
	
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
