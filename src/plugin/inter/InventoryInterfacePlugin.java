package plugin.inter;

import org.redrune.game.content.entity.actor.player.event.item.ItemInteractionEvent;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.networking.packet.handler.InventoryOptionsHandler;

import static org.redrune.utility.constants.PacketConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class InventoryInterfacePlugin implements InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 0) {
			if (slotId > 27 || player.getInterfaceManager().containsInventoryInter()) {
				return true;
			}
			Item item = player.getInventory().getItem(slotId);
			if (item == null || item.getId() != itemId) {
				return true;
			}
			switch(packetId) {
				case ACTION_BUTTON1_PACKET:
				case ACTION_BUTTON2_PACKET:
				case ACTION_BUTTON3_PACKET:
				case ACTION_BUTTON4_PACKET:
				case ACTION_BUTTON5_PACKET:
				case ACTION_BUTTON6_PACKET:
				case ACTION_BUTTON7_PACKET:
					player.getEventManager().start(new ItemInteractionEvent(item, slotId, packetId));
					break;
				case ACTION_BUTTON8_PACKET:
					InventoryOptionsHandler.handleItemOption8(player, slotId, itemId, item);
					break;
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(679);
	}
}
