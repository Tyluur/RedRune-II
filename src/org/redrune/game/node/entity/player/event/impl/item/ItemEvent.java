package org.redrune.game.node.entity.player.event.impl.item;

import org.redrune.game.module.ModuleRepository;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.EventPolicy.AnimationPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.InterfacePolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.WalkablePolicy;
import org.redrune.game.node.entity.player.event.context.item.ItemEventContext;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.item.Item;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class ItemEvent extends Event<ItemEventContext> {
	
	@Override
	public boolean canStart(Player player) {
		return !player.getManager().getLocks().isLocked(LockType.ITEM_INTERACTION);
	}
	
	public ItemEvent(ItemEventContext context) {
		super(context);
		if (getContext().getOption() != InteractionOption.EXAMINE) {
			setInterfacePolicy(InterfacePolicy.CLOSE);
			if (context.getOption().equals(InteractionOption.DROP)) {
				setWalkablePolicy(WalkablePolicy.RESET);
				setAnimationPolicy(AnimationPolicy.RESET);
			}
		}
	}
	
	@Override
	public void run(Player player) {
		if (ModuleRepository.handle(player, getContext().getItem(), getContext().getSlotId(), getContext().getOption())) {
			return;
		}
		if (getContext().getOption().equals(InteractionOption.FIRST_OPTION)) {
			handleItemUsage(player);
		} else if (getContext().getOption().equals(InteractionOption.SECOND_OPTION)) {
			handleItemEquipping(player, getContext().getItem(), getContext().getSlotId());
		} else if (getContext().getOption().equals(InteractionOption.EXAMINE)) {
			handleItemExamining(player);
		} else if (getContext().getOption().equals(InteractionOption.DROP)) {
			handleItemDrop(player);
		}
	}
	
	/**
	 * Handles the dropping of an item
	 *
	 * @param player
	 * 		The player dropping the item
	 */
	private void handleItemDrop(Player player) {
		final Item item = getContext().getItem();
		if (!Objects.equals(player.getInventory().getItems().get(getContext().getSlotId()), item)) {
			return;
		}
		player.getInventory().deleteItem(getContext().getSlotId(), item);
		RegionManager.addFloorItem(item.getId(), item.getAmount(), 180, player.getLocation(), player.getDetails().getUsername());
	}
	
	/**
	 * Handles the usage of items
	 *
	 * @param player
	 * 		The player
	 */
	private void handleItemUsage(Player player) {
	
	}
	
	/**
	 * Handles the equipping of an item
	 *
	 * @param player
	 * 		The player
	 */
	public static void handleItemEquipping(Player player, Item item, int slotId) {
		if (item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem(player.getDetails().getAppearance().isMale()) && item.getId() != 4084) {
			player.getTransmitter().sendMessage("You can't wear that.", true);
			return;
		}
		int targetSlot = EquipConstants.getItemSlot(item.getId());
		if (item.getAmount() == 4084) {
			targetSlot = 3;
		}
		if (targetSlot == -1) {
			player.getTransmitter().sendMessage("You can't wear that.", true);
			return;
		}
		boolean isTwoHandedWeapon = targetSlot == 3 && EquipConstants.isTwoHanded(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.getTransmitter().sendMessage("Not enough free space in your inventory.", true);
			return;
		}
		HashMap<Integer, Integer> requirements = item.getDefinitions().getWearingRequirements();
		boolean hasRequirements = true;
		if (requirements != null) {
			for (int skillId : requirements.keySet()) {
				if (skillId > 24 || skillId < 0) {
					continue;
				}
				int level = requirements.get(skillId);
				if (level < 0 || level > 120) {
					continue;
				}
				if (player.getSkills().getLevelForXp(skillId) < level) {
					if (hasRequirements) {
						player.getTransmitter().sendMessage("You are not high enough level to use this item.", true);
					}
					hasRequirements = false;
					String name = SkillConstants.SKILL_NAME[skillId].toLowerCase();
					player.getTransmitter().sendMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".", true);
				}
			}
		}
		if (!hasRequirements) {
			return;
		}
		player.getInventory().getItems().remove(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(5))) {
					player.getInventory().getItems().set(slotId, item);
					return;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null && EquipConstants.isTwoHanded(player.getEquipment().getItem(3))) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(3))) {
					player.getInventory().getItems().set(slotId, item);
					return;
				}
				player.getEquipment().getItems().set(3, null);
			}
		}
		if (player.getEquipment().getItem(targetSlot) != null && (item.getId() != player.getEquipment().getItem(targetSlot).getId() || !item.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			} else {
				player.getInventory().getItems().add(new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			}
			player.getEquipment().getItems().set(targetSlot, null);
		}
		/*if (targetSlot == Equipment.SLOT_AURA) {
			player.getAuraManager().removeAura();
		}*/
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(item.getId(), oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : 3);
		player.getEquipment().sendContainer();
		player.getInventory().refreshAll();
		player.getUpdateMasks().register(new AppearanceUpdate(player));
		/*if (targetSlot == 3) {
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		}*/
		//		ItemConstants.handleItemEquip(player, item2);
	}
	
	/**
	 * The examining of an item is sent here
	 *
	 * @param player
	 * 		The player
	 */
	private void handleItemExamining(Player player) {
		player.getTransmitter().sendMessage("Item examine to send: " + getContext().getItem(), true);
	}
}
