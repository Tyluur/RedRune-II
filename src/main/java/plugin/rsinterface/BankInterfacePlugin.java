package plugin.rsinterface;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.utility.constants.PacketConstants;
import org.redrune.utility.game.InputEvent;
import org.redrune.utility.game.InputEvent.InputEventType;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class BankInterfacePlugin implements InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == 762) {
			if (componentId == 117) {
				return true;
			}
			if (componentId == 15) {
				player.getBank().switchInsertItems();
			} else if (componentId == 19) {
				player.getBank().switchWithdrawNotes();
			} else if (componentId == 33) {
				player.getBank().depositAllInventory(true);
			} else if (componentId == 35) {
				player.getBank().depositAllEquipment(true);
			} else if (componentId == 44) {
				player.closeInterfaces();
				player.getInterfaceManager().sendInterface(767);
				player.setCloseInterfacesEvent(() -> player.getBank().openBank());
			} else if (componentId >= 44 && componentId <= 62) {
				int tabId = 9 - ((componentId - 44) / 2);
				if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
					player.getBank().setCurrentTab(tabId);
				} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					player.getBank().collapse(tabId);
				}
			} else if (componentId == 93) {
				if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
					player.getBank().withdrawItem(slotId, 1);
				} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					player.getBank().withdrawItem(slotId, 5);
				} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
					player.getBank().withdrawItem(slotId, 10);
				} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
					player.getBank().withdrawLastAmount(slotId);
				} else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
					player.getPackets().requestClientInput(new InputEvent("Enter Amount:", InputEventType.INTEGER) {
						@Override
						public void handleInput() {
							player.getBank().withdrawItem(slotId, getInput());
						}
					});
					player.getPackets().sendRunScript(108, "Enter Amount:");
				} else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
					player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == PacketConstants.ACTION_BUTTON6_PACKET) {
					player.getBank().withdrawItemButOne(slotId);
				} else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
					player.getBank().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 763) {
			if (componentId == 0) {
				if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
					player.getBank().depositItem(slotId, 1, true);
				} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					player.getBank().depositItem(slotId, 5, true);
				} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
					player.getBank().depositItem(slotId, 10, true);
				} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
					player.getBank().depositLastAmount(slotId);
				} else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
					player.getPackets().requestClientInput(new InputEvent("Enter Amount:", InputEventType.INTEGER) {
						@Override
						public void handleInput() {
							player.getBank().depositItem(slotId, getInput(), true);
						}
					});
				} else if (packetId == PacketConstants.ACTION_BUTTON9_PACKET) {
					player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
				} else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 767) {
			if (componentId == 10) {
				player.getBank().openBank();
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(767, 763, 762);
	}
}
