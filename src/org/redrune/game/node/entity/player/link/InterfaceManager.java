package org.redrune.game.node.entity.player.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.structure.out.CloseInterfaceBuilder;
import org.redrune.network.rs666.packet.structure.out.GameWindowBuilder;
import org.redrune.network.rs666.packet.structure.out.InterfaceDisplayBuilder;
import org.redrune.utility.rs.GameTab;
import org.redrune.utility.rs.constant.InterfaceConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class InterfaceManager implements InterfaceConstants {
	
	/**
	 * The bindings that store the data of the currently active interfaces. The key is the component id that an
	 * interface is drawn on. The values are an integer array. array[0] = interfaceId, array[1] = paneId.
	 */
	private final Map<Integer, int[]> interfaceBindings = new HashMap<>();
	
	/**
	 * The pane to draw the components on
	 */
	@Setter
	@Getter
	private int paneId;
	
	/**
	 * The player that owns this manager
	 */
	@Setter
	private transient Player player;
	
	public void sendLogin() {
		sendMainComponents();
		if (usingFixedMode()) {
			player.getTransmitter().sendFixedAMasks();
		} else {
			player.getTransmitter().sendFullScreenAMasks();
		}
	}
	
	/**
	 * Sends the main components
	 */
	private InterfaceManager sendMainComponents() {
		if (usingFixedMode()) {
			sendWindowPane(SCREEN_FIXED_WINDOW_ID);
			sendInterface(67, 751);
			sendInterface(FIXED_CHATBOX_COMPONENT_ID, CHATBOX_WINDOW_ID);
			sendInterface(16, 754);
			sendInterface(182, 748);
			sendInterface(184, 749);
			sendInterface(185, 750);
			sendInterface(187, 747);
			sendInterface(14, 745);
		} else {
			sendWindowPane(SCREEN_RESIZABLE_WINDOW_ID);
			sendInterface(18, 751);
			sendInterface(RESIZABLE_CHATBOX_COMPONENT_ID, CHATBOX_WINDOW_ID);
			sendInterface(72, 754);
			sendInterface(176, 748);
			sendInterface(177, 749);
			sendInterface(178, 750);
			sendInterface(179, 747);
			sendInterface(14, 745);
		}
		switch (player.getNetworkSession().getViewComponents().getScreenSizeMode()) {
			case 0:
			case 1:
				break;
			case 2:
			case 3:
				break;
		}
		sendInterface(CHATBOX_WINDOW_ID, 9, REGULAR_CHATBOX_INTERFACE_ID).sendDefaultTabs();
		return this;
	}
	
	/**
	 * Sends the default tabs.
	 */
	public InterfaceManager sendDefaultTabs() {
		for (GameTab data : GameTab.values()) {
			sendInterface(usingFixedMode() ? data.getFixedChildId() : data.getResizedChildId(), data.getInterfaceId());
		}
		return this;
	}
	
	/**
	 * If we have an interface open
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public boolean hasInterfaceOpen(int interfaceId) {
		if (interfaceId == paneId) {
			return true;
		}
		for (int[] values : interfaceBindings.values()) {
			if (values[0] == interfaceId) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sends a window pane
	 *
	 * @param paneId
	 * 		The id of the pane
	 */
	public InterfaceManager sendWindowPane(int paneId) {
		player.getTransmitter().send(new GameWindowBuilder(this.paneId = paneId, 0).build(player));
		return this;
	}
	
	/**
	 * Sends an interface over the chatbox
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public InterfaceManager sendChatboxInterface(int interfaceId) {
		return sendInterface(CHATBOX_WINDOW_ID, 13, interfaceId);
	}
	
	/**
	 * Sends an interface on the specified component id
	 *
	 * @param paneId
	 * 		The pane id to draw the interface on
	 * @param componentId
	 * 		The component id that the interface will be drawn on.
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public InterfaceManager sendInterface(int paneId, int componentId, int interfaceId) {
		if (interfaceBindings.get(componentId) != null) {
			closeInterface(paneId, componentId);
		}
		interfaceBindings.put(componentId, new int[] { interfaceId, paneId });
		flushComponent(componentId);
		return this;
	}
	
	/**
	 * Closes an interface
	 *
	 * @param paneId
	 * 		The pane the interface is on
	 * @param componentId
	 * 		The component the interface is on
	 */
	public InterfaceManager closeInterface(int paneId, int componentId) {
		interfaceBindings.remove(componentId);
		player.getTransmitter().send(new CloseInterfaceBuilder(paneId, componentId).build(player));
		return this;
	}
	
	/**
	 * Sends the interface to the client, using the component key to get the other data to send.
	 *
	 * @param componentId
	 * 		The component key
	 */
	private InterfaceManager flushComponent(int componentId) {
		int[] values = interfaceBindings.get(componentId);
		if (values == null) {
			return this;
		}
		boolean notTransparent = componentId == getScreenComponentId(usingFixedMode());
		player.getTransmitter().send(new InterfaceDisplayBuilder(values[1], componentId, values[0], !notTransparent).build(player));
		return this;
	}
	
	/**
	 * Gets the component id of the screen
	 *
	 * @param fixedMode
	 * 		If we are using fixed mode.
	 */
	private static int getScreenComponentId(boolean fixedMode) {
		return fixedMode ? DISPLAY_FIXED_CHILD_ID : DISPLAY_RESIZABLE_CHILD_ID;
	}
	
	/**
	 * If the player is using the fixed client mode.
	 */
	public boolean usingFixedMode() {
		return player.getNetworkSession().getViewComponents().usingFixedMode();
	}
	
	/**
	 * Closes the chatbox interface and sends the regular one
	 */
	public InterfaceManager closeChatboxInterface() {
		closeInterface(CHATBOX_WINDOW_ID, 13);
		return sendInterface(CHATBOX_WINDOW_ID, 9, REGULAR_CHATBOX_INTERFACE_ID).sendDefaultTabs();
	}
	
	/**
	 * Sends a regular screen interface
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 * @param force
	 * 		If we should force the interface to be shown (this means if we have a screen interface open, and force = false,
	 * 		it will not be shown)
	 */
	public InterfaceManager sendInterface(int interfaceId, boolean force) {
		if (!force && getScreenInterface() != -1) {
			player.getTransmitter().sendMessage("You need to close the interface you have open before doing this.", false);
			return this;
		}
		return sendInterface(getScreenPaneId(usingFixedMode()), getScreenComponentId(usingFixedMode()), interfaceId);
	}
	
	/**
	 * Gets the current interface that we are displaying on the screen
	 */
	public int getScreenInterface() {
		int[] values = interfaceBindings.get(getScreenComponentId(usingFixedMode()));
		if (values == null) {
			return -1;
		} else {
			return values[0];
		}
	}
	
	/**
	 * Gets the pane id for the mode we're on
	 *
	 * @param usingFixedMode
	 * 		If we are on fixed mode
	 */
	private static int getScreenPaneId(boolean usingFixedMode) {
		return usingFixedMode ? SCREEN_FIXED_WINDOW_ID : SCREEN_RESIZABLE_WINDOW_ID;
	}
	
	/**
	 * Gets the chatbox interface id
	 */
	public int getChatboxInterface() {
		for (Entry<Integer, int[]> entry : interfaceBindings.entrySet()) {
			if (entry.getKey() == 13 && entry.getValue()[1] == CHATBOX_WINDOW_ID) {
				return entry.getValue()[0];
			}
		}
		return -1;
	}
	
	/**
	 * Closes all the interfaces visible
	 */
	public InterfaceManager closeAllInterfaces() {
		if (getScreenInterface() != -1) {
			closeScreenInterface();
			System.out.println("Closed the screen interface");
		}
		if (getChatboxInterface() != -1) {
			System.out.println("Closed the chatbox interface");
			closeChatboxInterface();
		}
		return this;
	}
	
	/**
	 * Closes the interface we have open on the screen
	 */
	public InterfaceManager closeScreenInterface() {
		int componentId = getScreenComponentId(usingFixedMode());
		int[] values = interfaceBindings.get(componentId);
		if (values == null) {
			return this;
		}
		return closeInterface(getScreenPaneId(usingFixedMode()), componentId);
	}
	
	/**
	 * Sends an interface on a tab
	 *
	 * @param tab
	 * 		The tab to send it on
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public InterfaceManager sendTab(GameTab tab, int interfaceId) {
		return sendInterface(usingFixedMode() ? tab.getFixedChildId() : tab.getResizedChildId(), interfaceId);
	}
	
	/**
	 * Sends an interface on the specified component id
	 *
	 * @param componentId
	 * 		The component id that the interface will be drawn on.
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public InterfaceManager sendInterface(int componentId, int interfaceId) {
		return sendInterface(getScreenPaneId(usingFixedMode()), componentId, interfaceId);
	}
	
}