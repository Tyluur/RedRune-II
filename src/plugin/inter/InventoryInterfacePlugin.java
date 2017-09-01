package plugin.inter;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.item.Item;
import com.rs.game.plugin.type.InterfacePlugin;
import com.rs.networking.codec.decode.WorldPacketsDecoder;
import com.rs.networking.codec.decode.handlers.InventoryOptionsHandler;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class InventoryInterfacePlugin extends InterfacePlugin {
	
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
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				InventoryOptionsHandler.handleItemOption1(player, slotId, itemId, item);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				InventoryOptionsHandler.handleItemOption2(player, slotId, itemId, item);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				InventoryOptionsHandler.handleItemOption3(player, slotId, itemId, item);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				InventoryOptionsHandler.handleItemOption4(player, slotId, itemId, item);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
				InventoryOptionsHandler.handleItemOption5(player, slotId, itemId, item);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
				InventoryOptionsHandler.handleItemOption6(player, slotId, itemId, item);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
				InventoryOptionsHandler.handleItemOption7(player, slotId, itemId, item);
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(679);
	}
}
