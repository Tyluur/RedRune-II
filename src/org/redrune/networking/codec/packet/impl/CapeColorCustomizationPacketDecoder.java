package org.redrune.networking.codec.packet.impl;

import org.redrune.game.content.SkillCapeCustomizer;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class CapeColorCustomizationPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(COLOR_ID_PACKET);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		if (!player.hasStarted()) {
			return;
		}
		int colorId = stream.readUnsignedShort();
		if (player.getTemporaryAttributtes().get("SkillcapeCustomize") != null) {
			SkillCapeCustomizer.handleSkillCapeCustomizerColor(player, colorId);
		}
	}
}
