package com.rs.game.content;

import com.rs.game.entity.actor.mask.ForceTalk;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.world.World;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.item.Item;
import com.rs.game.entity.item.ItemsContainer;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.item.ItemConstants;
import com.rs.networking.codec.decode.WorldPacketsDecoder;
import com.rs.utility.game.item.ItemSetsKeyGenerator;
import com.rs.utility.Misc;

public class PartyRoom {

    public static int PARTY_CHEST_INTERFACE = 647, INVENTORY_INTERFACE = 336;
    
    private static ItemsContainer<Item> items = new ItemsContainer<Item>(100, false);
    
    private static final int CHEST_INTERFACE_ITEMS_KEY = ItemSetsKeyGenerator.generateKey();
    
	public static void openPartyChest(final Player player) {
		player.getTemporaryAttributtes().put("PartyRoomInventory", Boolean.TRUE);
		player.getInterfaceManager().sendInterface(PARTY_CHEST_INTERFACE);
		player.getInterfaceManager().sendInventoryInterface(INVENTORY_INTERFACE);
		sendOptions(player);
		player.setCloseInterfacesEvent(new Runnable() {
		    @Override
		    public void run() {
			player.getTemporaryAttributtes().remove("PartyRoomInventory");
		    }
		});
    }
	
	public static boolean handleInventoryInterface(Player player, int interfaceId, int buttonId, int packetId, int slotId) {
		if (interfaceId == 336) {
		if (buttonId == 0) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
			    addItem(player, slotId, 1);//1
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
			    addItem(player, slotId, 5);//5
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
				addItem(player, slotId, 10);//10
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
				addItem(player, slotId, Integer.MAX_VALUE);//all
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
			    player.getTemporaryAttributtes().put("party_room_X_Slot", slotId);
			    player.getTemporaryAttributtes().remove("party_room_remove");
			    player.getPackets().sendInputIntegerScript(true, "Enter amount:");//x amount
			}
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
			    player.getInventory().sendExamine(slotId);//examine
		    }
			return false;
		}
		return true;
	}
	
	public static void addItem(Player player, int slot, int amount) {
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage("You can't add that item to the party chest!");
			return;
		}
		Item[] itemsBefore = items.getItemsCopy();
		int maxAmount = player.getInventory().getItems().getNumberOf(item);
		if (amount < maxAmount)
			item = new Item(item.getId(), amount);
		else
			item = new Item(item.getId(), maxAmount);
		items.add(item);
		player.getInventory().deleteItem(slot, item);
		refreshItems(player, itemsBefore);
	}
	
	public static void removeItem(Player player, final int slot, int amount) {
		Item item = items.get(slot);
		if (item == null)
			return;
		Item[] itemsBefore = items.getItemsCopy();
		int maxAmount = items.getNumberOf(item);
		if (amount < maxAmount)
			item = new Item(item.getId(), amount);
		else
			item = new Item(item.getId(), maxAmount);
		items.remove(slot, item);
		player.getInventory().addItem(item);
		refreshItems(player, itemsBefore);
	}
	
	public static void refreshItems(Player player, Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = items.getItems()[index];
			if (itemsBefore[index] != item)  {
				if(itemsBefore[index] != null && (item == null || item.getId() != itemsBefore[index].getId() || item.getAmount() < itemsBefore[index].getAmount()))
					//sendFlash(index);
				changedSlots[count++] = index;
			}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(player, finalChangedSlots);
	}	
	public static void refresh(Player player, int... slots) {
		player.getPackets().sendUpdateItems(90, items, slots);
	}    
	private static void sendOptions(final Player player) {
		player.getPackets().sendInterSetItemsOptionsScript(INVENTORY_INTERFACE, 0, 93, 4, 7, "Deposit", "Deposit-5", "Deposit-10", "Deposit-All", "Deposit-X");
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, 1278);
		player.getPackets().sendInterSetItemsOptionsScript(INVENTORY_INTERFACE, 30, CHEST_INTERFACE_ITEMS_KEY, 4, 7, "Value");
		player.getPackets().sendIComponentSettings(PARTY_CHEST_INTERFACE, 30, 0, 27, 1150);
		player.getPackets().sendInterSetItemsOptionsScript(PARTY_CHEST_INTERFACE, 33, CHEST_INTERFACE_ITEMS_KEY, 4, 7, "Examine");
		player.getPackets().sendIComponentSettings(PARTY_CHEST_INTERFACE, 33, 0, 27, 1026);
	}
	public static void purchase(final Player player, boolean balloons) {
	    if (balloons) {
	    	if (player.getInventory().containsItem(995, 1000)) {
	    	// startParty(player);
	    } else {
	    	player.getDialogueManager().startDialogue("SimpleMessage", "Balloon Bonanza costs 1000 coins.");
	    }
	} else {
	    if (player.getInventory().containsItem(995, 500)) {
			startDancingKnights();
		} else {
			player.getDialogueManager().startDialogue("SimpleMessage", "Nightly Dance costs 500 coins.");
		    }
		}
    }
	
	private static WorldTile[] dancingTiles = { 
		new WorldTile(3049, 3378, 0), new WorldTile(3048, 3378, 0), 
		new WorldTile(3047, 3378, 0), new WorldTile(3046, 3378, 0),new WorldTile(3045, 3378, 0), 
		new WorldTile(3044, 3378, 0), new WorldTile(3043, 3378, 0), new WorldTile(3042, 3378, 0) };
	
	public static void startDancingKnights() {
		for (int i = 0; i < 8; i ++) {
			for (NPC npc : World.getNPCs()) {
				if (npc == null)
					continue;
				if (npc.getId() == 660) {
					World.spawnNPC(660, dancingTiles[i], -1, false);
					npc.setDirection(2);
				}
			}
		}
    }
	
	private static int dropValue = 2000000000;//2b drop test
	
	private static int getDropValue() {
		return dropValue;
	}
	
	private static int timeleft = 3;
	
	private static int getTimeLeft() {
		return timeleft;
	}
	
	public static boolean inFalador(WorldTile tile) {
		return ((tile.getX() >= 2938 && tile.getX() <= 3069) && (tile.getY() >= 3310 && tile.getY() <= 3395));
	}
	
	public static void announce(Player player, boolean worldwide) {
		if (worldwide) {
			for (NPC npc : World.getNPCs()) {
				if (npc == null)
					continue;
				if (npc.getName().toLowerCase().contains("banker")) 
					npc.setNextForceTalk(new ForceTalk
							(Misc.getFormattedNumber(getDropValue() / getDropValue() > 1000000000 ? 1000000000 : 1000000)+ " " + (getDropValue() > 1000000000 ? "B" : "M") +
									" Drop party in falador party room in "+getTimeLeft() + " "+(getTimeLeft() > 1 ? "s" : "")+"."));
			}
		}
	}
}
