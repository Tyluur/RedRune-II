package plugin.inter;

import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;
import com.rs.networking.codec.decode.WorldPacketsDecoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class FamiliarInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		
		if (interfaceId == 880) {
			if (componentId >= 7 && componentId <= 19) {
				Familiar.setLeftclickOption(player, (componentId - 7) / 2);
			} else if (componentId == 21) {
				Familiar.confirmLeftOption(player);
			} else if (componentId == 25) {
				Familiar.setLeftclickOption(player, 7);
			}
		} else if (interfaceId == 662) {
			if (player.getFamiliar() == null) {
				return true;
			}
			if (componentId == 49) {
				player.getFamiliar().call();
			} else if (componentId == 51) {
				player.getDialogueManager().startDialogue("DismissD");
			} else if (componentId == 67) {
				player.getFamiliar().takeBob();
			} else if (componentId == 69) {
				player.getFamiliar().renewFamiliar();
			} else if (componentId == 74) {
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK) {
					player.getFamiliar().setSpecial(true);
				}
				if (player.getFamiliar().hasSpecialOn()) {
					player.getFamiliar().submitSpecial(player);
				}
			}
		} else if (interfaceId == 747) {
			if (componentId == 7) {
				Familiar.selectLeftOption(player);
			} else if (player.getFamiliar() == null) {
				return true;
			}
			if (componentId == 10 || componentId == 19) {
				player.getFamiliar().call();
			} else if (componentId == 11 || componentId == 20) {
				player.getDialogueManager().startDialogue("DismissD");
			} else if (componentId == 12 || componentId == 21) {
				player.getFamiliar().takeBob();
			} else if (componentId == 13 || componentId == 22) {
				player.getFamiliar().renewFamiliar();
			} else if (componentId == 18 || componentId == 18) {
				player.getFamiliar().sendFollowerDetails();
			} else if (componentId == 17) {
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK) {
					player.getFamiliar().setSpecial(true);
				}
				if (player.getFamiliar().hasSpecialOn()) {
					player.getFamiliar().submitSpecial(player);
				}
			}
		}
		if (interfaceId == 665) {
			if (player.getFamiliar() == null || player.getFamiliar().getBob() == null) {
				return true;
			}
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("bob_isRemove");
					player.getPackets().sendRunScript(108, "Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 671) {
			if (player.getFamiliar() == null || player.getFamiliar().getBob() == null) {
				return true;
			}
			if (componentId == 27) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("bob_isRemove", Boolean.TRUE);
					player.getPackets().sendRunScript(108, "Enter Amount:");
				}
			} else if (componentId == 29) {
				player.getFamiliar().takeBob();
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(880, 662, 747, 665, 671);
	}
}
