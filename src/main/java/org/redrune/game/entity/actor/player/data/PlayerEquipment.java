package org.redrune.game.entity.actor.player.data;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.content.entity.actor.player.skills.runecrafting.Runecrafting;
import org.redrune.game.entity.actor.data.CombatDefinitions;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.item.ItemsContainer;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

import static org.redrune.utility.constants.EquipmentConstants.*;

public final class PlayerEquipment implements Serializable {
	
	private static final long serialVersionUID = -4147163237095647617L;
	
	private ItemsContainer<Item> items;
	
	@Setter
	private transient Player player;
	
	@Getter
	@Setter
	private transient int equipmentHpIncrease;
	
	public PlayerEquipment() {
		items = new ItemsContainer<>(15, false);
	}
	
	/**
	 * Requests the equip of multiple slots [in your inventory]
	 *
	 * @param player
	 * 		The player
	 * @param slotIds
	 * 		The slot ids to equip
	 */
	public static void equipMultipleSlots(Player player, int[] slotIds) {
		if (player.isFinished() || player.isDead() || player.getLocks().isEquipmentLocked()) {
			return;
		}
		boolean worn = false;
		Item[] copy = player.getInventory().getItems().getItemsCopy();
		for (int slotId : slotIds) {
			Item item = player.getInventory().getItem(slotId);
			if (item == null) {
				continue;
			}
			if (equipItem(player, slotId, item.getId())) {
				worn = true;
			}
		}
		player.getInventory().refreshItems(copy);
		if (worn) {
			player.getAppearance().generateAppearanceData();
			player.getPackets().sendSound(2240, 0, 1);
		}
	}
	
	/**
	 * Equips an item
	 *
	 * @param player
	 * 		The player
	 * @param slotId
	 * 		The id of the slot the item is in
	 * @param itemId
	 * 		The id of the item
	 */
	public static boolean equipItem(Player player, int slotId, int itemId) {
		if (player.isFinished() || player.isDead() || player.getLocks().isEquipmentLocked()) {
			return false;
		}
		Item item = player.getInventory().getItem(slotId);
		if (item == null || item.getId() != itemId) {
			return false;
		}
		if (item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem(player.getAppearance().isMale())) {
			player.getPackets().sendMessage("You can't wear that.");
			return false;
		}
		int targetSlot = getItemSlot(itemId);
		if (targetSlot == -1) {
			player.getPackets().sendMessage("You can't wear that.");
			return false;
		}
		boolean isTwoHandedWeapon = targetSlot == 3 && isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.getPackets().sendMessage("Not enough free space in your inventory.");
			return false;
		}
		HashMap<Integer, Integer> skillRequirements = item.getDefinitions().getWearingSkillRequirements();
		boolean hasRequirements = true;
		if (skillRequirements != null) {
			for (int skillId : skillRequirements.keySet()) {
				if (skillId > 24 || skillId < 0) {
					continue;
				}
				int level = skillRequirements.get(skillId);
				if (level < 0 || level > 120) {
					continue;
				}
				if (player.getSkills().getLevelForXp(skillId) < level) {
					if (hasRequirements) {
						player.getPackets().sendMessage("You are not high enough level to use this item.");
					}
					hasRequirements = false;
					String name = SkillConstants.SKILL_NAME[skillId].toLowerCase();
					player.getPackets().sendMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".");
				}
				
			}
		}
		if (!hasRequirements) {
			return false;
		}
		if (!player.getControllerManager().canEquip(targetSlot, itemId)) {
			return false;
		}
		player.getInventory().getItems().remove(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(5))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null && isTwoHandedWeapon(player.getEquipment().getItem(3))) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(3))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(3, null);
			}
			
		}
		if (player.getEquipment().getItem(targetSlot) != null && (itemId != player.getEquipment().getItem(targetSlot).getId() || !item.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			} else {
				player.getInventory().getItems().add(new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			}
			player.getEquipment().getItems().set(targetSlot, null);
		}
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : 3);
		if (targetSlot == 3) {
			player.getCombatDefinitions().decreaseSpecialEnergy(0);
		}
		player.getCharges().wear(targetSlot);
		return true;
	}
	
	public boolean hasShield() {
		return items.get(5) != null;
	}
	
	public Item getItem(int slot) {
		return items.get(slot);
	}
	
	public ItemsContainer<Item> getItems() {
		return items;
	}
	
	public void refresh(int... slots) {
		if (slots != null) {
			player.getPackets().sendUpdateItems(94, items, slots);
			player.getCombatDefinitions().checkAttackStyle();
		}
		player.getCombatDefinitions().refreshBonuses();
		refreshConfigs(slots == null);
	}
	
	public void refreshConfigs(boolean init) {
		double hpIncrease = 0;
		for (int index = 0; index < items.getSize(); index++) {
			Item item = items.get(index);
			if (item == null) {
				continue;
			}
			int id = item.getId();
			if (index == SLOT_HAT) {
				if (id == 20135 || id == 20137 || id == 20147 || id == 20149 || id == 20159 || id == 20161) {
					hpIncrease += 66;
				} else if (id == Runecrafting.AIR_TIARA) {
					player.getPackets().sendConfig(491, 1);
				} else if (id == Runecrafting.EARTH_TIARA) {
					player.getPackets().sendConfig(491, 8);
				} else if (id == Runecrafting.FIRE_TIARA) {
					player.getPackets().sendConfig(491, 16);
				} else if (id == Runecrafting.WATER_TIARA) {
					player.getPackets().sendConfig(491, 4);
				} else if (id == Runecrafting.BODY_TIARA) {
					player.getPackets().sendConfig(491, 32);
				} else if (id == Runecrafting.MIND_TIARA) {
					player.getPackets().sendConfig(491, 2);
				} else if (id == Runecrafting.OMNI_TIARA) {
					player.getPackets().sendConfig(491, -1);
				}
			} else if (index == SLOT_CHEST) {
				if (id == 20139 || id == 20141 || id == 20151 || id == 20153 || id == 20163 || id == 20165) {
					hpIncrease += 200;
				}
			} else if (index == SLOT_LEGS) {
				if (id == 20143 || id == 20145 || id == 20155 || id == 20157 || id == 20167 || id == 20169) {
					hpIncrease += 134;
				}
			}
		}
		if (hpIncrease != equipmentHpIncrease) {
			equipmentHpIncrease = (int) hpIncrease;
			if (!init) {
				player.refreshHitPoints();
			}
		}
	}
	
	public static void refreshEquipBonuses(Player player) {
		player.getPackets().sendIComponentText(667, 31, "Stab: +" + player.getCombatDefinitions().getBonuses()[0]);
		player.getPackets().sendIComponentText(667, 32, "Slash: +" + player.getCombatDefinitions().getBonuses()[1]);
		player.getPackets().sendIComponentText(667, 33, "Crush: +" + player.getCombatDefinitions().getBonuses()[2]);
		player.getPackets().sendIComponentText(667, 34, "Magic: +" + player.getCombatDefinitions().getBonuses()[3]);
		player.getPackets().sendIComponentText(667, 35, "Range: +" + player.getCombatDefinitions().getBonuses()[4]);
		player.getPackets().sendIComponentText(667, 36, "Stab: +" + player.getCombatDefinitions().getBonuses()[5]);
		player.getPackets().sendIComponentText(667, 37, "Slash: +" + player.getCombatDefinitions().getBonuses()[6]);
		player.getPackets().sendIComponentText(667, 38, "Crush: +" + player.getCombatDefinitions().getBonuses()[7]);
		player.getPackets().sendIComponentText(667, 39, "Magic: +" + player.getCombatDefinitions().getBonuses()[8]);
		player.getPackets().sendIComponentText(667, 40, "Range: +" + player.getCombatDefinitions().getBonuses()[9]);
		player.getPackets().sendIComponentText(667, 41, "Summoning: +" + player.getCombatDefinitions().getBonuses()[10]);
		player.getPackets().sendIComponentText(667, 42, "Absorve Melee: " + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 43, "Absorve Magic: +" + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 44, "Absorve Ranged: +" + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 45, "Strength: " + player.getCombatDefinitions().getBonuses()[14]);
		player.getPackets().sendIComponentText(667, 46, "Ranged Str: " + player.getCombatDefinitions().getBonuses()[15]);
		player.getPackets().sendIComponentText(667, 47, "Prayer: +" + player.getCombatDefinitions().getBonuses()[16]);
		player.getPackets().sendIComponentText(667, 48, "Magic Damage: +" + player.getCombatDefinitions().getBonuses()[17] + "%");
	}
	
	public void reset() {
		items.reset();
		init();
	}
	
	public void init() {
		player.getPackets().sendItems(94, items);
		refresh(null);
	}
	
	public void sendExamine(int slotId) {
		Item item = items.get(slotId);
		if (item == null) {
			return;
		}
		player.getPackets().sendMessage(ItemCharacteristicRepository.getExamine(item.getId()));
	}
	
	public boolean hasTwoHandedWeapon() {
		Item item = items.get(SLOT_WEAPON);
		return item != null && isTwoHandedWeapon(item);
	}
	
	public int getWeaponRenderEmote() {
		Item weapon = items.get(3);
		if (weapon == null) {
			return 1426;
		}
		return weapon.getDefinitions().getRenderAnimId();
	}
	
	public int getAmuletId() {
		return getIdInSlot(SLOT_AMULET);
	}
	
	public int getChestId() {
		return getIdInSlot(SLOT_CHEST);
	}
	
	public int getHatId() {
		return getIdInSlot(SLOT_HAT);
	}
	
	public int getShieldId() {
		return getIdInSlot(SLOT_SHIELD);
	}
	
	public int getLegsId() {
		return getIdInSlot(SLOT_LEGS);
	}
	
	public void removeAmmo(int ammoId, int amount) {
		if (amount == -1) {
			items.remove(SLOT_WEAPON, new Item(ammoId, 1));
			refresh(SLOT_WEAPON);
		} else {
			items.remove(SLOT_ARROWS, new Item(ammoId, amount));
			refresh(SLOT_ARROWS);
		}
		player.getAppearance().generateAppearanceData();
	}
	
	public int getAuraId() {
		Item item = items.get(SLOT_AURA);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}
	
	public int getCapeId() {
		Item item = items.get(SLOT_CAPE);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}
	
	public int getRingId() {
		Item item = items.get(SLOT_RING);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}
	
	public int getAmmoId() {
		Item item = items.get(SLOT_ARROWS);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}
	
	public void deleteItem(int itemId, int amount) {
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}
	
	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index]) {
				changedSlots[count++] = index;
			}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}
	
	public int getBootsId() {
		Item item = items.get(SLOT_FEET);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}
	
	public int getGlovesId() {
		Item item = items.get(SLOT_HANDS);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}
	
	public int getEquipmentHpIncrease() {
		return equipmentHpIncrease;
	}
	
	public void setEquipmentHpIncrease(int hp) {
		this.equipmentHpIncrease = hp;
	}
	
	public boolean isWearingArmour() {
		return getItem(SLOT_HAT) != null || getItem(SLOT_CAPE) != null || getItem(SLOT_AMULET) != null || getItem(SLOT_WEAPON) != null || getItem(SLOT_CHEST) != null || getItem(SLOT_SHIELD) != null || getItem(SLOT_LEGS) != null || getItem(SLOT_HANDS) != null || getItem(SLOT_FEET) != null || getItem(SLOT_RING) != null;
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
		ItemDefinitions definition = ItemDefinitions.getItemDefinitions(weaponId);
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
