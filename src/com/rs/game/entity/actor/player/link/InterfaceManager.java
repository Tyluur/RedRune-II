package com.rs.game.entity.actor.player.link;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerInventory;

import java.util.concurrent.ConcurrentHashMap;

public class InterfaceManager {
	
	public static final int FIXED_WINDOW_ID = 548;
	
	public static final int RESIZABLE_WINDOW_ID = 746;
	
	public static final int CHAT_BOX_TAB = 13;
	
	public static final int FIXED_SCREEN_TAB_ID = 9;
	
	public static final int FIXED_SCREEN2_TAB_ID = 11;
	
	public static final int RESIZABLE_SCREEN_TAB_ID = 12;
	
	public static final int FIXED_INV_TAB_ID = 199;
	
	public static final int RESIZABLE_INV_TAB_ID = 87;
	
	private final ConcurrentHashMap<Integer, int[]> openedinterfaces = new ConcurrentHashMap<>();
	
	private Player player;
	
	private boolean resizableScreen;
	
	private int windowsPane;
	
	public InterfaceManager(Player player) {
		this.player = player;
	}
	
	public void sendChatBoxInterface(int interfaceId) {
		player.getPackets().sendInterface(true, 752, CHAT_BOX_TAB, interfaceId);
	}
	
	public void closeChatBoxInterface() {
		player.getPackets().closeInterface(CHAT_BOX_TAB);
	}
	
	public void sendOverlay(int interfaceId, boolean fullScreen) {
		sendTab(resizableScreen ? fullScreen ? 1 : 11 : 0, interfaceId);
	}
	
	public void sendTab(int tabId, int interfaceId) {
		player.getPackets().sendInterface(true, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, tabId, interfaceId);
	}
	
	public void closeOverlay(boolean fullScreen) {
		player.getPackets().closeInterface(resizableScreen ? fullScreen ? 1 : 11 : 0);
	}
	
	public void sendInterface(int interfaceId) {
		player.getPackets().sendInterface(false, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID, interfaceId);
	}
	
	public void sendInterfacec(int interfaceId) {
		player.getPackets().sendInterface(true, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID, interfaceId);
	}
	
	public void sendInventoryInterface(int childId) {
		player.getPackets().sendInterface(false, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID, childId);
	}
	
	public final void sendInterfaces() {
		if (player.getDisplayMode() == 2 || player.getDisplayMode() == 3) {
			resizableScreen = true;
			sendFullScreenInterfaces();
		} else {
			resizableScreen = false;
			sendFixedInterfaces();
		}
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		player.getMusicsManager().unlockMusicPlayer();
		player.getInventory().unlockInventoryOptions();
		player.getPrayer().unlockPrayerBookButtons();
		if (player.getFamiliar() != null && player.isRunning()) {
			player.getFamiliar().unlock();
		}
		player.getControllerManager().sendInterfaces();
	}
	
	public void replaceRealChatBoxInterface(int interfaceId) {
		player.getPackets().sendInterface(true, 752, 12, interfaceId);
	}
	
	public void closeReplacedRealChatBoxInterface() {
		player.getPackets().closeInterface(752, 12);
	}
	
	public void sendFixedInterfaces() {
		player.getPackets().sendWindowsPane(548, 0);
		// Blank Interface
		sendTab(15, 745);
		
		// ChatBox Tabs
		sendTab(68, 751);
		
		// ChatBox Interface
		sendTab(192, 752);
		player.getPackets().sendInterface(true, 752, 9, 137);
		
		// Blank Interface
		sendTab(17, 754);
		
		// HP Orb
		sendTab(183, 748);
		
		// Prayer Orb
		sendTab(185, 749);
		
		// Energy Orb
		sendTab(186, 750);
		
		// Summoning Orb
		sendTab(188, 747);
		
		// Combat Interface
		sendTab(204, 884);
		
		// Tasks Interface
		sendTab(205, 930);
		// Skills Interface
		sendTab(206, 320);
		
		// quests Interface
		sendQuestTab();
		//	Notes.sendUnlockNotes(player);
		// Inventory Interface
		sendInventory();
		
		// Equipment Interface
		sendEquipment();
		
		// Prayer Interface
		sendPrayerBook();
		
		// Magic Interface
		sendMagicBook();
		
		// Friends Interface
		sendTab(213, 550);
		
		// Friends Chat Interface
		sendTab(214, 1109);
		
		// Clan Chat Interface
		sendTab(215, 1110);
		
		// Settings Interface
		sendSettings();
		
		// Emotes Interface
		sendTab(217, 464);
		
		// Music Interface
		sendTab(218, 187);
		
		//// notes Interface
		sendTab(219, 34);
		
		// Logout Interface
		sendTab(222, 182); // Logout tab
	}
	
	public void sendFullScreenInterfaces() {
		player.getPackets().sendWindowsPane(746, 0);
		// Blank Interface
		sendTab(15, 745);
		
		// ChatBox Tabs
		sendTab(19, 751);
		
		// ChatBox Interface
		sendTab(73, 752);
		player.getPackets().sendInterface(true, 752, 9, 137);
		
		// Blank Interface
		sendTab(72, 754);
		
		// HP Orb
		sendTab(177, 748);
		
		// Prayer Orb
		sendTab(178, 749);
		
		// Energy Orb
		sendTab(179, 750);
		
		// Summoning Orb
		sendTab(180, 747);
		
		// Combat Interface
		sendTab(90, 884);
		
		// Tasks Interface
		sendTab(91, 930);
		
		// Skills Interface
		sendTab(92, 320);
		
		// Quests Interface
		sendQuestTab();
		
		// Inventory Interface
		sendInventory();
		
		// Equipment Interface
		sendEquipment();
		
		// Prayer Interface
		sendPrayerBook();
		
		// Magic Interface
		sendMagicBook();
		
		// Friends Interface
		sendTab(99, 550);
		
		// Friend Chat Interface
		sendTab(100, 1109);
		
		// Clan Chat Interface (Interface 589 = Lobby Clan Chat)
		sendTab(101, 1110);
		
		// Settings Interface
		sendSettings();
		
		// Emotes Interface
		sendTab(103, 464);
		
		// Music Interface
		sendTab(104, 187);
		
		// Notes Interface
		sendTab(105, 34);
		NoteManager.refresh(player, true);
		// Logout Interface
		sendTab(108, 182);
	}
	
	public void sendCombatStyles() {
		sendTab(resizableScreen ? 111 : 204, 884);
	}
	
	public void sendTaskSystem() {
		sendTab(resizableScreen ? 112 : 205, 930);
	}
	
	public void sendEmotes() {
		sendTab(resizableScreen ? 124 : 217, 464);
	}
	
	public void sendFriends() {
		sendTab(resizableScreen ? 100 : 214, 550);
	}
	
	public void sendQuestTab() {
		sendTab(resizableScreen ? 93 : 207, 1149);
	}
	
	public void sendFriendsChat() {
		sendTab(resizableScreen ? 100 : 214, 1109);
	}
	
	public void sendClanChat() {
		sendTab(resizableScreen ? 101 : 215, 1110);
	}
	
	public void sendMusic() {
		sendTab(resizableScreen ? 104 : 218, 187);
	}
	
	public void sendNotes() {
		sendTab(resizableScreen ? 105 : 219, 34);
	}
	
	public void sendEquipment() {
		sendTab(resizableScreen ? 95 : 209, 387);
	}
	
	public void closeQuestTab() {
		player.getPackets().closeInterface(resizableScreen ? 93 : 207);
	}
	
	public void closeFriendsChat() {
		player.getPackets().closeInterface(resizableScreen ? 100 : 214);
	}
	
	public void closeClanChat() {
		player.getPackets().closeInterface(resizableScreen ? 101 : 215);
	}
	
	public void closeSettings() {
		player.getPackets().closeInterface(resizableScreen ? 102 : 216);
	}
	
	public void closeMusic() {
		player.getPackets().closeInterface(resizableScreen ? 104 : 218);
	}
	
	public void closeNotes() {
		player.getPackets().closeInterface(resizableScreen ? 105 : 219);
	}
	
	public void closeFriends() {
		player.getPackets().closeInterface(resizableScreen ? 99 : 213);
	}
	
	public void closeEquipment() {
		player.getPackets().closeInterface(resizableScreen ? 95 : 209);
	}
	
	public void sendInventory() {
		sendTab(resizableScreen ? 94 : 208, PlayerInventory.INVENTORY_INTERFACE);
	}
	
	public void closeInventory() {
		player.getPackets().closeInterface(resizableScreen ? 94 : 208);
	}
	
	public void sendSkills() {
		sendTab(resizableScreen ? 30 : 151, 320);
	}
	
	public void sendSettings() {
		sendSettings(261);
	}
	
	public void sendSettings(int interfaceId) {
		sendTab(resizableScreen ? 102 : 216, interfaceId);
	}
	
	public void sendPrayerBook() {
		sendTab(resizableScreen ? 96 : 210, 271);
	}
	
	public void sendMagicBook() {
		sendTab(resizableScreen ? 97 : 211, player.getCombatDefinitions().getSpellBook());
	}
	
	public boolean addInterface(int windowId, int tabId, int childId) {
		if (openedinterfaces.containsKey(tabId)) {
			player.getPackets().closeInterface(tabId);
		}
		openedinterfaces.put(tabId, new int[] { childId, windowId });
		return openedinterfaces.get(tabId)[0] == childId;
	}
	
	public boolean containsInterface(int tabId, int childId) {
		if (childId == windowsPane) {
			return true;
		}
		if (!openedinterfaces.containsKey(tabId)) {
			return false;
		}
		return openedinterfaces.get(tabId)[0] == childId;
	}
	
	public int getTabWindow(int tabId) {
		if (!openedinterfaces.containsKey(tabId)) {
			return FIXED_WINDOW_ID;
		}
		return openedinterfaces.get(tabId)[1];
	}
	
	public boolean containsInterface(int childId) {
		if (childId == windowsPane) {
			return true;
		}
		for (int[] value : openedinterfaces.values()) {
			if (value[0] == childId) {
				return true;
			}
		}
		return false;
	}
	
	public void removeAll() {
		openedinterfaces.clear();
	}
	
	public boolean containsScreenInter() {
		return containsTab(resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID);
	}
	
	public boolean containsTab(int tabId) {
		return openedinterfaces.containsKey(tabId);
	}
	
	public void closeInterface(int one, int two) {
		player.getPackets().closeInterface(resizableScreen ? two : one);
	}
	
	public void closePrayerBook() {
		player.getPackets().closeInterface(resizableScreen ? 117 : 210);
	}
	
	public void closeMagicBook() {
		player.getPackets().closeInterface(resizableScreen ? 118 : 211);
	}
	
	public void closeEmotes() {
		player.getPackets().closeInterface(resizableScreen ? 124 : 217);
	}
	
	public void closeSkills() {
		player.getPackets().closeInterface(resizableScreen ? 113 : 206);
	}
	
	public void closeCombatStyles() {
		player.getPackets().closeInterface(resizableScreen ? 111 : 204);
	}
	
	public void closeTaskSystem() {
		player.getPackets().closeInterface(resizableScreen ? 112 : 205);
	}
	
	public boolean containsInventoryInter() {
		return containsTab(resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID);
	}
	
	public void closeInventoryInterface() {
		player.getPackets().closeInterface(resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID);
	}
	
	public boolean containsChatBoxInter() {
		return containsTab(CHAT_BOX_TAB);
	}
	
	public boolean removeTab(int tabId) {
		return openedinterfaces.remove(tabId) != null;
	}
	
	public boolean removeInterface(int tabId, int childId) {
		if (!openedinterfaces.containsKey(tabId)) {
			return false;
		}
		if (openedinterfaces.get(tabId)[0] != childId) {
			return false;
		}
		return openedinterfaces.remove(tabId) != null;
	}
	
	public void sendScreenInterface(int backgroundInterface, int interfaceId) {
		player.getInterfaceManager().closeScreenInterface();
		
		if (hasRezizableScreen()) {
			player.getPackets().sendInterface(false, RESIZABLE_WINDOW_ID, 9, backgroundInterface);
			player.getPackets().sendInterface(false, RESIZABLE_WINDOW_ID, 11, interfaceId);
		} else {
			player.getPackets().sendWindowsPane(interfaceId, 0);
		}
		
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				if (hasRezizableScreen()) {
					player.getPackets().closeInterface(9);
					player.getPackets().closeInterface(11);
				} else {
					player.getPackets().sendWindowsPane(FIXED_WINDOW_ID, 0);
				}
			}
		});
	}
	
	public void closeScreenInterface() {
		player.getPackets().closeInterface(resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID);
	}
	
	public boolean hasRezizableScreen() {
		return resizableScreen;
	}
	
	public int getWindowsPane() {
		return windowsPane;
	}
	
	public void setWindowsPane(int windowsPane) {
		this.windowsPane = windowsPane;
	}
	
	public int openGameTab(int tabId) {
		player.getPackets().sendGlobalConfig(168, tabId);
		int lastTab = 4;
		return lastTab;
	}
	
	public boolean GotResisableScreen() {
		// TODO Auto-generated method stub
		return resizableScreen;
	}
	
}