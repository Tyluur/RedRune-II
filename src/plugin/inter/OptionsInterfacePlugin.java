package plugin.inter;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class OptionsInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (player.getInterfaceManager().containsInventoryInter()) {
			return true;
		}
		if (componentId == 14) {
			if (player.getInterfaceManager().containsScreenInter()) {
				player.getPackets().sendGameMessage("Please close the interface you have open before setting your graphic options.");
				return true;
			}
			player.stopAll();
			player.getInterfaceManager().sendInterface(742);
		} else if (componentId == 4) {
			player.getPacketSender().switchAllowChatEffects();
		} else if (componentId == 5) {
			player.getInterfaceManager().sendSettings(982);
		} else if (componentId == 6) {
			player.getPacketSender().switchMouseButtons();
		} else if (componentId == 16) {
			if (player.getInterfaceManager().containsScreenInter()) {
				player.getPackets().sendGameMessage("Please close the interface you have open before setting your audio options.");
				return true;
			}
			player.stopAll();
			player.getInterfaceManager().sendInterface(743);
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(261);
	}
}
