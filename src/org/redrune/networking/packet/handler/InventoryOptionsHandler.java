package org.redrune.networking.packet.handler;

import org.redrune.game.GameFlags;
import org.redrune.game.content.combat.function.Magic;
import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer;
import org.redrune.game.content.entity.actor.player.skills.firemaking.Firemaking;
import org.redrune.game.content.entity.actor.player.skills.herblore.HerbCleaning;
import org.redrune.game.content.entity.actor.player.skills.hunter.Hunter;
import org.redrune.game.content.entity.actor.player.skills.hunter.Hunter.HunterEquipment;
import org.redrune.game.content.entity.actor.player.skills.runecrafting.Runecrafting;
import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning;
import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.content.entity.item.*;
import org.redrune.game.content.entity.item.Burying.Bone;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.PlayerEquipment;
import org.redrune.game.entity.actor.player.data.RouteEvent;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.EquipmentConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository;

public class InventoryOptionsHandler {
	
	public static void handleItemOption1(Player player, final int slotId, final int itemId, Item item) {
		long time = Misc.currentTimeMillis();
		if (player.getLocks().isInteractionLocked() || player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		player.stopAll(false);
		if (Foods.eat(player, item, slotId)) {
			return;
		}
		if (Burying.bury(player, slotId)) {
			return;
		}
		if (itemId == 8013) {
			player.getInventory().deleteItem(8013, 1);
			player.setNextAnimation(new Animation(9597));
			player.setNextGraphics(new Graphics(1680));
		}
		if (itemId == 15098) {
			DiceGame.rollDice8(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2075));
			return;
		}
		if (itemId == 15086) {
			DiceGame.rollDice2(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2072));
			return;
		}
		if (itemId == 15088) {
			DiceGame.rollDice3(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2074));
			return;
		}
		if (itemId == 15090) {
			DiceGame.rollDice4(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2071));
			return;
		}
		if (itemId == 15092) {
			DiceGame.rollDice5(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2070));
			return;
		}
		if (itemId == 15094) {
			DiceGame.rollDice5(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2073));
			return;
		}
		if (itemId == 15096) {
			DiceGame.rollDice7(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2068));
			return;
		}
		if (itemId == 15100) {
			DiceGame.rollDice1(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2069));
			return;
		}
		
		if (!player.getControllerManager().handleItemOption1(player, slotId, itemId, item)) {
			return;
		}
		if (Pots.pot(player, item, slotId)) {
			return;
		}
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509) {
				pouch = 0;
			}
			if (itemId == 5510) {
				pouch = 1;
			}
			if (itemId == 5512) {
				pouch = 2;
			}
			if (itemId == 5514) {
				pouch = 3;
			}
			Runecrafting.fillPouch(player, pouch);
			return;
		}
		if (HerbCleaning.clean(player, item, slotId)) {
			return;
		}
		Bone bone = Bone.forId(itemId);
		if (bone != null) {
			Bone.bury(player, slotId);
			return;
		}
		if (Magic.useTabTeleport(player, itemId)) {
			return;
		}
		if (itemId == AncientEffigies.SATED_ANCIENT_EFFIGY || itemId == AncientEffigies.GORGED_ANCIENT_EFFIGY || itemId == AncientEffigies.NOURISHED_ANCIENT_EFFIGY || itemId == AncientEffigies.STARVED_ANCIENT_EFFIGY) {
			player.getDialogueManager().startDialogue("AncientEffigiesD", itemId);
		} else if (itemId == 4155) {
			player.getDialogueManager().startDialogue("EnchantedGemDialouge");
		} else if (itemId == HunterEquipment.BOX.getId()) {
			player.getActionManager().setAction(new Hunter(HunterEquipment.BOX));
		} else if (itemId == HunterEquipment.BRID_SNARE.getId()) {
			player.getActionManager().setAction(new Hunter(HunterEquipment.BRID_SNARE));
		} else {
			player.getPackets().sendGameMessage("Nothing interesting happens...");
			if (GameFlags.debugMode) {
				System.out.println("Item Select:" + itemId + ", Slot Id:" + slotId);
			}
		}
	}
	
	public static void handleItemOption2(final Player player, final int slotId, final int itemId, Item item) {
		if (Firemaking.isFiremaking(player, itemId)) {
			return;
		}
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509) {
				pouch = 0;
			}
			if (itemId == 5510) {
				pouch = 1;
			}
			if (itemId == 5512) {
				pouch = 2;
			}
			if (itemId == 5514) {
				pouch = 3;
			}
			Runecrafting.emptyPouch(player, pouch);
			player.stopAll(false);
		} else {
			if (player.getAttributes().isEquipDisabled()) {
				return;
			}
			player.stopAll(false);
			PlayerEquipment.equipMultipleSlots(player, new int[] { slotId });
		}
	}
	
	public static Item contains(int id1, Item item1, Item item2) {
		if (item1.getId() == id1) {
			return item2;
		}
		if (item2.getId() == id1) {
			return item1;
		}
		return null;
	}
	
	public static boolean contains(int id1, int id2, Item... items) {
		boolean containsId1 = false;
		boolean containsId2 = false;
		for (Item item : items) {
			if (item.getId() == id1) {
				containsId1 = true;
			} else if (item.getId() == id2) {
				containsId2 = true;
			}
		}
		return containsId1 && containsId2;
	}
	
	public static void handleItemOption3(Player player, int slotId, int itemId, Item item) {
		long time = Misc.currentTimeMillis();
		if (player.getLocks().isInteractionLocked() || player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		player.stopAll(false);
		if (itemId == 21371) {
			if (!player.getAttributes().isCanPvp()) {
				if (player.getInventory().getFreeSlots() > 1) {
					player.getInventory().deleteItem(21371, 1);
					player.getInventory().addItem(4151, 1);
					player.getInventory().addItem(21369, 1);
					player.getPackets().sendGameMessage("You split the vine and whip apart.");
					return;
				} else {
					player.getPackets().sendGameMessage("You need two inventory spaces to do this.");
					return;
				}
			} else {
				player.getPackets().sendGameMessage("You can not do this in the wilderness.");
				return;
			}
		}
		
		if (itemId == 20767 || itemId == 20769 || itemId == 20771) {
			SkillCapeCustomizer.startCustomizing(player, itemId);
		} else if (EquipmentConstants.getItemSlot(itemId) == EquipmentConstants.SLOT_AURA) {
			player.getAuraManager().sendTimeRemaining(itemId);
		}
		System.out.println("Option 3?????????");
	}
	
	public static void handleItemOption4(Player player, int slotId, int itemId, Item item) {
	
	}
	
	public static void handleItemOption5(Player player, int slotId, int itemId, Item item) {
	}
	
	public static void handleItemOption6(Player player, int slotId, int itemId, Item item) {
		
		if (itemId == 20769 || itemId == 20771) {
			player.getDialogueManager().startDialogue("CompCape");
			return;
		}
		long time = Misc.currentTimeMillis();
		if (player.getLocks().isInteractionLocked() || player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		player.stopAll(false);
		Pouches pouches = Pouches.forId(itemId);
		if (pouches != null) {
			Summoning.spawnFamiliar(player, pouches);
		} else if (itemId == 1438) {
			Runecrafting.locate(player, 3127, 3405);
		} else if (itemId == 1440) {
			Runecrafting.locate(player, 3306, 3474);
		} else if (itemId == 1442) {
			Runecrafting.locate(player, 3313, 3255);
		} else if (itemId == 1444) {
			Runecrafting.locate(player, 3185, 3165);
		} else if (itemId == 1446) {
			Runecrafting.locate(player, 3053, 3445);
		} else if (itemId == 1448) {
			Runecrafting.locate(player, 2982, 3514);
		} else if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354 && itemId <= 10362) {
			player.getDialogueManager().startDialogue("Transportation", "Edgeville", new WorldTile(3087, 3496, 0), "Karamja", new WorldTile(2918, 3176, 0), "Draynor Village", new WorldTile(3105, 3251, 0), "Al Kharid", new WorldTile(3293, 3163, 0), itemId);
		} else if (itemId == 1704 || itemId == 10352) {
			player.getPackets().sendGameMessage("The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
		} else if (itemId >= 3853 && itemId <= 3867) {
			player.getDialogueManager().startDialogue("Transportation", "Burthrope Games Room", new WorldTile(2880, 3559, 0), "Barbarian Outpost", new WorldTile(2519, 3571, 0), "Gamers' Grotto", new WorldTile(2970, 9679, 0), "Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);
		}
	}
	
	public static void handleItemOption7(Player player, int slotId, int itemId, Item item) {
		long time = System.currentTimeMillis();
		if (player.getLocks().isInteractionLocked() || player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		player.stopAll(false);
		if (item.getDefinitions().isDestroyItem()) {
			player.getDialogueManager().startDialogue("DestroyItemOption", Integer.valueOf(slotId), item);
			return;
		}
		if (player.getCharges().degradeCompletly(item)) {
			return;
		}
		player.getInventory().deleteItem(slotId, item);
		RegionManager.addGroundItem(item, new WorldTile(player), player, false, 180, true);
		player.getPackets().sendSound(2739, 0, 1);
	}
	
	public static void handleItemOption8(Player player, int slotId, int itemId, Item item) {
		player.getPackets().sendGameMessage(ItemCharacteristicRepository.getExamine(item.getId()));
	}
	
	public static void handleItemOnPlayer(final Player player, final Player usedOn, final int itemId) {
		player.setRouteEvent(new RouteEvent(usedOn, () -> {
			player.faceActor(usedOn);
			if (usedOn.getInterfaceManager().containsScreenInter()) {
				player.getPackets().sendGameMessage(usedOn.getDisplayName() + " is busy.");
				return;
			}
			switch (itemId) {
				case 962:// Christmas cracker
					if (player.getInventory().getFreeSlots() < 3 || usedOn.getInventory().getFreeSlots() < 3) {
						String message = (player.getInventory().getFreeSlots() < 3 ? "You do" : "The other player does") + " not have enough inventory space to open this cracker.";
						player.getPackets().sendGameMessage(message);
						return;
					}
					player.getDialogueManager().startDialogue("ChristmasCrackerD", usedOn, itemId);
					break;
				default:
					player.getPackets().sendGameMessage("Nothing interesting happens.");
					break;
			}
		}, true));
	}
}