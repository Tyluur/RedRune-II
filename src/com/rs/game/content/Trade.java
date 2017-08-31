package com.rs.game.content;

import com.rs.game.entity.item.Item;
import com.rs.game.entity.item.ItemConstants;
import com.rs.game.entity.item.ItemsContainer;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.Misc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Alex <lubricant@pulsescape.net>
 */
public class Trade implements Serializable {
	
	private static final long serialVersionUID = 4297266601674588915L;
	
	private final Player trader, partner;
	
	private boolean borrowing = false;
	
	private int borrowedFrom = -1;
	
	private boolean lending = false;
	
	private int lentItem = -1;
	
	private int lentTo = -1;
	
	private Player player;
	
	private TradeState currentState = TradeState.STATE_ONE;// thats how my old
	
	// one started
	private ItemsContainer<Item> traderItemsOffered = new ItemsContainer<Item>(28, false);
	
	private ItemsContainer<Item> partnerItemsOffered = new ItemsContainer<Item>(28, false);
	
	private boolean traderDidAccept, partnerDidAccept;
	
	Item[] lendItems = new Item[] { new Item(-1, -1) };
	
	public static final int[] LENDABLE_ITEMS = { 4151 };
	
	/**
	 * Constructor
	 */
	public Trade(Player trader, Player partner, Player player) {
		this.trader = trader;
		this.partner = partner;
		this.player = player;
		trader.getTemporaryAttributtes().put("didRequestTrade", Boolean.FALSE);
		partner.getTemporaryAttributtes().put("didRequestTrade", Boolean.FALSE);
		trader.setCloseInterfacesEvent(new Runnable() {
			
			@Override
			public void run() {
				tradeFailed();
				
			}
			
		});
		partner.setCloseInterfacesEvent(new Runnable() {
			
			@Override
			public void run() {
				tradeFailed();
				
			}
			
		});
	}
	
	public void start() {
		if (trader.getX() == partner.getX() && trader.getY() == partner.getY()) {
			trader.sendMessage("You can't trade in this position.");
			trader.getTrade().endSession();
			partner.sendMessage("You can't trade in this position.");
			partner.getTrade().endSession();
			return;
		}
		refreshScreen();
		openFirstTradeScreen(trader);
		openFirstTradeScreen(partner);
		refreshScreen();
	}
	
	/**
	 * @return partner
	 */
	public Player getPartner() {
		return partner;
	}
	
	/**
	 * Sends Options
	 */
	public static void sendOptions(Player p) {
		Object[] tparams1 = new Object[] { "", "", "", "Value<col=FF9040>", "Remove-X", "Remove-All", "Remove-10", "Remove-5", "Remove", -1, 0, 7, 4, 90, 335 << 16 | 31 };
		p.getPackets().sendRunScript(150, tparams1);
		p.getPackets().sendIComponentSettings(335, 31, 0, 27, 1150); // Access
		Object[] tparams3 = new Object[] { "", "", "", "", "", "", "", "", "Value<col=FF9040>", -1, 0, 7, 4, 90, 335 << 16 | 34 };
		p.getPackets().sendRunScript(695, tparams3);
		p.getPackets().sendIComponentSettings(335, 34, 0, 27, 1026); // Access
		Object[] tparams2 = new Object[] { "", "", "Lend", "Value<col=FF9040>", "Offer-X", "Offer-All", "Offer-10", "Offer-5", "Offer", -1, 0, 7, 4, 93, 336 << 16 };
		p.getPackets().sendRunScript(150, tparams2);
		p.getPackets().sendIComponentSettings(336, 0, 0, 27, 1278); // Access
		// refreshScreen();
		
	}
	
	/**
	 * Opens first screen
	 */
	public void openFirstTradeScreen(Player p) {
		sendOptions(p);
		p.getInterfaceManager().sendInterface(335);
		p.getInterfaceManager().sendInventoryInterface(336);
		p.getPackets().sendItems(90, false, traderItemsOffered);
		p.getPackets().sendItems(90, true, partnerItemsOffered);
		p.getPackets().sendIComponentText(335, 37, "");
		String name = p.equals(trader) ? partner.getUsername() : trader.getUsername();
		p.getPackets().sendIComponentText(335, 15, "Trading with: " + Misc.formatPlayerNameForDisplay(name));
		p.getPackets().sendIComponentText(335, 22, Misc.formatPlayerNameForDisplay(name));
		refreshScreen();
		if (trader.getX() == partner.getX() && trader.getY() == partner.getY()) {
			trader.sendMessage("You can't trade in this position.");
			trader.getTrade().endSession();
			partner.sendMessage("You can't trade in this position.");
			partner.getTrade().endSession();
		}
	}
	
	/**
	 * Opens second screen
	 */
	public void openSecondTradeScreen(Player p) {
		currentState = TradeState.STATE_TWO;
		partnerDidAccept = false;
		traderDidAccept = false;
		p.getInterfaceManager().sendInterface(334);
		p.getPackets().sendIComponentText(334, 54, (Misc.formatPlayerNameForDisplay(trader.getUsername())));
		p.getPackets().sendIComponentText(334, 34, "Are you sure you want to make this trade?");
		if (trader.getX() == partner.getX() && trader.getY() == partner.getY()) {
			trader.sendMessage("You can't trade in this position.");
			trader.getTrade().endSession();
			partner.sendMessage("You can't trade in this position.");
			partner.getTrade().endSession();
			return;
		}
	}
	
	/**
	 * Add's item to inter.
	 */
	public void addItem(Player player, int slot, int amount) {
		if (currentState == TradeState.STATE_ONE) {
			
			Item item = player.getInventory().getItem(slot);
			if (item == null) {
				return;
			}
			if (!ItemConstants.isTradeable(item)) {
				player.getPackets().sendGameMessage("That item isn't tradeable.");
				return;
			}
			Item itemsBefore[] = getOffer(player).getItemsCopy();
			int maxAmount = player.getInventory().getItems().getNumberOf(item);
			if (amount < maxAmount) {
				item = new Item(item.getId(), amount);
			} else {
				item = new Item(item.getId(), maxAmount);
			}
			getOffer(player).add(item);
			player.getInventory().deleteItem(slot, item);
			refreshItems(player, itemsBefore);
			resetAccept();
		}
		if (currentState == TradeState.STATE_TWO) {
			return;
		}
	}
	
	/**
	 * saves Logs TODO
	 */
	@SuppressWarnings("unused")
	private void saveLog(final Player player, final int i) {
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter("tradelog.txt", true));
			bf.write("[" + DateFormat.getDateTimeInstance().format(new Date()) + " " + Calendar.getInstance().getTimeZone().getDisplayName() + "] " + player.getUsername() + ": " + partnerItemsOffered);
			bf.newLine();
			bf.flush();
			bf.close();
		} catch (IOException ignored) {
		}
	}
	
	/**
	 * Shows trade icons (IE the red exclamation)
	 */
	public static void TradeIcons(Player player, int slot) {
		Object[] tparams4 = new Object[] { slot, 7, 4, 21954593 };
		player.getPackets().sendRunScript(143, tparams4);
	}
	
	/**
	 * getOffer for items.
	 */
	private ItemsContainer<Item> getOffer(Player p) {
		return p != partner ? traderItemsOffered : partnerItemsOffered;
	}
	
	/**
	 * Gets other offer
	 */
	private Player getOther(Player other) {
		return partner;
	}
	
	/**
	 * Removes Item from interface
	 */
	public void removeItem(Player player, int slotId, int amount) {
		if (currentState != TradeState.STATE_TWO) {
			Item item = getOffer(player).get(slotId);
			if (item == null) {
				return;
			}
			Item itemsBefore[] = getOffer(player).getItemsCopy();
			int maxAmount = getOffer(player).getNumberOf(item);
			if (amount < maxAmount) {
				item = new Item(item.getId(), amount);
			} else {
				item = new Item(item.getId(), maxAmount);
			}
			getOffer(player).remove(slotId, item);
			player.getInventory().addItem(item);
			refreshItems(player, itemsBefore);
			// TradeIcons(partner, slotId);
			// TradeIcons(trader, slotId);
			TradeIcons(getOther(player), slotId);
			resetAccept();
		}
	}
	
	/**
	 * refreshes items on inter
	 */
	private void refreshItems(Player player, Item itemsBefore[]) {
		int changedSlots[] = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = getOffer(player).getItems()[index];
			if (itemsBefore[index] != item) {
				changedSlots[count++] = index;
			}
		}
		int finalChangedSlots[] = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refreshScreen();
	}
	
	/**
	 * Refreshes screen
	 */
	private void refreshScreen() {
		trader.getPackets().sendItems(90, false, traderItemsOffered);
		trader.getPackets().sendItems(90, true, partnerItemsOffered);
		partner.getPackets().sendItems(90, false, partnerItemsOffered);
		partner.getPackets().sendItems(90, true, traderItemsOffered);
		String name = trader.getUsername();
		partner.getPackets().sendIComponentText(335, 22, Misc.formatPlayerNameForDisplay(name));
		String name1 = partner.getUsername();
		trader.getPackets().sendIComponentText(335, 22, Misc.formatPlayerNameForDisplay(name1));
		trader.getPackets().sendIComponentText(335, 21, " has " + partner.getInventory().getFreeSlots() + " free inventory slots.");
		partner.getPackets().sendIComponentText(335, 21, " has " + trader.getInventory().getFreeSlots() + " free inventory slots.");
		trader.getPackets().sendGlobalConfig(729, getTradersItemsValue());
		trader.getPackets().sendGlobalConfig(697, getPartnersItemsValue());
		partner.getPackets().sendGlobalConfig(729, getPartnersItemsValue());
		partner.getPackets().sendGlobalConfig(697, getTradersItemsValue());
	}
	
	/**
	 * Gets trade value
	 *
	 * @return returns initial price
	 */
	private int getTradersItemsValue() {
		int initialPrice = 0;
		for (Item item : traderItemsOffered.toArray()) {
			if (item != null) {
				initialPrice += item.getDefinitions().getValue(item.getId()) * item.getAmount();
			}
		}
		return initialPrice;
	}
	
	/**
	 * gets Value of partner
	 *
	 * @return regular price
	 */
	private int getPartnersItemsValue() {
		int initialPrice = 0;
		for (Item item : partnerItemsOffered.toArray()) {
			if (item != null) {
				initialPrice += item.getDefinitions().getValue(item.getId()) * item.getAmount();
			}
		}
		return initialPrice;
	}
	
	/**
	 * Sets accepted pressed.
	 */
	public void acceptPressed(Player pl) {
		if (!traderDidAccept && pl.equals(trader)) {
			traderDidAccept = true;
		} else if (!partnerDidAccept && pl.equals(partner)) {
			partnerDidAccept = true;
		}
		switch (currentState) {
			case STATE_ONE:
				if (pl.equals(trader)) {
					if (partnerDidAccept && traderDidAccept) {
						if (!trader.getInventory().getItems().hasSpaceFor(partnerItemsOffered)) {
							partner.getPackets().sendGameMessage("Other player does not have enough space in their inventory.");
							trader.getPackets().sendGameMessage("You do not have enough space in your inventory.");
							tradeFailed();
							return;
						} else if (!partner.getInventory().getItems().hasSpaceFor(traderItemsOffered)) {
							trader.getPackets().sendGameMessage("Other player does not have enough space in their inventory.");
							partner.getPackets().sendGameMessage("You do not have enough space in your inventory.");
							tradeFailed();
							return;
						} else if (partner.getInventory().getItems().goesOverAmount(traderItemsOffered)) {
							partner.getPackets().sendGameMessage("You'll go over max of an item!");
							trader.getPackets().sendGameMessage("They'll go over max of an item!");
							tradeFailed();
							return;
						} else if (trader.getInventory().getItems().goesOverAmount(partnerItemsOffered)) {
							trader.getPackets().sendGameMessage("You'll go over max of an item!");
							partner.getPackets().sendGameMessage("They'll go over max of an item!");
							tradeFailed();
							return;
						}
						openSecondTradeScreen(trader);
						openSecondTradeScreen(partner);
					} else {
						trader.getPackets().sendIComponentText(335, 37, "Waiting for other player...");
						partner.getPackets().sendIComponentText(335, 37, "Other player has accepted");
					}
				} else if (pl.equals(partner)) {
					if (partnerDidAccept && traderDidAccept) {
						if (!trader.getInventory().getItems().hasSpaceFor(partnerItemsOffered)) {
							partner.getPackets().sendGameMessage("Other player does not have enough space in their inventory.");
							trader.getPackets().sendGameMessage("You do not have enough space in your inventory.");
							tradeFailed();
							return;
						} else if (!partner.getInventory().getItems().hasSpaceFor(traderItemsOffered)) {
							trader.getPackets().sendGameMessage("Other player does not have enough space in their inventory.");
							partner.getPackets().sendGameMessage("You do not have enough space in your inventory.");
							tradeFailed();
							return;
						} else if (partner.getInventory().getItems().goesOverAmount(traderItemsOffered)) {
							partner.getPackets().sendGameMessage("You'll go over max of an item!");
							trader.getPackets().sendGameMessage("They'll go over max of an item!");
							tradeFailed();
							return;
						} else if (trader.getInventory().getItems().goesOverAmount(partnerItemsOffered)) {
							trader.getPackets().sendGameMessage("You'll go over max of an item!");
							partner.getPackets().sendGameMessage("They'll go over max of an item!");
							tradeFailed();
							return;
						}
						openSecondTradeScreen(trader);
						openSecondTradeScreen(partner);
					} else {
						partner.getPackets().sendIComponentText(335, 37, "Waiting for other player...");
						trader.getPackets().sendIComponentText(335, 37, "Other player has accepted");
					}
				}
				break;
			
			case STATE_TWO:
				if (pl.equals(trader)) {
					if (partnerDidAccept && traderDidAccept) {
						giveItems();
					} else {
						trader.getPackets().sendIComponentText(334, 34, "Waiting for other player...");
						partner.getPackets().sendIComponentText(334, 34, "Other player has accepted");
					}
				} else if (pl.equals(partner)) {
					if (partnerDidAccept && traderDidAccept) {
						giveItems();
					} else {
						partner.getPackets().sendIComponentText(334, 34, "Waiting for other player...");
						trader.getPackets().sendIComponentText(334, 34, "Other player has accepted");
					}
				}
				break;
		}
		
	}
	
	/**
	 * Ends Session of trade
	 */
	public void endSession() {
		trader.getInterfaceManager().closeScreenInterface();
		trader.getInterfaceManager().closeInventoryInterface();
		partner.getInterfaceManager().closeScreenInterface();
		partner.getInterfaceManager().closeInventoryInterface();
		traderItemsOffered = null;
		partnerItemsOffered = null;
		trader.setCloseInterfacesEvent(null);
		partner.setCloseInterfacesEvent(null);
		trader.setTrade(null);
		partner.setTrade(null);
		trader.getInventory().refresh();
		partner.getInventory().refresh();
		
	}
	
	/**
	 * WHat happens when tradeFailed
	 */
	public void tradeFailed() {
		trader.getInventory().getItems().addAll(traderItemsOffered);
		partner.getInventory().getItems().addAll(partnerItemsOffered);
		partner.getPackets().sendGameMessage("<col=FFF0000>Other player declined trade!</col>");
		traderItemsOffered = null;
		partnerItemsOffered = null;
		trader.setCloseInterfacesEvent(null);
		partner.setCloseInterfacesEvent(null);
		trader.getInterfaceManager().closeInventoryInterface();
		partner.getInterfaceManager().closeInventoryInterface();
		trader.setTrade(null);
		partner.setTrade(null);
		endSession();
		trader.getInventory().refresh(trader.getInventory().getItems());
		partner.getInventory().refresh(partner.getInventory().getItems());
	}
	
	/**
	 * Gives items to partner / other
	 */
	private void giveItems() {
		try {
			if (!trader.getInventory().getItems().hasSpaceFor(partnerItemsOffered)) {
				partner.getPackets().sendGameMessage("Other player does not have enough space in their inventory.");
				trader.getPackets().sendGameMessage("You do not have enough space in your inventory.");
				tradeFailed();
				return;
			} else if (!partner.getInventory().getItems().hasSpaceFor(traderItemsOffered)) {
				trader.getPackets().sendGameMessage("Other player does not have enough space in their inventory.");
				partner.getPackets().sendGameMessage("You do not have enough space in your inventory.");
				tradeFailed();
				return;
			}
			for (Item itemAtIndex : traderItemsOffered.toArray()) {
				if (itemAtIndex != null) {
					partner.getInventory().addItem(itemAtIndex.getId(), itemAtIndex.getAmount());
				}
			}
			for (Item itemAtIndex : partnerItemsOffered.toArray()) {
				if (itemAtIndex != null) {
					trader.getInventory().addItem(itemAtIndex.getId(), itemAtIndex.getAmount());
				}
			}
			endSession();
			partner.getInventory().refresh();
			partner.sendMessage("Accepted trade.");
			trader.getInventory().refresh();
			trader.sendMessage("Accepted trade.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * gets items offered
	 */
	public ItemsContainer<Item> getPlayerItemsOffered(Player p) {
		return (p.equals(trader) ? traderItemsOffered : partnerItemsOffered);
	}
	
	public enum TradeState {
		STATE_ONE,
		STATE_TWO
	}
	
	/**
	 * resets the accept trade.
	 */
	public void resetAccept() {
		partnerDidAccept = traderDidAccept = false;
		switch (currentState) {
			case STATE_ONE:
				partner.getPackets().sendIComponentText(335, 37, "");
				trader.getPackets().sendIComponentText(335, 37, "");
				break;
			case STATE_TWO:
				partner.getPackets().sendIComponentText(334, 34, "");
				trader.getPackets().sendIComponentText(334, 34, "");
				break;
		}
	}
	
	/**
	 * Gets the state of trade
	 *
	 * @return state
	 */
	public TradeState getState() {// Where? Just havent implemented, I got
		// everything needed tho XD Nice :P Fuck, I
		// really dont feel like adding more then 3
		// pets -____-
		return currentState;
	}
}