package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.item.ItemsContainer;
import org.redrune.network.rs666.packet.outgoing.impl.ContainerPacketBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.ContainerUpdateBuilder;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.utility.rs.constant.BonusConstants;
import org.redrune.utility.rs.constant.EquipConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class PlayerEquipment implements EquipConstants, BonusConstants {
	
	/**
	 * The container of items
	 */
	@Getter
	private final ItemsContainer<Item> items = new ItemsContainer<>(15, false);
	
	/**
	 * The bonuses of the player
	 */
	private int[] bonuses = new int[18];
	
	/**
	 * The player
	 */
	@Setter
	private transient Player player;
	
	/**
	 * Sends the full container of items
	 */
	public void sendContainer() {
		player.getTransmitter().send(new ContainerPacketBuilder(94, items.toArray(), false).build(player));
	}
	
	/**
	 * Refreshes an array of slots
	 *
	 * @param slots
	 * 		The slots
	 */
	public void refresh(int... slots) {
		if (slots != null) {
			player.getTransmitter().send(new ContainerUpdateBuilder(94, items.toArray(), slots).build(player));
		}
		updateBonuses();
	}
	
	/**
	 * Gets the id of the item in the slot
	 *
	 * @param slot
	 * 		The slot
	 */
	public int getIdInSlot(int slot) {
		Item item = getItem(slot);
		if (item == null) {
			return -1;
		} else {
			return item.getId();
		}
	}
	
	/**
	 * Gets an item in the slot
	 *
	 * @param slot
	 * 		The slot
	 */
	public Item getItem(int slot) {
		return items.get(slot);
	}
	
	/**
	 * Checks that the player is equipping a shield
	 */
	public boolean hasShield() {
		return items.get(5) != null;
	}
	
	/**
	 * Gets the bonus at an index
	 *
	 * @param index
	 * 		The index
	 */
	public int getBonus(int index) {
		if (index < 0 || index >= bonuses.length) {
			System.out.println("Invalid bonus index expected: " + index);
			return 0;
		}
		return bonuses[index];
	}
	
	/**
	 * Gets the render emote of the weapon
	 */
	public int getWeaponRenderEmote() {
		Item weapon = items.get(3);
		if (weapon == null) {
			return 1426;
		}
		if (weapon.getId() == 4565) {
			return 594;
		}
		return weapon.getDefinitions().getRenderAnimId();
	}
	
	/**
	 * Updates the bonuses accurately
	 */
	private void updateBonuses() {
		bonuses = new int[18];
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null) {
				continue;
			}
			int[] bonuses = ItemRepository.getBonuses(item.getId());
			if (bonuses == null) {
				continue;
			}
			for (int id = 0; id < bonuses.length; id++) {
				if (id == RANGED_STRENGTH_BONUS && this.bonuses[RANGED_STRENGTH_BONUS] != 0) {
					continue;
				}
				this.bonuses[id] += bonuses[id];
			}
		}
	}
}
