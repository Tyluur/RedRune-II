package org.redrune.game.module.interaction.rsinterface;

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
		return Misc.arguments(CHAT_SETUP_INTERFACE_ID, SCREEN_RESIZABLE_WINDOW_ID, SCREEN_FIXED_WINDOW_ID, OPTIONS_INTERFACE_ID, PRAYER_ORB_INTERFACE_ID, RUN_ORB_INTERACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		switch (interfaceId) {
			case RUN_ORB_INTERACE_ID:
				if (componentId == 1 && packetId == NetworkConstants.FIRST_PACKET_ID) {
					player.getVariables().setRunToggled(!player.getVariables().isRunToggled());
					player.sendSettings();
				}
				break;
			case CHAT_SETUP_INTERFACE_ID:
				switch (componentId) {
					case 5:
						player.getManager().getInterfaces().sendTab(GameTab.OPTIONS, GameTab.OPTIONS.getInterfaceId());
						return true;
				}
				break;
			case OPTIONS_INTERFACE_ID:
				switch (componentId) {
					case 3:
						player.getVariables().putAttribute(AttributeKey.FILTERING_PROFANITY, !player.getVariables().getAttribute(AttributeKey.FILTERING_PROFANITY, false));
						player.sendSettings();
						return true;
					case 4:
						player.getVariables().putAttribute(AttributeKey.CHAT_EFFECTS, !player.getVariables().getAttribute(AttributeKey.CHAT_EFFECTS, true));
						player.sendSettings();
						break;
					case 5:
						player.getManager().getInterfaces().sendTab(GameTab.OPTIONS, CHAT_SETUP_INTERFACE_ID);
						break;
					case 6:
						player.getVariables().putAttribute(AttributeKey.MOUSE_BUTTONS, player.getVariables().getAttribute(AttributeKey.MOUSE_BUTTONS, 0) == 0 ? 1 : 0);
						player.sendSettings();
						break;
					case 7:
						player.getVariables().putAttribute(AttributeKey.ACCEPTING_AID, !player.getVariables().getAttribute(AttributeKey.ACCEPTING_AID, false));
						player.sendSettings();
						break;
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
