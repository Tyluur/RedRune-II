package org.redrune.rs2.node.entity.player.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.network.rs666.packet.structure.out.CloseInterfaceBuilder;
import org.redrune.network.rs666.packet.structure.out.GameWindowBuilder;
import org.redrune.network.rs666.packet.structure.out.InterfaceDisplayBuilder;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.rs.GameTab;
import org.redrune.utility.rs.constant.InterfaceConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public final class InterfaceManager implements InterfaceConstants {
	
	/**
	 * The map of the player's tabs. The key id is the child id of the tab. The value is the interface id of the tab.
	 */
	private final Map<Integer, Integer> gameTabs = new HashMap<>();
	
	/**
	 * The id of the window pane
	 */
	@Getter
	@Setter
	private int windowPaneId = -1;
	
	/**
	 * The id of the interface that is opened
	 */
	@Getter
	@Setter
	private int screenInterfaceId = -1;
	
	/**
	 * The id of the interface that is on the chatbox
	 */
	@Getter
	@Setter
	private int chatboxInterfaceId = -1;
	
	/**
	 * The player that owns this manager
	 */
	@Setter
	private transient Player player;
	
	/**
	 * Sends the main interface configuration
	 */
	public void sendInterfaceConfiguration() {
		sendMainInterfaces();
		if (isFixed()) {
			player.getTransmitter().sendFixedAMasks();
		} else {
			player.getTransmitter().sendFullScreenAMasks();
		}
	}
	
	/**
	 * Sends all interfaces to the client
	 */
	private void sendMainInterfaces() {
		switch (player.getNetworkSession().getViewComponents().getScreenSizeMode()) {
			case 0:
			case 1:
				sendWindowPane(SCREEN_FIXED_WINDOW_ID);
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_FIXED_WINDOW_ID, 67, 751, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_FIXED_WINDOW_ID, 192, CHATBOX_WINDOW_ID, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_FIXED_WINDOW_ID, 16, 754, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_FIXED_WINDOW_ID, 182, 748, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_FIXED_WINDOW_ID, 184, 749, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_FIXED_WINDOW_ID, 185, 750, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_FIXED_WINDOW_ID, 187, 747, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_FIXED_WINDOW_ID, 14, 745, true).build(player));
				break;
			case 2:
			case 3:
				sendWindowPane(SCREEN_RESIZABLE_WINDOW_ID);
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_RESIZABLE_WINDOW_ID, 18, 751, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_RESIZABLE_WINDOW_ID, 71, CHATBOX_WINDOW_ID, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_RESIZABLE_WINDOW_ID, 72, 754, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_RESIZABLE_WINDOW_ID, 176, 748, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_RESIZABLE_WINDOW_ID, 177, 749, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_RESIZABLE_WINDOW_ID, 178, 750, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_RESIZABLE_WINDOW_ID, 179, 747, true).build(player));
				player.getTransmitter().send(new InterfaceDisplayBuilder(SCREEN_RESIZABLE_WINDOW_ID, 14, 745, true).build(player));
				break;
		}
		player.getTransmitter().send(new InterfaceDisplayBuilder(CHATBOX_WINDOW_ID, 9, REGULAR_CHATBOX_INTERFACE_ID, true).build(player));
		sendAllTabs();
	}
	
	/**
	 * If the display mode is on fixed
	 */
	private boolean isFixed() {
		return player.getNetworkSession().getViewComponents().getDisplayMode() <= 1;
	}
	
	/**
	 * Sends a window pane
	 *
	 * @param windowPaneId
	 * 		The id of the window pane
	 */
	public void sendWindowPane(int windowPaneId) {
		player.getTransmitter().send(new GameWindowBuilder(this.windowPaneId = windowPaneId, 0).build(player));
	}
	
	/**
	 * Sends all tabs
	 */
	public void sendAllTabs() {
		gameTabs.clear();
		boolean fixed = isFixed();
		for (GameTab data : GameTab.values()) {
			storeTab(data, fixed);
		}
		pushAllTabs();
	}
	
	/**
	 * Stores the tabs data into the map.
	 *
	 * @param tab
	 * 		The tab
	 * @param fixed
	 * 		If the player is on fixed mode
	 */
	private void storeTab(GameTab tab, boolean fixed) {
		gameTabs.put(fixed ? tab.getFixedChildId() : tab.getResizedChildId(), tab.getInterfaceId());
	}
	
	/**
	 * Pushes all the stored tab data out to the network transmitter
	 */
	private void pushAllTabs() {
		boolean fixed = isFixed();
		for (Entry<Integer, Integer> entry : gameTabs.entrySet()) {
			Integer childId = entry.getKey();
			Integer interfaceId = entry.getValue();
			player.getTransmitter().send(new InterfaceDisplayBuilder(fixed ? SCREEN_FIXED_WINDOW_ID : SCREEN_RESIZABLE_WINDOW_ID, childId, interfaceId, true).build(player));
		}
	}
	
	/**
	 * Toggles the visibility of a tab
	 *
	 * @param tab
	 * 		The tab
	 */
	public void toggleTab(GameTab tab) {
		int childId = isFixed() ? tab.getFixedChildId() : tab.getResizedChildId();
		boolean opened = gameTabs.containsKey(childId);
		if (opened) {
			closeTab(tab);
		} else {
			openTab(tab);
		}
	}
	
	/**
	 * Closes a game tab
	 *
	 * @param tab
	 * 		The game tab
	 */
	public void closeTab(GameTab tab) {
		int childId = isFixed() ? tab.getFixedChildId() : tab.getResizedChildId();
		gameTabs.remove(childId);
		player.getTransmitter().send(new CloseInterfaceBuilder(isFixed() ? SCREEN_FIXED_WINDOW_ID : SCREEN_RESIZABLE_WINDOW_ID, childId).build(player));
	}
	
	/**
	 * Opens a game tab
	 *
	 * @param tab
	 * 		The game tab
	 */
	public void openTab(GameTab tab) {
		int childId = isFixed() ? tab.getFixedChildId() : tab.getResizedChildId();
		gameTabs.put(childId, tab.getInterfaceId());
		player.getTransmitter().send(new InterfaceDisplayBuilder(isFixed() ? SCREEN_FIXED_WINDOW_ID : SCREEN_RESIZABLE_WINDOW_ID, childId, tab.getInterfaceId(), true).build(player));
	}
	
	/**
	 * Sends a tab on an existing interface id
	 *
	 * @param tab
	 * 		The tab
	 * @param interfaceId
	 * 		The interface id
	 */
	public void sendTab(GameTab tab, int interfaceId) {
		final int childId = isFixed() ? tab.getFixedChildId() : tab.getResizedChildId();
		gameTabs.put(childId, interfaceId);
		player.getTransmitter().send(new InterfaceDisplayBuilder(isFixed() ? SCREEN_FIXED_WINDOW_ID : SCREEN_RESIZABLE_WINDOW_ID, childId, interfaceId, true).build(player));
	}
	
	/**
	 * Handles the closing of a screen interface
	 */
	public void closeScreenInterface() {
		setScreenInterfaceId(-1);
		player.getTransmitter().send(new CloseInterfaceBuilder(isFixed() ? SCREEN_FIXED_WINDOW_ID : SCREEN_RESIZABLE_WINDOW_ID, isFixed() ? DISPLAY_FIXED_CHILD_ID : DISPLAY_RESIZABLE_CHILD_ID).build(player));
	}
	
	/**
	 * If the player has the interface visible
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public boolean hasInterfaceVisible(int interfaceId) {
		return interfaceId == windowPaneId || screenInterfaceId == interfaceId || chatboxInterfaceId == interfaceId || gameTabs.containsValue(interfaceId);
	}
	
	/**
	 * Shows a screen interface
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 * @param force
	 * 		If the interface should be forced to be displayed, taking priority over the currently opened interface, if
	 * 		there is one.
	 */
	public void showScreenInterface(int interfaceId, boolean force) {
		if (!force) {
			if (screenInterfaceId != -1) {
				player.getTransmitter().sendMessage("You need to close the interface you have open before doing this.", false);
				return;
			}
		}
		player.getTransmitter().send(new InterfaceDisplayBuilder(isFixed() ? SCREEN_FIXED_WINDOW_ID : SCREEN_RESIZABLE_WINDOW_ID, isFixed() ? DISPLAY_FIXED_CHILD_ID : DISPLAY_RESIZABLE_CHILD_ID, screenInterfaceId = interfaceId, false).build(player));
	}
	
	/**
	 * Sends an interface over the chatbox
	 *
	 * @param interfaceId
	 * 		The id of the chatbox
	 */
	public void sendChatboxInterface(int interfaceId) {
		player.getTransmitter().send(new InterfaceDisplayBuilder(CHATBOX_WINDOW_ID, 9, this.chatboxInterfaceId = interfaceId, true).build(player));
	}
	
	/**
	 * Closes the interface on the chatbox and sends the original chatbox interface
	 */
	public void closeChatboxInterface() {
		player.getTransmitter().send(new InterfaceDisplayBuilder(CHATBOX_WINDOW_ID, 9, this.chatboxInterfaceId = REGULAR_CHATBOX_INTERFACE_ID, true).build(player));
	}
}
