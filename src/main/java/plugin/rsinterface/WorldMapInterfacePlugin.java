package plugin.rsinterface;

import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
public class WorldMapInterfacePlugin implements InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 44) {
			player.setNextAnimation(new Animation(-1));
		}
		player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746 : 548, 2);
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(755);
	}
}
