package com.rs.game.content.actor.player.design;

import com.rs.game.content.actor.player.design.DefaultDesign.DefaultSubDesign;
import com.rs.game.content.actor.player.design.DesignState.CustomizeCategory;
import com.rs.game.content.actor.player.design.DesignState.InterfaceState;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerAppearance;
import com.rs.utility.constants.DesignConstants;
import com.rs.utility.tools.RandomFunction;

import java.util.Random;

public class PlayerDesign {
	
	private static final Random RANDOM = new Random();
	
	/**
	 * Opens the player design screen.
	 */
	public static void open(Player player) {
		int interfaceId = 1028;
		
		player.getPackets().sendWindowsPane(interfaceId, 0);
		player.getPackets().sendUnlockIComponentOptionSlots(interfaceId, 45, 0, 11, 0);
		player.getPackets().sendUnlockIComponentOptionSlots(interfaceId, 107, 0, 50, 0);
		player.getPackets().sendUnlockIComponentOptionSlots(interfaceId, 111, 0, 250, 0);
		player.getPackets().sendConfig(1363, player.getAppearance().isMale() ? 8249 : 12345);
	}
	
	/**
	 * Handles all of the player's screen buttons.
	 *
	 * @param player
	 * 		The {@code Player} instance.
	 * @param buttonId
	 * 		The button id to use.
	 * @param slot
	 * 		the slot id to use.
	 */
	public static void handle(Player player, int buttonId, int slot) {
		PlayerAppearance appearance = player.getAppearance();
		DesignState state = player.getAttribute("design_state");
		if (state == null) {
			state = new DesignState();
			player.putAttribute("design_state", state);
		}
		switch (buttonId) {
			case 115:
				state.state = InterfaceState.CUSTOMIZATION;
				break;
			case 116:
				state.state = InterfaceState.MAIN;
				break;
			case 117:
				player.closeInterfaces();
				player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746 : 548, 0);
				player.getAttributes().remove("SelectWearDesignD");
				player.getAttributes().remove("ViewWearDesign");
				player.getAttributes().remove("ViewWearDesignD");
				player.getAppearance().generateAppearanceData();
				break;
			case 95:
			case 96:
			case 97:
			case 98:
			case 99:
			case 100:
				int ordinal = buttonId - 95;
				state.customIndex = CustomizeCategory.getCustomIndex(ordinal);
				break;
			case 45:
				player.getAppearance().setColor(4, DesignConstants.SKIN_COLOURS[slot]);
				break;
			case 107:
				if (appearance.isMale()) {
					switch (state.customIndex) {
						case HAIR:
							appearance.setLook(0, DesignConstants.MALE_HAIR_LOOKS[slot]);
							break;
						case TORSO:
							int[] torso = DesignConstants.MALE_TORSO_LOOKS[slot];
							appearance.setLook(2, torso[0] == -1 ? 0 : torso[0]);
							int arms = torso[1];
							if (arms != -1) {
								appearance.setLook(3, torso[1]);
							}
							appearance.setLook(4, torso[2] == -1 ? 0 : torso[2]);
							break;
						case LEGS:
							appearance.setLook(5, DesignConstants.MALE_LEG_LOOKS[slot]);
							break;
						case SHOES:
							appearance.setLook(6, DesignConstants.MALE_FEET_LOOKS[slot]);
							break;
						case FACIAL_HAIR:
							appearance.setLook(1, DesignConstants.MALE_FACIAL_LOOKS[slot]);
							break;
						case SKIN:
							break;
						default:
							break;
					}
				} else {
					switch (state.customIndex) {
						case HAIR:
							appearance.setLook(0, DesignConstants.FEMALE_HAIR_LOOKS[slot]);
							break;
						case TORSO:
							int[] torso = DesignConstants.FEMALE_TORSO_LOOKS[slot];
							appearance.setLook(2, torso[0] == -1 ? 0 : torso[0]);
							int arms = torso[1];
							if (arms != -1) {
								appearance.setLook(3, torso[1]);
							}
							appearance.setLook(4, torso[2] == -1 ? 0 : torso[2]);
							break;
						case LEGS:
							appearance.setLook(5, DesignConstants.FEMALE_LEG_LOOKS[slot]);
							break;
						case SHOES:
							appearance.setLook(6, DesignConstants.FEMALE_FEET_LOOKS[slot]);
							break;
						case FACIAL_HAIR:
							break;
						case SKIN:
							break;
						default:
							break;
					}
				}
				break;
			case 111: // modify colours
				switch (state.customIndex) {
					case SKIN:
						appearance.setColor(4, DesignConstants.SKIN_COLOURS[slot]);
						break;
					case HAIR:
						appearance.setColor(0, DesignConstants.HAIR_COLOURS[slot]);
						break;
					case TORSO:
						appearance.setColor(1, DesignConstants.CLOTH_COLOURS[slot]);
						break;
					case LEGS:
						appearance.setColor(2, DesignConstants.CLOTH_COLOURS[slot]);
						break;
					case SHOES:
						appearance.setColor(3, DesignConstants.FEET_COLOURS[slot]);
						break;
					case FACIAL_HAIR:
						if (appearance.isMale()) {
							appearance.setColor(0, DesignConstants.HAIR_COLOURS[slot]);
						}
						break;
				}
				break;
			case 38:
				if (!appearance.isMale()) {
					appearance.setMale(false);
					appearance.setLook(0, 5);
					appearance.setLook(1, 14);
					setDefaultLook(appearance, state.designIndex, state.secondaryDesignIndex);
				}
				break;
			case 39:
				if (appearance.isMale()) {
					appearance.setLook(0, 141);
					appearance.setMale(true);
					appearance.setLook(1, 9); // TODO check if correct
					setDefaultLook(appearance, state.designIndex, state.secondaryDesignIndex);
				}
				break;
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 56:
			case 57:
			case 58:
			case 59:
			case 60:
			case 61:
			case 62:
			case 63:
			case 64:
			case 65:
			case 66:
			case 67:
				setDefaultLook(appearance, buttonId - 48, 0);
				state.designIndex = buttonId - 48;
				break;
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
				setDefaultLook(appearance, state.designIndex, buttonId - 83);
				state.secondaryDesignIndex = buttonId - 83;
				break;
			case 120:
				int index = DesignConstants.CLOTH_COLOURS[RandomFunction.random(DesignConstants.CLOTH_COLOURS.length)];
				appearance.setColor(1, index);
				player.getPackets().sendConfig(1016, index);
				index = DesignConstants.CLOTH_COLOURS[RandomFunction.random(DesignConstants.CLOTH_COLOURS.length)];
				appearance.setColor(2, index);
				player.getPackets().sendConfig(1016, index);
				break;
		}
	}
	
	private static void setDefaultLook(PlayerAppearance app, int primaryIndex, int secondaryIndex) {
		if (primaryIndex == -1) {
			primaryIndex = 0;
		}
		if (secondaryIndex == -1) {
			secondaryIndex = 0;
		}
		if (app.isMale()) {
			DefaultDesign dd = DesignConstants.MALE_DEFAULT_DESIGNS[primaryIndex];
			DefaultSubDesign dsd = dd.getSubDesigns()[secondaryIndex];
			for (int[] i : dsd.getLooks()) {
				int j = i[1];
				if (j != -1) {
					app.setLook(i[0], j);
				}
				
			}
			for (int[] i : dd.getColours()) {
				app.setColor(i[0], i[1]);
			}
		} else {
			DefaultDesign dd = DesignConstants.FEMALE_DEFAULT_DESIGNS[primaryIndex];
			DefaultSubDesign dsd = dd.getSubDesigns()[secondaryIndex];
			for (int[] i : dsd.getLooks()) {
				int j = i[1];
				if (j != -1) {
					app.setLook(i[0], j);
				}
				
			}
			for (int[] i : dd.getColours()) {
				app.setColor(i[0], i[1]);
			}
		}
	}
	
}