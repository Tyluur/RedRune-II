package plugin.inter;

import org.redrune.game.content.entity.actor.player.skills.smithing.Smithing.ForgingInterface;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class ForgingInterfacePlugin implements InterfacePlugin {
	
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
