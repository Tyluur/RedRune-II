package plugin.inter;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class CorporealBeastInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 17) {
			player.stopAll();
			player.setNextWorldTile(new WorldTile(2974, 4384, 0));
			player.getControllerManager().startController("CorpBeastController");
		} else if (componentId == 18) {
			player.closeInterfaces();
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(650);
	}
}
