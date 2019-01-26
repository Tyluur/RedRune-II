package plugin.inter;

import org.redrune.game.content.entity.actor.player.market.Shop;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.utility.constants.PacketConstants;
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository;

import static org.redrune.game.content.entity.actor.player.market.Shop.INTERFACE_ID;
import static org.redrune.game.content.entity.actor.player.market.Shop.INVENTORY_INTERFACE_ID;

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
