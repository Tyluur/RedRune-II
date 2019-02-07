package org.redrune.game.content.entity.actor.player.event.item;

import org.redrune.game.GameFlags;
import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.content.entity.actor.player.skills.crafting.GemCutting;
import org.redrune.game.content.entity.actor.player.skills.crafting.GemCutting.Gem;
import org.redrune.game.content.entity.actor.player.skills.crafting.LeatherCrafting;
import org.redrune.game.content.entity.actor.player.skills.firemaking.Firemaking;
import org.redrune.game.content.entity.actor.player.skills.fletching.Fletching;
import org.redrune.game.content.entity.actor.player.skills.fletching.Fletching.Fletch;
import org.redrune.game.content.entity.actor.player.skills.herblore.Herblore;
import org.redrune.game.content.entity.item.DiceGame;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.PlayerInventory;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.item.ItemOnItemHandler;
import org.redrune.game.entity.item.ItemOnItemHandler.ItemOnItem;

import static org.redrune.networking.packet.handler.InventoryOptionsHandler.contains;

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
			player.stopAll();
			if (!player.getControllerManager().canUseItemOnItem(itemUsed, usedWith)) {
				return;
			}
			Fletch fletch = Fletching.isFletching(usedWith, itemUsed);
			if (fletch != null) {
				player.getDialogueManager().startDialogue("FletchingD", fletch);
				return;
			}
			int herblore = Herblore.isHerbloreSkill(itemUsed, usedWith);
			if (herblore > -1) {
				player.getDialogueManager().startDialogue("HerbloreD", herblore, itemUsed, usedWith);
				return;
			}
			
			if (itemUsedId == 15086 && itemUsedWithId == 15086) {
				DiceGame.rollDice2(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2072));
				return;
			}
			if (itemUsedId == 15088 && itemUsedWithId == 15088) {
				DiceGame.rollDice3(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2074));
				return;
			}
			if (itemUsedId == 15090 && itemUsedWithId == 15090) {
				DiceGame.rollDice4(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2071));
				return;
			}
			if (itemUsedId == 15092 && itemUsedWithId == 15092) {
				DiceGame.rollDice5(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2070));
				return;
			}
			if (itemUsedId == 15094 && itemUsedWithId == 15094) {
				DiceGame.rollDice5(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2073));
				return;
			}
			if (itemUsedId == 15096 && itemUsedWithId == 15096) {
				DiceGame.rollDice7(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2068));
				return;
			}
			if (itemUsedId == 15098 && itemUsedWithId == 15098) {
				DiceGame.rollDice8(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2075));
				return;
			}
			if (itemUsedId == 15100 && itemUsedWithId == 15100) {
				DiceGame.rollDice1(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2069));
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
			if (Firemaking.isFiremaking(player, itemUsed, usedWith)) {
				return;
			} else if (contains(1755, Gem.OPAL.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.OPAL);
			} else if (contains(1755, Gem.JADE.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.JADE);
			} else if (contains(1755, Gem.RED_TOPAZ.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.RED_TOPAZ);
			} else if (contains(1755, Gem.SAPPHIRE.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.SAPPHIRE);
			} else if (contains(1755, Gem.EMERALD.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.EMERALD);
			} else if (contains(1755, Gem.RUBY.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.RUBY);
			} else if (contains(1755, Gem.DIAMOND.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.DIAMOND);
			} else if (contains(1755, Gem.DRAGONSTONE.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.DRAGONSTONE);
			} else if (itemUsed.getId() == 21369 && usedWith.getId() == 4151) {
				player.getInventory().deleteItem(21369, 1);
				player.getInventory().deleteItem(4151, 1);
				player.getInventory().addItem(21371, 1);
				player.getPackets().sendGameMessage("You have succesfully combined a vine and a whip");
			} else if (itemUsed.getId() == 4151 && usedWith.getId() == 21369) {
				player.getInventory().deleteItem(21369, 1);
				player.getInventory().deleteItem(4151, 1);
				player.getInventory().addItem(21371, 1);
				player.getPackets().sendGameMessage("You have succesfully combined a whip and a vine.");
			} else if (contains(1755, Gem.ONYX.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.ONYX);
			} else {
				player.getPackets().sendGameMessage("Nothing interesting happens.");
			}
			if (GameFlags.debugMode) {
				System.out.println("Used: " + itemUsed.getId() + ", With:" + usedWith.getId());
			}
		}
	}
	
	@Override
	public EventPolicy[] policies() {
		return arguments(EventPolicy.CLOSE_INTERFACE);
	}
}
