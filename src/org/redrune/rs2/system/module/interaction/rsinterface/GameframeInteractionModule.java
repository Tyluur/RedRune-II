package org.redrune.rs2.system.module.interaction.rsinterface;

import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.system.module.type.InterfaceInteractionModule;
import org.redrune.utility.Misc;

import static org.redrune.utility.rs.constant.InterfaceConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class GameframeInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return Misc.arguments(SCREEN_RESIZABLE_WINDOW_ID, SCREEN_FIXED_WINDOW_ID, OPTIONS_INTERFACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		switch (interfaceId) {
			case OPTIONS_INTERFACE_ID:
				if (componentId == 16) {
					player.getInterfaceManager().showScreenInterface(743, false);
					return true;
				} else if (componentId == 14) {
					player.getInterfaceManager().showScreenInterface(742, false);
					return true;
				}
				break;
		}
		return false;
	}
}
