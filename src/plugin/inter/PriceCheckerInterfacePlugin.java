package plugin.inter;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.utility.constants.PacketConstants;
import org.redrune.utility.game.InputEvent;
import org.redrune.utility.game.InputEvent.InputEventType;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class PriceCheckerInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == 206) {
			if (componentId == 15) {
				if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, 1);
				} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, 5);
				} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, 10);
				} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
					player.getPackets().requestClientInput(new InputEvent("Enter Amount:", InputEventType.INTEGER) {
						@Override
						public void handleInput() {
							player.getPriceCheckManager().removeItem(slotId, getInput());
						}
					});
				}
			}
		} else if (interfaceId == 207) {
			if (componentId == 0) {
				if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
					player.getPriceCheckManager().addItem(slotId, 1);
				} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					player.getPriceCheckManager().addItem(slotId, 5);
				} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
					player.getPriceCheckManager().addItem(slotId, 10);
				} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
					player.getPriceCheckManager().addItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
					player.getPackets().requestClientInput(new InputEvent("Enter Amount:", InputEventType.INTEGER) {
						@Override
						public void handleInput() {
							player.getPriceCheckManager().addItem(slotId, getInput());
						}
					});
				} else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(206, 207);
	}
}
