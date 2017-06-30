package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.impl.item.ItemEvent;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.item.Item;
import org.redrune.network.NetworkConstants;
import org.redrune.network.rs666.packet.outgoing.impl.AccessMaskBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.CS2ConfigBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.CS2StringBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.InterfaceChangeBuilder;
import org.redrune.utility.repository.item.ItemRepository;

import static org.redrune.utility.rs.constant.BonusConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public class BonusesInterfaceInteractionModule implements InterfaceInteractionModule {
	
	/**
	 * The data used to write bonuses on the equipment bonuses interface.
	 */
	private static final Object[][] BONUSES_INTERFACE_DATA = new Object[][] {
			// ATTACK
			{ 31, STAB_ATTACK, "Stab" }, { 32, SLASH_ATTACK, "Slash" }, { 33, CRUSH_ATTACK, "Crush" }, { 34, MAGIC_ATTACK, "Magic" }, { 35, RANGE_ATTACK, "Range" },
			// DEFENCE
			{ 36, STAB_DEFENCE, "Stab" }, { 37, SLASH_DEFENCE, "Slash" }, { 38, CRUSH_DEFENCE, "Crush" }, { 39, MAGIC_DEFENCE, "Magic" }, { 40, RANGE_DEFENCE, "Range" }, { 41, SUMMONING_DEFENCE, "Summoning" },
			// ABSORB
			{ 42, ABSORB_MELEE_BONUS, "Absorb Melee" }, { 43, ABSORB_MAGE_BONUS, "Absorb Magic" }, { 44, ABSORB_RANGE_BONUS, "Absorb Range" },
			// STRENGTHS
			{ 45, STRENGTH_BONUS, "Strength" }, { 46, RANGED_STRENGTH_BONUS, "Ranged Strength" }, { 47, PRAYER_BONUS, "Prayer" }, { 48, MAGIC_DAMAGE_BONUS, "Magic Damage" } };
	
	/**
	 * The id of the interface
	 */
	private static final int INTERFACE_ID = 667;
	
	/**
	 * The id of the inventory interface
	 */
	private static final int INVENTORY_INTERFACE_ID = 670;
	
	/**
	 * The bonus labels
	 */
	private static final String[] BONUS_LABELS = new String[] { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", "Summoning", "Absorb Melee", "Absorb Magic", "Absorb Ranged", "Strength", "Ranged Str", "Prayer", "Magic Damage" };
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(INTERFACE_ID, INVENTORY_INTERFACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == INTERFACE_ID) {
			if (componentId == 7) {
				Item item = player.getEquipment().getItems().lookup(itemId);
				if (item == null || item.getId() != itemId) {
					return true;
				}
				if (!player.getInventory().getItems().hasSpaceFor(item)) {
					player.getTransmitter().sendMessage("You do not have enough space in your inventory.");
					return true;
				}
				if (packetId == NetworkConstants.FIRST_PACKET_ID) {
					player.getEquipment().getItems().remove(item);
					player.getEquipment().refresh(slotId);
					player.getInventory().addItem(item.getId(), item.getAmount());
					player.getUpdateMasks().register(new AppearanceUpdate(player));
					refresh(player);
					return true;
				} else if (packetId == NetworkConstants.SEVENTH_PACKET_ID) {
					showStats(player, item);
					return true;
				} else if (packetId == NetworkConstants.EXAMINE_PACKET_ID) {
					ItemEvent.handleItemExamining(player, item);
					return true;
				}
			} else if (componentId == 65) {
				// close button
				return true;
			}
		} else {
			if (componentId == 0) {
				Item item = player.getInventory().getItems().get(slotId);
				if (item == null || item.getId() != itemId) {
					return true;
				}
				if (packetId == NetworkConstants.FIRST_PACKET_ID) {
					ItemEvent.handleItemEquipping(player, item, slotId);
					refresh(player);
					return true;
				} else if (packetId == NetworkConstants.SEVENTH_PACKET_ID) {
					showStats(player, item);
					return true;
				} else if (packetId == NetworkConstants.EXAMINE_PACKET_ID) {
					ItemEvent.handleItemExamining(player, item);
					return true;
				}
			}
		}
		System.out.println("player = [" + player + "], interfaceId = [" + interfaceId + "], componentId = [" + componentId + "], itemId = [" + itemId + "], slotId = [" + slotId + "], packetId = [" + packetId + "]");
		return true;
	}
	
	/**
	 * Refreshes the interface
	 *
	 * @param player
	 * 		The player
	 */
	private static void refresh(Player player) {
		for (Object[] element : BONUSES_INTERFACE_DATA) {
			int bonus = player.getEquipment().getBonus((int) element[1]);
			String sign = bonus > 0 ? "+" : "";
			player.getManager().getInterfaces().sendInterfaceText(INTERFACE_ID, (int) element[0], element[2] + ": " + sign + bonus);
		}
		player.getTransmitter().send(new CS2ConfigBuilder(779, player.getEquipment().getWeaponRenderEmote()).build(player));
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
		int[] bonuses = ItemRepository.getBonuses(item.getId());
		if (bonuses == null) {
			bonuses = new int[18];
		}
		StringBuilder attack = new StringBuilder();
		attack.append("Attack Bonuses<br><br>");
		StringBuilder defence = new StringBuilder();
		defence.append("Defence Bonuses<br><br>");
		StringBuilder other = new StringBuilder();
		other.append("Other Bonuses<br><br>");
		for (int i = 0; i < bonuses.length; i++) {
			double bonus = bonuses[i];
			String label = BONUS_LABELS[i];
			StringBuilder bldr = (i <= 4 ? attack : i <= 13 ? defence : other);
			String sign = bonus > 0 ? "+" : "";
			
			bldr.append(label).append(": ").append(sign).append(bonus).append(label.contains("Absorb") ? "%" : "").append(i == bonuses.length - 1 ? "" : "<br>");
		}
		other.append("<br>Weight: ").append(ItemRepository.getWeight(item.getId(), true));
		player.getTransmitter().send(new CS2StringBuilder(321, "Stats for " + item.getName()).build(player));
		player.getTransmitter().send(new CS2StringBuilder(323, attack.toString()).build(player));
		player.getTransmitter().send(new CS2StringBuilder(324, defence.toString()).build(player));
		player.getTransmitter().send(new CS2StringBuilder(325, other.toString()).build(player));
	}
	
	/**
	 * Shows the interface to the player
	 *
	 * @param player
	 * 		The player
	 */
	public static void show(Player player) {
		// sent twice because of the bank glitch
		for (int i = 0; i < 2; i++) {
			player.stop(true, true, true, true);
			player.getTransmitter().send(new AccessMaskBuilder(INTERFACE_ID, 7, 0, 15, 1538).build(player));
			player.getTransmitter().send(new AccessMaskBuilder(INVENTORY_INTERFACE_ID, 0, 0, 28, 1538).build(player));
			player.getTransmitter().send(new InterfaceChangeBuilder(INTERFACE_ID, 49, true).build(player));
			player.getManager().getInterfaces().sendInterface(INTERFACE_ID, true).sendInventoryInterface(INVENTORY_INTERFACE_ID);
			refresh(player);
		}
	}
	
}
