package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.content.dialogue.impl.misc.WorldMapDialogue;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.NetworkConstants;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.GameTab;

import static org.redrune.utility.rs.constant.InterfaceConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class GameframeInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return Misc.arguments(CHAT_SETUP_INTERFACE_ID, SCREEN_RESIZABLE_WINDOW_ID, SCREEN_FIXED_WINDOW_ID, OPTIONS_INTERFACE_ID, PRAYER_ORB_INTERFACE_ID, RUN_ORB_INTERACE_ID, LOGOUT_INTERFACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		switch (interfaceId) {
			case SCREEN_FIXED_WINDOW_ID:
			case SCREEN_RESIZABLE_WINDOW_ID:
				if (componentId == 179) {
					player.getManager().getDialogues().startDialogue(new WorldMapDialogue());
					return true;
				} else if (componentId == 0) {
					if (packetId == NetworkConstants.FIRST_PACKET_ID) {
						return true;
					} else if (packetId == NetworkConstants.DROP_PACKET_ID) {
						player.getSkills().resetExperienceCounter();
						return true;
					}
				}
				break;
			case RUN_ORB_INTERACE_ID:
				if (componentId == 1) {
					if (packetId == NetworkConstants.FIRST_PACKET_ID) {
						player.getVariables().setRunToggled(!player.getVariables().isRunToggled());
						player.sendSettings();
						return true;
					}
				}
				break;
			case PRAYER_ORB_INTERFACE_ID:
				if (componentId == 1) {
					if (packetId == NetworkConstants.FIRST_PACKET_ID) {
						player.getManager().getPrayers().toggleQuickPrayers();
						return true;
					} else if (packetId == NetworkConstants.SECOND_PACKET_ID) {
						player.getManager().getPrayers().selectQuickPrayers();
						return true;
					}
				}
				break;
			case CHAT_SETUP_INTERFACE_ID:
				switch (componentId) {
					case 5:
						player.getManager().getInterfaces().sendTab(GameTab.OPTIONS, GameTab.OPTIONS.getInterfaceId());
						return true;
				}
				break;
			case LOGOUT_INTERFACE_ID:
				player.logout(componentId == 6);
				return true;
			case OPTIONS_INTERFACE_ID:
				switch (componentId) {
					case 3:
						player.getVariables().putAttribute(AttributeKey.FILTERING_PROFANITY, !player.getVariables().getAttribute(AttributeKey.FILTERING_PROFANITY, false));
						player.sendSettings();
						return true;
					case 4:
						player.getVariables().putAttribute(AttributeKey.CHAT_EFFECTS, !player.getVariables().getAttribute(AttributeKey.CHAT_EFFECTS, true));
						player.sendSettings();
						return true;
					case 5:
						player.getManager().getInterfaces().sendTab(GameTab.OPTIONS, CHAT_SETUP_INTERFACE_ID);
						return true;
					case 6:
						player.getVariables().putAttribute(AttributeKey.MOUSE_BUTTONS, player.getVariables().getAttribute(AttributeKey.MOUSE_BUTTONS, 0) == 0 ? 1 : 0);
						player.sendSettings();
						return true;
					case 7:
						player.getVariables().putAttribute(AttributeKey.ACCEPTING_AID, !player.getVariables().getAttribute(AttributeKey.ACCEPTING_AID, true));
						player.sendSettings();
						return true;
					case 14:
						player.getManager().getInterfaces().sendInterface(742, false);
						return true;
					case 16:
						player.getManager().getInterfaces().sendInterface(743, false);
						return true;
				}
				break;
		}
		return false;
	}
}
