package org.redrune.net.packet.context.impl;

import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.net.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class CapeColorCustomizationPacketContext extends PacketContext {
	
	/**
	 * The id of the color to use
	 */
	private final int colorId;
	
	public CapeColorCustomizationPacketContext(int colorId) {
		this.colorId = colorId;
	}
	
	@Override
	public void handle(Player player) {
		if (player.getTemporaryAttributes().get("SkillcapeCustomize") != null) {
			SkillCapeCustomizer.handleSkillCapeCustomizerColor(player, colorId);
		}
	}
}
