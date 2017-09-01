package plugin.inter;

import com.rs.game.content.skills.smithing.Smithing.ForgingInterface;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class ForgingInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		ForgingInterface.handleIComponents(player, componentId);
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(300);
	}
}
