package org.redrune.game.entity.actor.player.link;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.item.ItemsContainer;
import org.redrune.utility.constants.ItemConstants;
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository;

import java.util.Objects;
import java.util.stream.Stream;

public class TradeManager {
	
	/** The important player objects in this class. Us and the recipient of our items */
	private final Player player;
	private Player target;
	
	/** The items we are offering */
	private final ItemsContainer<Item> items;
	
	/** If the item we're lending is lent until logout */
	private boolean lentTillLogout;
	
	/** The time the item was lent for */
	private long hoursLentFor;
	
	/** If the trade page was modified */
	private boolean tradeModified;
	
	/** If we have accepted */
	private boolean accepted;
	
	public TradeManager(Player player) {
		this.player = player; //player reference
		items = new ItemsContainer<>(28, false);
	}
	
	/*
	 * called to both players
	 */
	public void openTrade(Player target) {
		synchronized (this) {
			this.target = target;
			this.lentTillLogout = true;
			this.hoursLentFor = 0;
			player.getPackets().sendIComponentText(335, 15, "Trading With: " + target.getDisplayName());
			player.getPackets().sendGlobalString(203, target.getDisplayName());
			sendInterItems();
			sendOptions();
			sendTradeModified();
			refreshFreeInventorySlots();
			refreshTradeWealth();
			refreshStageMessage(true);
			player.getInterfaceManager().sendInterface(335);
			player.getInterfaceManager().sendInventoryInterface(336);
			player.setCloseInterfacesEvent(() -> closeTrade(CloseTradeStage.CANCEL));
		}
	}
	
	public void removeItem(final int slot, int amount) {
		synchronized (this) {
			if (!isTrading()) {
				return;
			}
			if (!player.getInterfaceManager().containsInventoryInter()) {
				System.out.println(player.getUsername() + " attempted to remove items after trade stage.");
				return;
			}
			Item item = items.get(slot);
			if (item == null) {
				return;
			}
			Item[] itemsBefore = items.getItemsCopy();
			int maxAmount = items.getNumberOf(item);
			if (amount < maxAmount) {
				item = new Item(item.getId(), amount);
			} else {
				item = new Item(item.getId(), maxAmount);
			}
			items.remove(slot, item);
			player.getInventory().addItemDrop(item);
			refreshItems(itemsBefore);
			cancelAccepted();
			setTradeModified(true);
		}
	}
	
	public boolean isTrading() {
		return target != null;
	}
	
	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = items.getItems()[index];
			if (itemsBefore[index] != item) {
				if (itemsBefore[index] != null && (item == null || item.getId() != itemsBefore[index].getId() || item.getAmount() < itemsBefore[index].getAmount())) {
					sendFlash(index);
				}
				changedSlots[count++] = index;
			}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
		refreshFreeInventorySlots();
		refreshTradeWealth();
	}
	
	public void cancelAccepted() {
		boolean canceled = false;
		if (accepted) {
			accepted = false;
			canceled = true;
		}
		if (target.getTradeManager().accepted) {
			target.getTradeManager().accepted = false;
			canceled = true;
		}
		if (canceled) {
			refreshBothStageMessage(true);
		}
	}
	
	public void setTradeModified(boolean modified) {
		if (modified == tradeModified) {
			return;
		}
		tradeModified = modified;
		sendTradeModified();
	}
	
	public void sendFlash(int slot) {
		target.getPackets().sendInterFlashScript(335, 33, 4, 7, slot);
		player.getPackets().sendInterFlashScript(335, 36, 4, 7, slot);
	}
	
	public void refresh(int... slots) {
		player.getPackets().sendUpdateItems(90, items, slots);
		target.getPackets().sendUpdateItems(90, true, items.getItems(), slots);
	}
	
	public void refreshFreeInventorySlots() {
		int freeSlots = player.getInventory().getFreeSlots();
		target.getPackets().sendIComponentText(335, 21, "has " + (freeSlots == 0 ? "no" : freeSlots) + " free" + "<br>inventory slots");
	}
	
	public void refreshTradeWealth() {
		int wealth = getTradeWealth();
		player.getPackets().sendGlobalConfig(729, wealth);
		target.getPackets().sendGlobalConfig(697, wealth);
	}
	
	public void refreshBothStageMessage(boolean firstStage) {
		refreshStageMessage(firstStage);
		target.getTradeManager().refreshStageMessage(firstStage);
	}
	
	public void sendTradeModified() {
		player.getPackets().sendConfig(1042, tradeModified ? 1 : 0);
		target.getPackets().sendConfig(1043, tradeModified ? 1 : 0);
	}
	
	public int getTradeWealth() {
		int wealth = 0;
		for (Item item : items.getItems()) {
			if (item == null) {
				continue;
			}
			wealth += (item.getId() == 995 ? 1 : item.getDefinitions().getValue()) * item.getAmount();
		}
		return wealth;
	}
	
	public void refreshStageMessage(boolean firstStage) {
		player.getPackets().sendIComponentText(firstStage ? 335 : 334, firstStage ? 37 : 34, getAcceptMessage(firstStage));
	}
	
	public String getAcceptMessage(boolean firstStage) {
		if (accepted) {
			return "Waiting for other player...";
		}
		if (target.getTradeManager().accepted) {
			return "Other player has accepted.";
		}
		return firstStage ? "" : "Are you sure you want to make this trade?";
	}
	
	public void addItem(Item item, boolean deleteFromInventory) {
		synchronized (this) {
			if (!isTrading()) {
				return;
			}
			if (item == null) {
				return;
			}
			if (!ItemConstants.isTradeable(item)) {
				player.getPackets().sendMessage("That item isn't tradeable.");
				return;
			}
			Item[] itemsBefore = items.getItemsCopy();
			items.add(item);
			if (deleteFromInventory) {
				player.getInventory().deleteItem(item);
			}
			refreshItems(itemsBefore);
			cancelAccepted();
		}
	}
	
	public void addItem(int slot, int amount) {
		synchronized (this) {
			if (!isTrading()) {
				return;
			}
			Item item = player.getInventory().getItem(slot);
			if (item == null) {
				return;
			}
			if (!ItemConstants.isTradeable(item)) {
				player.getPackets().sendMessage("That item isn't tradeable.");
				return;
			}
			Item[] itemsBefore = items.getItemsCopy();
			int maxAmount = player.getInventory().getItems().getNumberOf(item);
			if (amount < maxAmount) {
				item = new Item(item.getId(), amount);
			} else {
				item = new Item(item.getId(), maxAmount);
			}
			items.add(item);
			player.getInventory().deleteItem(slot, item);
			refreshItems(itemsBefore);
			cancelAccepted();
		}
	}
	
	public void sendOptions() {
		Object[] tparams1 = new Object[] { "", "", "", "Value<col=FF9040>", "Remove-X", "Remove-All", "Remove-10", "Remove-5", "Remove", -1, 0, 7, 4, 90, 335 << 16 | 31 };
		player.getPackets().sendRunScript(150, tparams1);
		player.getPackets().sendIComponentSettings(335, 31, 0, 27, 1150); // Access
		Object[] tparams3 = new Object[] { "", "", "", "", "", "", "", "", "Value<col=FF9040>", -1, 0, 7, 4, 90, 335 << 16 | 34 };
		player.getPackets().sendRunScript(695, tparams3);
		player.getPackets().sendIComponentSettings(335, 34, 0, 27, 1026); // Access
		Object[] tparams2 = new Object[] { "", "", "Lend", "Value<col=FF9040>", "Offer-X", "Offer-All", "Offer-10", "Offer-5", "Offer", -1, 0, 7, 4, 93, 336 << 16 };
		player.getPackets().sendRunScript(150, tparams2);
		player.getPackets().sendIComponentSettings(336, 0, 0, 27, 1278); // Access
		for (int i = 50; i < 58; i++) {
			player.getPackets().sendIComponentSettings(335, i, -1, -1, 6);
		}
	}
	
	public void sendInterItems() {
		player.getPackets().sendItems(90, items);
		target.getPackets().sendItems(90, true, items);
	}
	
	public void accept(boolean firstStage) {
		synchronized (this) {
			if (!isTrading()) {
				return;
			}
			if (target.getTradeManager().accepted) {
				if (firstStage) {
					if (nextStage()) {
						target.getTradeManager().nextStage();
					}
				} else {
					player.setCloseInterfacesEvent(null);
					player.closeInterfaces();
					closeTrade(CloseTradeStage.DONE);
				}
				return;
			}
			accepted = true;
			refreshBothStageMessage(firstStage);
		}
	}
	
	public void sendValue(int slot, boolean traders) {
		if (!isTrading()) {
			return;
		}
		Item item = traders ? target.getTradeManager().items.get(slot) : items.get(slot);
		if (item == null) {
			return;
		}
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendMessage("That item isn't tradeable.");
			return;
		}
		player.getPackets().sendMessage(item.getDefinitions().getName() + ": market price is " + item.getDefinitions().getValue() + " coins.");
	}
	
	public void sendValue(int slot) {
		Item item = player.getInventory().getItem(slot);
		if (item == null) {
			return;
		}
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendMessage("That item isn't tradeable.");
			return;
		}
		player.getPackets().sendMessage(item.getDefinitions().getName() + ": market price is " + item.getDefinitions().getValue() + " coins.");
	}
	
	public void sendExamine(int slot, boolean traders) {
		if (!isTrading()) {
			return;
		}
		Item item = traders ? target.getTradeManager().items.get(slot) : items.get(slot);
		if (item == null) {
			return;
		}
		player.getPackets().sendMessage(ItemCharacteristicRepository.getExamine(item));
	}
	
	public boolean nextStage() {
		if (!isTrading()) {
			return false;
		}
		if (player.getInventory().getItems().getUsedSlots() + target.getTradeManager().items.getUsedSlots() > 28) {
			player.setCloseInterfacesEvent(null);
			player.closeInterfaces();
			closeTrade(CloseTradeStage.NO_SPACE);
			return false;
		}
		accepted = false;
		player.getInterfaceManager().sendInterface(334);
		player.getInterfaceManager().closeInventoryInterface();
		player.getPackets().sendHideIComponent(334, 55, !(tradeModified || target.getTradeManager().tradeModified));
		refreshBothStageMessage(false);
		return true;
	}
	
	public void closeTrade(CloseTradeStage stage) {
		synchronized (this) {
			Player oldTarget = target;
			oldTarget.setNextFaceActor(null);
			player.setNextFaceActor(null);
			target = null;
			tradeModified = false;
			accepted = false;
			if (CloseTradeStage.DONE != stage) {
				Stream.of(items.toArray()).filter(Objects::nonNull).forEach(item -> player.getInventory().addItemDrop(item));
				items.clear();
				player.getInterfaceManager().closeInventoryInterface();
				player.getInventory().init();
			} else {
				player.getPackets().sendMessage("Accepted trade.");
				player.getInventory().getItems().addAll(oldTarget.getTradeManager().items);
				player.getInventory().init();
				oldTarget.getTradeManager().items.clear();
			}
			if (oldTarget.getTradeManager().isTrading()) {
				oldTarget.setCloseInterfacesEvent(null);
				oldTarget.closeInterfaces();
				oldTarget.getTradeManager().closeTrade(stage);
				if (CloseTradeStage.CANCEL == stage) {
					oldTarget.getPackets().sendMessage("<col=ff0000>Other player declined trade!");
				} else if (CloseTradeStage.NO_SPACE == stage) {
					player.getPackets().sendMessage("You don't have enough space in your inventory for this trade.");
					oldTarget.getPackets().sendMessage("Other player doesn't have enough space in their inventory for this trade.");
				}
			}
		}
	}
	
	public Player getTarget() {
		return target;
	}
	
	private enum CloseTradeStage {
		CANCEL,
		NO_SPACE,
		DONE
	}
}
