package plugin.inter;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.utility.constants.PacketConstants;
import org.redrune.utility.game.InputEvent;
import org.redrune.utility.game.InputEvent.InputEventType;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
public class TradeInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		 if (interfaceId== 334) {
			if (componentId == 22) {
				player.closeInterfaces();
			} else if (componentId == 21) {
				player.getTradeManager().accept(false);
			}
		} else if (interfaceId== 335) {
			if (componentId == 16) {
				player.getTradeManager().accept(true);
			} else if (componentId == 18) {
				player.closeInterfaces();
			} else if (componentId == 31) {
				if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
					player.getTradeManager().removeItem(slotId, 1);
				} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					player.getTradeManager().removeItem(slotId, 5);
				} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
					player.getTradeManager().removeItem(slotId, 10);
				} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
					player.getTradeManager().removeItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
					player.getPackets().requestClientInput(new InputEvent("Enter Amount:", InputEventType.INTEGER) {
						@Override
						public void handleInput() {
							player.getTradeManager().removeItem(slotId, getInput());
						}
					});
				} else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
					player.getTradeManager().sendValue(slotId, false);
				} else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
					player.getTradeManager().sendExamine(slotId, false);
				}
			} else if (componentId == 34) {
				if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
					player.getTradeManager().sendValue(slotId, true);
				} else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
					player.getTradeManager().sendExamine(slotId, true);
				}
			}
		} else if (interfaceId== 336) {
			if (componentId == 0) {
				if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
					player.getTradeManager().addItem(slotId, 1);
				} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					player.getTradeManager().addItem(slotId, 5);
				} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
					player.getTradeManager().addItem(slotId, 10);
				} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
					player.getTradeManager().addItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
					player.getPackets().requestClientInput(new InputEvent("Enter Amount:", InputEventType.INTEGER) {
						@Override
						public void handleInput() {
							player.getTradeManager().addItem(slotId, getInput());
						}
					});
				} else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
					player.getTradeManager().sendValue(slotId);
				} else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(334, 335, 336);
	}
}
