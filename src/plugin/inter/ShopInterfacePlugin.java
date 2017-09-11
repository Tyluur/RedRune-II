package plugin.inter;

import com.rs.game.content.market.Shop;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.item.Item;
import com.rs.game.plugin.type.InterfacePlugin;
import com.rs.utility.constants.PacketConstants;
import com.rs.utility.repo.item.ItemCharacteristicRepository;

import static com.rs.game.content.market.Shop.INTERFACE_ID;
import static com.rs.game.content.market.Shop.INVENTORY_INTERFACE_ID;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public class ShopInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		Shop shop = player.getAttribute("open_shop");
		if (shop == null) {
			return true;
		}
		if (interfaceId == INTERFACE_ID) {
			if (componentId == 25) {
				switch(packetId) {
					case PacketConstants.ACTION_BUTTON1_PACKET:
						shop.value(player, slotId, false);
						break;
					case PacketConstants.ACTION_BUTTON2_PACKET:
						shop.buy(player, slotId, 1);
						break;
					case PacketConstants.ACTION_BUTTON3_PACKET:
						shop.buy(player, slotId, 5);
						break;
					case PacketConstants.ACTION_BUTTON4_PACKET:
						shop.buy(player, slotId, 10);
						break;
					case PacketConstants.ACTION_BUTTON5_PACKET:
						shop.buy(player, slotId, 50);
						break;
					case PacketConstants.ACTION_BUTTON9_PACKET:
						shop.buy(player, slotId, 500);
						break;
					case PacketConstants.ACTION_BUTTON8_PACKET:
						Item item = shop.getItem(slotId / 6);
						player.getPackets().sendGameMessage(ItemCharacteristicRepository.getExamine(item));
						break;
				}
			}
		} else if (interfaceId == INVENTORY_INTERFACE_ID) {
			if (componentId == 0) {
				switch (packetId) {
					case PacketConstants.ACTION_BUTTON1_PACKET:
						shop.value(player, slotId, true);
						break;
					case PacketConstants.ACTION_BUTTON2_PACKET:
						shop.sell(player, slotId, 1);
						break;
					case PacketConstants.ACTION_BUTTON3_PACKET:
						shop.sell(player, slotId, 5);
						break;
					case PacketConstants.ACTION_BUTTON4_PACKET:
						shop.sell(player, slotId, 10);
						break;
					case PacketConstants.ACTION_BUTTON5_PACKET:
						shop.sell(player, slotId, 50);
						break;
					case PacketConstants.ACTION_BUTTON9_PACKET:
						player.getInventory().sendExamine(slotId);
						break;
				}
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(INTERFACE_ID, INVENTORY_INTERFACE_ID);
	}
}
