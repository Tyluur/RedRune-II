package org.redrune.rs2.node.entity.player.components.managers;

import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.Misc;

/**
 * InterfaceManager.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class InterfaceManager {

	public enum DisplayMode {

		FIXED_DISPLAY(548),

		RESIZEABLE_DISPLAY(746);

		private final int paneId;

		private DisplayMode(int paneId) {
			this.paneId = paneId;
		}

		public int getPaneId() {
			return paneId;
		}

	}

	private final Player player;

	private DisplayMode displayMode;

	public InterfaceManager(Player player) {
		this.player = player;
	}

	public void sendInterface(int interfaceId) {
		player.getPacketSender().sendInterface(displayMode.getPaneId(), interfaceId,
				displayMode.equals(DisplayMode.FIXED_DISPLAY) ? 47 : 31, false);
	}

	public void closeInterfaces() {
		player.getPacketSender().sendCloseInterface(displayMode.getPaneId(),
				displayMode.equals(DisplayMode.FIXED_DISPLAY) ? 47 : 31);
	}

	public void sendChatBoxInterface(int interfaceId) {
		player.getPacketSender().sendInterface(752, interfaceId, 13, true);
	}

	public void closeChatBoxInterface() {
		player.getPacketSender().sendCloseInterface(752, 13);
	}

	public void sendInterfaces() {
		player.getPacketSender().sendGamePane(displayMode.getPaneId(), 0);
		switch (displayMode) {
		case FIXED_DISPLAY:
			player.getPacketSender().sendInterface(548, 751, 57, true);// CHATBOX
																		// TABS
			player.getPacketSender().sendInterface(548, 752, 16, true);// CHATBOX
			player.getPacketSender().sendInterface(548, 748, 164, true);// HP
																		// ORB
			player.getPacketSender().sendInterface(548, 749, 165, true);// PRAYERS
																		// ORB
			player.getPacketSender().sendInterface(548, 750, 166, true);// RUN
																		// ENERGY
																		// ORB
			player.getPacketSender().sendInterface(548, 747, 168, true);// SUMMONING
																		// ORB
			player.getPacketSender().sendInterface(752, 137, 9, true);// CHATBOX
																		// OVERLAY
			player.getPacketSender().sendInterface(548, 34, 193, true);// NOTES
																		// TAB
			player.getPacketSender().sendInterface(548, 187, 192, true);// MUSIC
																		// TAB
			player.getPacketSender().sendInterface(548, 590, 191, true);// EMOTES
																		// TAB
			player.getPacketSender().sendInterface(548, 261, 190, true);// SETTINGS
																		// TAB
			player.getPacketSender().sendInterface(548, 1110, 189, true);// CLAN
																			// TAB
			player.getPacketSender().sendInterface(548, 1109, 188, true);// FRIENDS
																			// CHAT
																			// TAB
			player.getPacketSender().sendInterface(548, 550, 187, true);// FRIENDS
																		// LIST
																		// TAB
			player.getPacketSender().sendInterface(548, 1139, 186, true);// EXTRAS
																			// TAB
			player.getPacketSender().sendInterface(548, 275, 185, true);// ABILITY
																		// BOOK
																		// TAB
			player.getPacketSender().sendInterface(548, 271, 184, true);// PRAYERS
																		// TAB
			player.getPacketSender().sendInterface(548, 387, 183, true);// EQUIPMENT
																		// TAB
			player.getPacketSender().sendInterface(548, 679, 182, true);// INVENTORY
																		// TAB
			player.getPacketSender().sendInterface(548, 1374, 181, true);// PERIPHERALS
																			// TAB
			player.getPacketSender().sendInterface(548, 320, 180, true);// SKILLS
																		// TAB
			player.getPacketSender().sendInterface(548, 1056, 179, true);// NOTICEBOARD
																			// TAB
			player.getPacketSender().sendInterface(548, 464, 178, true);// COMBAT
																		// TAB
			player.getPacketSender().sendInterface(548, 640, 19, true);// ACTION
																		// BAR
			player.getPacketSender().sendInterface(548, 182, 196, true);// LOGOUT
																		// TAB
			player.getPacketSender().sendInterface(548, 1213, 51, true);// SKILL
																		// PROGRESS
																		// ORB
			break;
		case RESIZEABLE_DISPLAY:
			player.getPacketSender().sendInterface(746, 752, 24, true);// CHATBOX
			player.getPacketSender().sendInterface(746, 751, 25, true);// CHATBOX
																		// TABS
			player.getPacketSender().sendInterface(746, 748, 198, true);// HP
																		// ORB
			player.getPacketSender().sendInterface(746, 749, 199, true);// PRAYERS
																		// ORB
			player.getPacketSender().sendInterface(746, 750, 200, true);// RUN
																		// ENERGY
																		// ORB
			player.getPacketSender().sendInterface(746, 747, 201, true);// SUMMONING
																		// ORB
			player.getPacketSender().sendInterface(752, 137, 9, true);// CHATBOX
																		// OVERLAY
			player.getPacketSender().sendInterface(746, 34, 129, true);// NOTES
																		// TAB
			player.getPacketSender().sendInterface(746, 187, 128, true);// MUSIC
																		// TAB
			player.getPacketSender().sendInterface(746, 590, 127, true);// EMOTES
																		// TAB
			player.getPacketSender().sendInterface(746, 261, 126, true);// SETTINGS
																		// TAB
			player.getPacketSender().sendInterface(746, 1110, 125, true);// CLAN
																			// TAB
			player.getPacketSender().sendInterface(746, 1109, 124, true);// FRIENDS
																			// CHAT
																			// TAB
			player.getPacketSender().sendInterface(746, 550, 123, true);// FRIENDS
																		// LIST
																		// TAB
			player.getPacketSender().sendInterface(746, 1139, 122, true);// EXTRAS
																			// TAB
			player.getPacketSender().sendInterface(746, 275, 121, true);// ABILITY
																		// BOOK
																		// TAB
			player.getPacketSender().sendInterface(746, 271, 120, true);// PRAYERS
																		// TAB
			player.getPacketSender().sendInterface(746, 387, 119, true);// EQUIPMENT
																		// TAB
			player.getPacketSender().sendInterface(746, 679, 118, true);// INVENTORY
																		// TAB
			player.getPacketSender().sendInterface(746, 1374, 117, true);// PERIPHERALS
																			// TAB
			player.getPacketSender().sendInterface(746, 320, 116, true);// SKILLS
																		// TAB
			player.getPacketSender().sendInterface(746, 1056, 115, true);// NOTICEBOARD
																			// TAB
			player.getPacketSender().sendInterface(746, 464, 114, true);// COMBAT
																		// TAB
			player.getPacketSender().sendInterface(746, 640, 26, true);// ACTION
																		// BAR
			player.getPacketSender().sendInterface(746, 182, 132, true);// LOGOUT
																		// TAB
			player.getPacketSender().sendInterface(746, 1213, 46, true);// SKILL
																		// PROGRESS
																		// ORB
			break;
		}
		refreshCombatStyles();
	}

	public void refreshCombatStyles() {
		player.getPacketSender().sendStringOnChild(464, 7,
				Misc.formatPlayerNameForDisplay(player.getDetails().getUsername()));
		player.getPacketSender().sendStringOnChild(464, 8, Integer.toString(player.getSkills().getCombatLevel()));
		player.getPacketSender().sendPlayerOnChild(464, 1);
		player.getPacketSender().sendAnimationOnChild(464, 0, 9804);
		player.getPacketSender().sendAnimationOnChild(464, 1, 9804);
	}

	public void sendCombatExperienceTab() {
		player.getPacketSender().sendInterface(displayMode.getPaneId(), 723,
				displayMode.equals(DisplayMode.FIXED_DISPLAY) ? 178 : 114, true);
	}

	public void sendCombatStylesTab() {
		player.getPacketSender().sendInterface(displayMode.getPaneId(), 464,
				displayMode.equals(DisplayMode.FIXED_DISPLAY) ? 178 : 114, true);
		refreshCombatStyles();
	}

	public Player getPlayer() {
		return player;
	}

	public DisplayMode getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(DisplayMode displayMode) {
		this.displayMode = displayMode;
	}

}
