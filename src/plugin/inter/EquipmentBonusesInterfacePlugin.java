package plugin.inter;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerEquipment;
import com.rs.game.entity.item.Item;
import com.rs.game.plugin.type.InterfacePlugin;
import com.rs.networking.codec.decode.WorldPacketsDecoder;
import com.rs.utility.constants.EquipmentConstants;
import com.rs.utility.repo.item.ItemCharacteristicRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/30/2017
 */
public class EquipmentBonusesInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		switch (interfaceId) {
			case 387:
				if (componentId == 39) {
					if (player.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendGameMessage("Please finish what you're doing before opening your equipment bonuses.");
						return true;
					}
					displayEquipmentBonuses(player);
				}
				if (componentId == 42) {
					if (player.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendGameMessage("Please finish what you're doing before opening the price checker.");
						return true;
					}
					player.stopAll();
					player.getPriceCheckManager().initPriceCheck();
				}
				if (componentId == 45) {
					if (player.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendGameMessage("Please finish what you're doing before opening your items kept on death.");
						return true;
					}
					player.stopAll();
					player.getInterfaceManager().sendInterface(17);
				}
				// Head Gear
				if (componentId == 8 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_HAT);
				} else if (componentId == 8 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_HAT);
				}
				// Weapons
				else if (componentId == 17 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_WEAPON);
				} else if (componentId == 17 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_WEAPON);
				}
				// Chest Gear
				else if (componentId == 20 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_CHEST);
				} else if (componentId == 20 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_CHEST);
				}
				// Shield Gear
				else if (componentId == 23 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_SHIELD);
				} else if (componentId == 23 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_SHIELD);
				}
				// Leg Gear
				else if (componentId == 26 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_LEGS);
				} else if (componentId == 26 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_LEGS);
				}
				// Gloves Gear
				else if (componentId == 29 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_HANDS);
				} else if (componentId == 29 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_HANDS);
				}
				// Feet Gear
				else if (componentId == 32 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_FEET);
				} else if (componentId == 32 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_FEET);
				}
				// Rings
				else if (componentId == 35 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_RING);
				} else if (componentId == 35 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_RING);
				}
				// Arrows
				else if (componentId == 38 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_ARROWS);
				} else if (componentId == 38 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_ARROWS);
				}
				// Amulets
				else if (componentId == 14 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_AMULET);
				} else if (componentId == 14 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_AMULET);
				}
				// Capes
				else if (componentId == 11 && packetId == 61) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_CAPE);
				} else if (componentId == 11 && packetId == 25) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_CAPE);
				}
				break;
			case 670:
				if (componentId == 0) {
					if (slotId >= player.getInventory().getItemsContainerSize()) {
						return true;
					}
					Item item = player.getInventory().getItem(slotId);
					if (item == null) {
						return true;
					}
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
						if (EquipmentConstants.sendWear(player, slotId, item.getId())) {
							PlayerEquipment.refreshEquipBonuses(player);
							player.getPackets().sendGlobalConfig(779, player.getEquipment().getWeaponRenderEmote());
						}
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
						showStats(player, item);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
						player.getInventory().sendExamine(slotId);
					}
				}
				break;
			case 667:
				if (componentId == 7) {
					if (slotId >= 14) {
						return true;
					}
					Item item = player.getEquipment().getItem(slotId);
					if (item == null) {
						return true;
					}
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
						EquipmentConstants.sendRemove(player, slotId);
						PlayerEquipment.refreshEquipBonuses(player);
						player.getPackets().sendGlobalConfig(779, player.getEquipment().getWeaponRenderEmote());
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON10_PACKET) {
						showStats(player, item);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
						player.getPackets().sendGameMessage(ItemCharacteristicRepository.getExamine(item.getId()));
					}
				}
				break;
		}
		return true;
	}
	
	/**
	 * Displays the equipment bonuses interface
	 *
	 * @param player
	 * 		The player
	 */
	private void displayEquipmentBonuses(Player player) {
		// sent twice because of the bank glitch
		player.stopAll();
		for (int i = 0; i < 2; i++) {
			player.getPackets().sendGlobalConfig(779, player.getEquipment().getWeaponRenderEmote());
			player.getInterfaceManager().sendInventoryInterface(670);
			player.getPackets().sendInterSetItemsOptionsScript(670, 0, 93, 4, 7, "Equip", "Compare", "Stats", "Examine");
			player.getPackets().sendUnlockIComponentOptionSlots(670, 0, 0, 27, 0, 1, 2, 3);
			player.getInterfaceManager().sendInterface(667);
			player.getPackets().sendIComponentSettings(667, 7, 0, 15, 1538);
			//			player.getPackets().sendIComponentSettings(667, 7, 0, 15, 1030);
			//			player.getPackets().sendIComponentSettings(667, 14, 0, 15, 1030);
			PlayerEquipment.refreshEquipBonuses(player);
		}
	}
	
	/**
	 * Shows the stats of the item
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item
	 */
	private static void showStats(Player player, Item item) {
		int[] bonuses = ItemCharacteristicRepository.getBonuses(item.getId());
		if (bonuses == null) {
			bonuses = new int[18];
		}

		StringBuilder titles = new StringBuilder();
		StringBuilder names = new StringBuilder();
		StringBuilder stats = new StringBuilder();

		String namesArray[] = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", "Summoning", "Absorb Melee", "Absorb Magic", "Absorb Range", "Strength", "Ranged Str", "Prayer", "Magic Damage" };
		int count = 0;
		boolean title1Done = false;
		boolean title2Done = false;
		boolean title3Done = false;
		boolean namesIndentDone = false;

		for (int i = 0; i < bonuses.length; i++) {
			if (bonuses[i] != 0) {
				if (i <= 4 && !title1Done) {
					title1Done = true;
					titles.append("Attack Bonus");
					titles.append("                 ");
					stats.append("<br>");
					names.append("<br>");
				} else if (i >= 5 && i <= 13 && !title2Done) {
					for (int j = 0; j <= count; j++) {
						if (title1Done) {
							titles.append("<br>");
						}
					}
					count = 0;
					title2Done = true;
					titles.append("Defence Bonus");
					titles.append("                 ");
					stats.append("<br>");
					names.append("<br>");
				} else if (i >= 14 && !title3Done) {
					for (int j = 0; j <= count; j++) {
						titles.append("<br>");
					}
					count = 0;
					title3Done = true;
					titles.append("Other");
					titles.append("                 ");
					stats.append("<br>");
					names.append("<br>");
				}
				names.append(namesArray[i]).append(":");
				if (!namesIndentDone) {
					namesIndentDone = true;
					names.append("                       ");
				}
				names.append("<br>");
				stats.append(bonuses[i] > 0 ? "+" : "").append(bonuses[i]);
				stats.append("<br>");
				count++;
			}
		}
		player.getPackets().sendGlobalString(321, item.getName());
		player.getPackets().sendGlobalString(324, stats.toString());
		player.getPackets().sendGlobalString(323, names.toString());
		player.getPackets().sendGlobalString(322, titles.toString());
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(387, 667, 670);
	}
}
