package plugin.inter;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.InterfacePlugin;
import org.redrune.utility.constants.PacketConstants;
import org.redrune.utility.game.InputEvent;
import org.redrune.utility.game.InputEvent.InputEventType;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class DepositBoxInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 17) {
			if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
				player.getBank().depositItem(slotId, 1, false);
			} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
				player.getBank().depositItem(slotId, 5, false);
			} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
				player.getBank().depositItem(slotId, 10, false);
			} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
				player.getBank().depositItem(slotId, Integer.MAX_VALUE, false);
			} else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
				player.getPackets().requestClientInput(new InputEvent("Enter Amount:", InputEventType.INTEGER) {
					@Override
					public void handleInput() {
						player.getBank().depositItem(slotId, getInput(), false);
					}
				});
			} else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
				player.getInventory().sendExamine(slotId);
			}
		} else if (componentId == 18) {
			player.getBank().depositAllInventory(false);
		} else if (componentId == 20) {
			player.getBank().depositAllEquipment(false);
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(11);
	}
	
}
