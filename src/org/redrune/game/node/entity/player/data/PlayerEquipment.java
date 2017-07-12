package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.item.ItemsContainer;
import org.redrune.network.rs666.packet.outgoing.impl.ContainerUpdateBuilder;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.utility.rs.constant.BonusConstants;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.cache.parse.definition.ItemDefinition;
import org.redrune.network.rs666.packet.outgoing.impl.ContainerPacketBuilder;

import java.util.HashMap;

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
	 * The weight of the player's equipment
	 */
	@Getter
	@Setter
	private transient double weight;
	
	/**
	 * Sends the full container of items
	 */
	public void sendContainer() {
		player.getTransmitter().send(new ContainerPacketBuilder(94, items.toArray(), false).build(player));
		double weight = 0;
		for (Item item : items.toArray()) {
			if (item == null) {
				continue;
			}
			weight += ItemRepository.getWeight(item.getId(), true);
		}
		this.weight = weight;
		player.getTransmitter().sendWeight();
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
	 * Updates the bonuses accurately
	 */
	private void updateBonuses() {
		bonuses = new int[18];
		double weight = 0;
		for (Item item : items.toArray()) {
			if (item == null) {
				continue;
			}
			weight += ItemRepository.getWeight(item.getId(), true);
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
		this.weight = weight;
		player.getTransmitter().sendWeight();
	}
	
	/**
	 * Checks that the player is equipping a shield
	 */
	public boolean hasShield() {
		return items.get(5) != null;
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
	 * Handles the absorption of a hit
	 *
	 * @param hit
	 * 		The hit
	 */
	public void handleAbsorption(Hit hit) {
		if (hit.getDamage() >= 200) {
			if (hit.getSplat() == HitSplat.MELEE_DAMAGE) {
				int reducedDamage = hit.getDamage() * getBonus(ABSORB_MELEE_BONUS) / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaked(reducedDamage);
				}
			} else if (hit.getSplat() == HitSplat.RANGE_DAMAGE) {
				int reducedDamage = hit.getDamage() * getBonus(ABSORB_RANGE_BONUS) / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaked(reducedDamage);
				}
			} else if (hit.getSplat() == HitSplat.MAGIC_DAMAGE) {
				int reducedDamage = hit.getDamage() * getBonus(ABSORB_MAGE_BONUS) / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaked(reducedDamage);
				}
			}
		}
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
	 * Gets the id of the weapon
	 */
	public int getWeaponId() {
		return getIdInSlot(SLOT_WEAPON);
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
	 * Checks if the player's cape saves ammo
	 */
	public boolean capeSavesAmmo() {
		int capeId = getIdInSlot(SLOT_CAPE);
		String name = capeId == -1 ? "unarmed" : ItemDefinitionParser.forId(capeId).getName();
		return capeId == 20771 || name.toLowerCase().contains("ava's");
	}
	
	/**
	 * Drains the run energy, based on the weight modifier
	 */
	public void drainRunEnergy() {
		if (player.getMovement().getNextRunDirection() != -1) {
			double toLose = (0.67 + ((player.getEquipment().getWeight() + player.getInventory().getWeight()) / 50)) / 2;
			player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() - toLose);
			player.getTransmitter().refreshEnergy();
		}
	}
	
	/**
	 * Gets the skill weapon requirement of a weapon
	 *
	 * @param skill
	 * 		The skill
	 */
	public int getWeaponRequirement(int skill) {
		int weaponId = getWeaponId();
		if (weaponId == -1) {
			return 1;
		}
		ItemDefinition definition = ItemDefinitionParser.forId(weaponId);
		HashMap<Integer, Integer> requirements = definition.getWearingRequirements();
		if (requirements == null) {
			return 1;
		}
		for (int skillId : requirements.keySet()) {
			if (skillId > 24 || skillId < 0) {
				continue;
			}
			int level = requirements.get(skillId);
			if (level < 0 || level > 120) {
				continue;
			}
			if (skill == skillId) {
				return level;
			}
		}
		return 1;
	}
	
}
