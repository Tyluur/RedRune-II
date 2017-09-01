package plugin.inter;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;
import com.rs.networking.codec.decode.WorldPacketsDecoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class DepositBoxInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 17) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				player.getBank().depositItem(slotId, 1, false);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				player.getBank().depositItem(slotId, 5, false);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				player.getBank().depositItem(slotId, 10, false);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				player.getBank().depositItem(slotId, Integer.MAX_VALUE, false);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
				player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
				player.getTemporaryAttributtes().remove("bank_isWithdraw");
				player.getPackets().sendRunScript(108, "Enter Amount:");
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
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
