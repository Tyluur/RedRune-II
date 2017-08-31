package com.rs.game.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.entity.item.Item;
import com.rs.game.entity.actor.data.CombatDefinitions;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.Skills;
import com.rs.game.entity.item.ItemConstants;
import com.rs.utility.game.item.ItemBonuses;
import com.rs.utility.game.item.ItemExamines;
import com.rs.utility.game.item.ItemSetsKeyGenerator;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Shop {

	private static final int MAIN_STOCK_ITEMS_KEY = ItemSetsKeyGenerator
			.generateKey();

	private static final int MAX_SHOP_ITEMS = 40;
	public static final int COINS = 995;
	public int id = 0;

	private String name;
	private Item[] mainStock;
	private int[] defaultQuantity;
	private Item[] generalStock;
	private int money;
	private CopyOnWriteArrayList<Player> viewingPlayers;

	public Shop(String name, int money, Item[] mainStock,
			boolean isGeneralStore, int id) {
		viewingPlayers = new CopyOnWriteArrayList<Player>();
		this.name = name;
		this.money = money;
		this.mainStock = mainStock;
		this.id = id;
		defaultQuantity = new int[mainStock.length];
		for (int i = 0; i < defaultQuantity.length; i++)
			defaultQuantity[i] = mainStock[i].getAmount();
		if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS)
			generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
	}

	public boolean isGeneralStore() {
		return generalStock != null;
	}

	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getTemporaryAttributtes().put("Shop", this);
		player.setCloseInterfacesEvent(() -> {
			viewingPlayers.remove(player);
			player.getTemporaryAttributtes().remove("Shop");
		});
		player.getPackets().sendConfig(118, MAIN_STOCK_ITEMS_KEY); // sets
																	// mainstock
																	// items set
		player.getPackets().sendConfig(1496, -1); // sets samples items set
		player.getPackets().sendConfig(532, money);
		sendStore(player);
		player.getPackets().sendGlobalConfig(199, -1);// unknown
		player.getInterfaceManager().sendInterface(620); // opens shop
		for (int i = 0; i < MAX_SHOP_ITEMS; i++)
			player.getPackets().sendGlobalConfig(
					946 + i,
					i < defaultQuantity.length ? defaultQuantity[i]
							: generalStock != null ? 0 : -1);// prices
		player.getPackets().sendGlobalConfig(1241, 16750848);// unknown
		player.getPackets().sendGlobalConfig(1242, 15439903);// unknown
		player.getPackets().sendGlobalConfig(741, -1);// unknown
		player.getPackets().sendGlobalConfig(743, -1);// unknown
		player.getPackets().sendGlobalConfig(744, 0);// unknown
		if (generalStock != null)
			player.getPackets().sendHideIComponent(620, 19, false); // unlocks
																	// general
																	// store
																	// icon
		player.getPackets().sendIComponentSettings(620, 25, 0,
				getStoreSize() * 6, 1150); // unlocks stock slots
		sendInventory(player);
		player.getPackets().sendIComponentText(620, 20, name);
	}

	public void sendInventory(Player player) {
		player.getInterfaceManager().sendInventoryInterface(621);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendUnlockIComponentOptionSlots(621, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(621, 0, 93, 4, 7,
				"Value", "Sell 1", "Sell 5", "Sell 10", "Sell 50", "Examine");
	}

	public int getSlotId(int clickSlotId) {
		return clickSlotId / 6;
	}

	public void buy(Player player, int clickSlot, int quantity) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		if (item.getAmount() == 0) {
			player.getPackets().sendGameMessage(
					"There is no stock of that item at the moment.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		int amountCoins = player.getInventory().getItems().getNumberOf(money);
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();

		boolean enoughCoins = maxQuantity >= buyQ;
		if (!enoughCoins) {
			player.getPackets().sendGameMessage("You don't have enough coins.");
			buyQ = maxQuantity;
		} else if (quantity > buyQ)
			player.getPackets().sendGameMessage(
					"The shop has run out of stock.");
		if (item.getDefinitions().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
			}
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			player.getInventory().deleteItem(money, totalPrice);
			player.getInventory().addItem(item.getId(), buyQ);
			item.setAmount(item.getAmount() - buyQ);
			if (item.getAmount() <= 0 && slotId >= mainStock.length)
				generalStock[slotId - mainStock.length] = null;
			refreshShop();
			sendInventory(player);
		}
	}

	public void restoreItems() {
		boolean needRefresh = false;
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i].getAmount() < defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + 1);
				needRefresh = true;
			} else if (mainStock[i].getAmount() > defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + -1);
				needRefresh = true;
			}
		}
		if (generalStock != null) {
			for (int i = 0; i < generalStock.length; i++) {
				Item item = generalStock[i];
				if (item == null)
					continue;
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() <= 0)
					generalStock[i] = null;
				needRefresh = true;
			}
		}
		if (needRefresh)
			refreshShop();
	}

	private boolean addItem(int itemId, int quantity) {
		for (Item item : mainStock) {
			if (item.getId() == itemId) {
				item.setAmount(item.getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (generalStock != null) {
			for (Item item : generalStock) {
				if (item == null)
					continue;
				if (item.getId() == itemId) {
					item.setAmount(item.getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new Item(itemId, quantity);
					refreshShop();
					return true;
				}
			}
		}
		return false;
	}

	public boolean isSellable(int id) {
		int[] nosell = { 18347, 18349, 18351, 18353, 18355, 18357, 18359,
				18361, 18363, 18365, 18367, 18369, 18371, 18373, 18333, 18334,
				18335, 18337, 19893, 19669, 4084, 18746, 18745, 18744, 15704,
				15703, 15702, 15701, 15444, 15443, 15442, 15441, 21999, 21989,
				21979, 21969, 23952, 23942, 23932, 23922, 23912, 23673, 20929,
				22985, 23805, 10404, 1057, 1055, 1053 };
		for (int j : nosell) {
			if (j != id) {
				continue;
			} else if (j == id) {
				return true;
			}
		}
		return false;
	}

	public void sell(Player player, int slotId, int quantity) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		if (isSellable(item.getId())) {
			player.getPackets().sendGameMessage("You cannot sell this item to the shop");
			return;
		}
		int originalId = item.getId();
		if (item.getDefinitions().isNoted())
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (item.getDefinitions().isDestroyItem()
				|| ItemConstants.getItemDefaultCharges(item.getId()) != -1
				|| !ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == 0 && generalStock == null) {
			player.getPackets().sendGameMessage(
					"You can't sell this item to this shop.");
			return;
		}
		int price = ItemDefinitions.getItemDefinitions(item.getId()).getValue(
				item.getId()) / 4;
		int numberOff = player.getInventory().getItems()
				.getNumberOf(originalId);
		if (quantity > numberOff)
			quantity = numberOff;
		if (!addItem(item.getId(), quantity)) {
			player.getPackets().sendGameMessage("Shop is currently full.");
			return;
		}
		player.getInventory().deleteItem(originalId, quantity);
		player.getInventory().addItem(money, price * quantity);
	}

	public void sendValue(Player player, int slotId) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		if (item.getDefinitions().isNoted())
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (item.getDefinitions().isNoted() || !ItemConstants.isTradeable(item)
				|| item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		if (isSellable(item.getId())) {
			player.getPackets().sendGameMessage("You cant sell this item to the shop!");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == 0 && generalStock == null) {
			player.getPackets().sendGameMessage(
					"You can't sell this item to this shop.");
			return;
		}
		int price = ItemDefinitions.getItemDefinitions(item.getId()).getValue(
				item.getId()) / 4;
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName()
						+ ": shop will buy for: "
						+ price
						+ " "
						+ ItemDefinitions.getItemDefinitions(money).getName()
								.toLowerCase()
						+ ". Right-click the item to sell.");
	}

	public int getDefaultQuantity(int itemId) {
		for (int i = 0; i < mainStock.length; i++)
			if (mainStock[i].getId() == itemId)
				return defaultQuantity[i];
		return 0;
	}

	public void sendInfo(Player player, int clickSlot) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		player.getTemporaryAttributtes().put("ShopSelectedSlot", clickSlot);
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName()
						+ ": current costs "
						+ price
						+ " "
						+ ItemDefinitions.getItemDefinitions(money).getName()
								.toLowerCase() + ".");
		player.getInterfaceManager().sendInventoryInterface(449);
		player.getPackets().sendGlobalConfig(741, item.getId());
		player.getPackets().sendGlobalConfig(743, money);
		player.getPackets().sendUnlockIComponentOptionSlots(449, 15, -1, 0, 0,
				1, 2, 3, 4); // unlocks buy
		player.getPackets().sendGlobalConfig(744, price);
		player.getPackets().sendGlobalConfig(745, 0);
		player.getPackets().sendGlobalConfig(746, -1);
		player.getPackets().sendGlobalConfig(168, 98);
		player.getPackets().sendGlobalString(25, ItemExamines.getExamine(item));
		player.getPackets().sendGlobalString(34, ""); // quest id for some items
		int[] bonuses = ItemBonuses.getItemBonuses(item.getId());
		if (bonuses != null) {
			HashMap<Integer, Integer> requiriments = item.getDefinitions()
					.getWearingSkillRequiriments();
			if (requiriments != null && !requiriments.isEmpty()) {
				String reqsText = "";
				for (int skillId : requiriments.keySet()) {
					if (skillId > 24 || skillId < 0)
						continue;
					int level = requiriments.get(skillId);
					if (level < 0 || level > 120)
						continue;
					boolean hasReq = player.getSkills().getLevelForXp(skillId) >= level;
					reqsText += "<br>"
							+ (hasReq ? "<col=00ff00>" : "<col=ff0000>")
							+ "Level " + level + " "
							+ Skills.SKILL_NAME[skillId];
				}
				player.getPackets().sendGlobalString(26,
						"<br>Worn on yourself, requiring: " + reqsText);
			} else
				player.getPackets()
						.sendGlobalString(26, "<br>Worn on yourself");
			player.getPackets().sendGlobalString(
					35,
					"<br>Attack<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_ATTACK]
							+ "<br><col=ffff00>---" + "<br>Strength"
							+ "<br>Ranged Strength" + "<br>Magic Damage"
							+ "<br>Absorve Melee" + "<br>Absorve Magic"
							+ "<br>Absorve Ranged" + "<br>Prayer Bonus");
			player.getPackets()
					.sendGlobalString(36,
							"<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
			player.getPackets().sendGlobalString(
					52,
					"<<br>Defence<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SUMMONING_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STRENGTH_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.RANGED_STR_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.MAGIC_DAMAGE]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORVE_MELEE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORVE_MAGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORVE_RANGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.PRAYER_BONUS]);
		} else
			player.getPackets().sendGlobalString(26, "");

	}

	public void sendVoteInfo(Player player, int clickSlot) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		player.getTemporaryAttributtes().put("ShopSelectedSlot", clickSlot);
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getVotePrice(item, dq);
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName() + ": current costs " + price
						+ " Vote Points.");
		player.getInterfaceManager().sendInventoryInterface(449);
		player.getPackets().sendGlobalConfig(741, item.getId());
		player.getPackets().sendGlobalConfig(743, money);
		player.getPackets().sendUnlockIComponentOptionSlots(449, 15, -1, 0, 0,
				1, 2, 3, 4); // unlocks buy
		player.getPackets().sendGlobalConfig(744, price);
		player.getPackets().sendGlobalConfig(745, 0);
		player.getPackets().sendGlobalConfig(746, -1);
		player.getPackets().sendGlobalConfig(168, 98);
		player.getPackets().sendGlobalString(25, ItemExamines.getExamine(item));
		player.getPackets().sendGlobalString(34, ""); // quest id for some items
		int[] bonuses = ItemBonuses.getItemBonuses(item.getId());
		if (bonuses != null) {
			HashMap<Integer, Integer> requiriments = item.getDefinitions()
					.getWearingSkillRequiriments();
			if (requiriments != null && !requiriments.isEmpty()) {
				String reqsText = "";
				for (int skillId : requiriments.keySet()) {
					if (skillId > 24 || skillId < 0)
						continue;
					int level = requiriments.get(skillId);
					if (level < 0 || level > 120)
						continue;
					boolean hasReq = player.getSkills().getLevelForXp(skillId) >= level;
					reqsText += "<br>"
							+ (hasReq ? "<col=00ff00>" : "<col=ff0000>")
							+ "Level " + level + " "
							+ Skills.SKILL_NAME[skillId];
				}
				player.getPackets().sendGlobalString(26,
						"<br>Worn on yourself, requiring: " + reqsText);
			} else
				player.getPackets()
						.sendGlobalString(26, "<br>Worn on yourself");
			player.getPackets().sendGlobalString(
					35,
					"<br>Attack<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_ATTACK]
							+ "<br><col=ffff00>---" + "<br>Strength"
							+ "<br>Ranged Strength" + "<br>Magic Damage"
							+ "<br>Absorve Melee" + "<br>Absorve Magic"
							+ "<br>Absorve Ranged" + "<br>Prayer Bonus");
			player.getPackets()
					.sendGlobalString(36,
							"<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
			player.getPackets().sendGlobalString(
					52,
					"<<br>Defence<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SUMMONING_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STRENGTH_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.RANGED_STR_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.MAGIC_DAMAGE]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORVE_MELEE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORVE_MAGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORVE_RANGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.PRAYER_BONUS]);
		} else
			player.getPackets().sendGlobalString(26, "");
	}

	public void sendDungInfo(Player player, int clickSlot) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		player.getTemporaryAttributtes().put("ShopSelectedSlot", clickSlot);
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getDungPrice(item, dq);
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName() + ": current costs " + price
						+ " Dung Tokens.");
		player.getInterfaceManager().sendInventoryInterface(449);
		player.getPackets().sendGlobalConfig(741, item.getId());
		player.getPackets().sendGlobalConfig(743, money);
		player.getPackets().sendUnlockIComponentOptionSlots(449, 15, -1, 0, 0,
				1, 2, 3, 4); // unlocks buy
		player.getPackets().sendGlobalConfig(744, price);
		player.getPackets().sendGlobalConfig(745, 0);
		player.getPackets().sendGlobalConfig(746, -1);
		player.getPackets().sendGlobalConfig(168, 98);
		player.getPackets().sendGlobalString(25, ItemExamines.getExamine(item));
		player.getPackets().sendGlobalString(34, ""); // quest id for some items
		int[] bonuses = ItemBonuses.getItemBonuses(item.getId());
		if (bonuses != null) {
			HashMap<Integer, Integer> requiriments = item.getDefinitions()
					.getWearingSkillRequiriments();
			if (requiriments != null && !requiriments.isEmpty()) {
				String reqsText = "";
				for (int skillId : requiriments.keySet()) {
					if (skillId > 24 || skillId < 0)
						continue;
					int level = requiriments.get(skillId);
					if (level < 0 || level > 120)
						continue;
					boolean hasReq = player.getSkills().getLevelForXp(skillId) >= level;
					reqsText += "<br>"
							+ (hasReq ? "<col=00ff00>" : "<col=ff0000>")
							+ "Level " + level + " "
							+ Skills.SKILL_NAME[skillId];
				}
				player.getPackets().sendGlobalString(26,
						"<br>Worn on yourself, requiring: " + reqsText);
			} else
				player.getPackets()
						.sendGlobalString(26, "<br>Worn on yourself");
			player.getPackets().sendGlobalString(
					35,
					"<br>Attack<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_ATTACK]
							+ "<br><col=ffff00>---" + "<br>Strength"
							+ "<br>Ranged Strength" + "<br>Magic Damage"
							+ "<br>Absorve Melee" + "<br>Absorve Magic"
							+ "<br>Absorve Ranged" + "<br>Prayer Bonus");
			player.getPackets()
					.sendGlobalString(36,
							"<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
			player.getPackets().sendGlobalString(
					52,
					"<<br>Defence<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SUMMONING_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STRENGTH_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.RANGED_STR_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.MAGIC_DAMAGE]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORVE_MELEE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORVE_MAGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORVE_RANGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.PRAYER_BONUS]);
		} else
			player.getPackets().sendGlobalString(26, "");
	}

	public int getVotePrice(Item item, int dq) {
		switch (item.getId()) {
		case 1053:
			return 115;
		case 1055:
			return 115;
		case 1057:
			return 115;
		case 10404:
			return 15;
		case 23805:
			return 2;
		case 22985:
			return 5;
		case 20929:
			return 21;
		case 23673:
			return 28;
		case 23912:
		case 23922:
		case 23932:
		case 23942:
		case 23952:
		case 21969:
		case 21979:
		case 21989:
		case 21999:
			return 5;
		case 15441:
		case 15442:
		case 15443:
		case 15444:
		case 15701:
		case 15702:
		case 15703:
		case 15704:
			return 7;
		case 18744:
		case 18745:
		case 18746:
			return 10;
		case 4084:
			return 49;
		}

		return -1;
	}

	public int getDungPrice(Item item, int dq) {
		switch (item.getId()) {
		/* Chaotic */
		case 18349:
			return 200000;
		case 18351:
			return 200000;
		case 18353:
			return 200000;
		case 18355:
			return 200000;
		case 18357:
			return 200000;
		case 18359:
			return 200000;
		case 18361:
			return 200000;
		case 18363:
			return 200000;
			/* Chaotic */

			/* Arcane Knecklaces */
		case 18333:
			return 20000;
		case 18334:
			return 40000;
		case 18335:
			return 60000;
			/* Arcane Knecklaces */

			/* Other Knecklaces */
		case 19887:
			return 70000;
		case 18337:
			return 45000;
			/* Other Knecklaces */

			/* Gravite */
		case 18365:
			return 75000;
		case 18367:
			return 75000;
		case 18369:
			return 75000;
		case 18371:
			return 75000;
		case 18373:
			return 75000;
			/* Gravite */

			/* Other Items */
		case 19893:
			return 125000;
		case 18347:
			return 70000;
		case 19669:
			return 100000;
			/**/
			/**/
			/**/
			/**/
			/**/
			/**/
		}

		return -1;
	}

	public int getBuyPrice(Item item, int dq) {
		return ItemDefinitions.getItemDefinitions(item.getId()).getValue(
				item.getId());
	}

	public int getSellPrice(Item item, int dq) {
		return (ItemDefinitions.getItemDefinitions(item.getId()).getValue(
				item.getId()) * (3 / 4));
	}

	public void sendExamine(Player player, int clickSlot) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshShop() {
		for (Player player : viewingPlayers) {
			sendStore(player);
			player.getPackets().sendIComponentSettings(620, 25, 0,
					getStoreSize() * 6, 1150);
		}
	}

	public int getStoreSize() {
		return mainStock.length
				+ (generalStock != null ? generalStock.length : 0);
	}

	public void sendStore(Player player) {
		Item[] stock = new Item[mainStock.length
				+ (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(mainStock, 0, stock, 0, mainStock.length);
		if (generalStock != null)
			System.arraycopy(generalStock, 0, stock, mainStock.length,
					generalStock.length);
		player.getPackets().sendItems(MAIN_STOCK_ITEMS_KEY, stock);
	}

}
