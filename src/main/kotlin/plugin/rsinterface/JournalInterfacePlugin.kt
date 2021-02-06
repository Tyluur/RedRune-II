package plugin.rsinterface;

import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur
 * @since 2019-05-22
 */
public class JournalInterfacePlugin implements InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		return false;
	}
	
	@Override
	public void register() {
	
	}
}
