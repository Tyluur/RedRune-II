package com.rs.networking.codec.decode.handlers;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.PluginRepository;
import com.rs.networking.io.InputStream;
import com.rs.utility.Misc;

/**
 * @author Matrix Team
 * @author Tyluur <itstyluur@gmail.com>
 */
public class ButtonHandler {
	
	/**
	 * Decodes the interface packet stream received from any interaction with an interface
	 *
	 * @param player
	 * 		The player
	 * @param stream
	 * 		The stream
	 * @param packetId
	 * 		The id of the packet
	 */
	public static void decodeInterfaceStream(final Player player, InputStream stream, int packetId) {
		int interfaceHash = stream.readIntV2();
		int interfaceId = interfaceHash >> 16;
		if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
			return;
		}
		if (player.isDead() || !player.getInterfaceManager().containsInterface(interfaceId)) {
			return;
		}
		final int componentId = interfaceHash - (interfaceId << 16);
		if (componentId != 65535 && Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
			return;
		}
		final int itemId = stream.readUnsignedShortLE128();
		final int slotId = stream.readUnsignedShort();
		if (!player.getControllerManager().processButtonClick(interfaceId, componentId, slotId, packetId)) {
			return;
		}
		PluginRepository.handleInterface(player, interfaceId, componentId, itemId, slotId, packetId);
	}
	
}