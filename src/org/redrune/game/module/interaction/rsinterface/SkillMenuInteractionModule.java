package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.structure.out.VarpPacketBuilder;
import org.redrune.utility.Misc;

import static org.redrune.utility.AttributeKey.SKILL_MENU;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/28/2017
 */
public class SkillMenuInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return Misc.arguments(499);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		int skillMenu = player.getAttribute(SKILL_MENU, -1);
		if (componentId >= 10 && componentId <= 25) {
			player.getTransmitter().send(new VarpPacketBuilder(965, ((componentId - 10) * 1024) + skillMenu).build(player));
		} else if (componentId == 29) {
			//		TODO:	player.stopAll();
		}
		return true;
	}
}
