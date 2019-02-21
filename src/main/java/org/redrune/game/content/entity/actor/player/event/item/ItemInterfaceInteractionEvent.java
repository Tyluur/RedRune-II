package org.redrune.game.content.entity.actor.player.event.item;

import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.content.entity.actor.player.skills.crafting.LeatherCrafting;
import org.redrune.game.content.entity.actor.player.skills.herblore.Herblore;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.PlayerInventory;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.item.ItemOnItemHandler;
import org.redrune.game.entity.item.ItemOnItemHandler.ItemOnItem;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-06
 */
public class ItemInterfaceInteractionEvent extends Event {
	
	private final int interfaceId;
	
	private final int itemUsedId;
	
	private final int fromSlot;
	
	private final int interfaceId2;
	
	private final int itemUsedWithId;
	
	private final int toSlot;
	
	public ItemInterfaceInteractionEvent(int interfaceId, int itemUsedId, int fromSlot, int interfaceId2, int itemUsedWithId, int toSlot) {
		this.interfaceId = interfaceId;
		this.itemUsedId = itemUsedId;
		this.fromSlot = fromSlot;
		this.interfaceId2 = interfaceId2;
		this.itemUsedWithId = itemUsedWithId;
		this.toSlot = toSlot;
	}
	
	@Override
	public void run(Player player) {
		if ((interfaceId2 == 747 || interfaceId2 == 662) && interfaceId == PlayerInventory.INVENTORY_INTERFACE) {
			if (player.getFamiliar() != null) {
				player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.ITEM) {
					if (player.getFamiliar().hasSpecialOn()) {
						player.getFamiliar().submitSpecial(toSlot);
					}
				}
			}
			return;
		}
		
		if (interfaceId == PlayerInventory.INVENTORY_INTERFACE && interfaceId == interfaceId2 && !player.getInterfaceManager().containsInventoryInter()) {
			if (toSlot >= 28 || fromSlot >= 28) {
				return;
			}
			Item usedWith = player.getInventory().getItem(toSlot);
			Item itemUsed = player.getInventory().getItem(fromSlot);
			if (itemUsed == null || usedWith == null || itemUsed.getId() != itemUsedId || usedWith.getId() != itemUsedWithId) {
				return;
			}
			if (!player.getControllerManager().canUseItemOnItem(itemUsed, usedWith)) {
				return;
			}
			if (PluginRepository.handleItemOnItem(player, itemUsed, usedWith)) {
				return;
			}
			int herblore = Herblore.isHerbloreSkill(itemUsed, usedWith);
			if (herblore > -1) {
				player.getDialogueManager().startDialogue("HerbloreD", herblore, itemUsed, usedWith);
				return;
			}
			if (itemUsed.getId() == LeatherCrafting.NEEDLE.getId() || usedWith.getId() == LeatherCrafting.NEEDLE.getId()) {
				if (LeatherCrafting.handleItemOnItem(player, itemUsed, usedWith)) {
					return;
				}
			}
			ItemOnItem itemOnItem = ItemOnItem.forId(itemUsedId);
			if (itemOnItem != null) {
				if (itemUsedWithId == itemOnItem.getItem2()) {
					ItemOnItemHandler.handleItemOnItem(player, itemOnItem, usedWith.getId(), itemUsed.getId());
				}
				return;
			}
			player.getPackets().sendMessage("Nothing interesting happens.");
		}
	}
	
	@Override
	public EventPolicy[] policies() {
		return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK);
	}
}
