package org.redrune.networking.codec.packet.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.InputEvent;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class DialogueInteractionPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(DIALOGUE_CONTINUE_PACKET, ENTER_STRING_PACKET, ENTER_LONG_STRING_PACKET, ENTER_INTEGER_PACKET);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		switch (packetId) {
			case DIALOGUE_CONTINUE_PACKET: {
				int interfaceHash = stream.readIntV2();
				@SuppressWarnings("unused") int junk = stream.readShortLE128();
				int interfaceId = interfaceHash >> 16;
				@SuppressWarnings("unused") int buttonId = (interfaceHash & 0xFF);
				if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
					// hack, or server error or client error
					// player.getSession().getChannel().close();
					return;
				}
				if (!player.isRunning() || !player.getInterfaceManager().containsInterface(interfaceId)) {
					return;
				}
				int componentId = interfaceHash - (interfaceId << 16);
				player.getDialogueManager().continueDialogue(interfaceId, componentId);
				break;
			}
			case ENTER_STRING_PACKET: {
				if (!player.isRunning() || player.isDead()) {
					return;
				}
				String value = stream.readString();
				if (value.equals("")) {
					return;
				}
				if (player.getAttribute("input_event", null) != null) {
					InputEvent event = player.removeAttribute("input_event");
					event.setInput(value);
					event.handleInput();
					return;
				}
				if (player.getInterfaceManager().containsInterface(1108)) {
					player.getContactManager().setChatPrefix(value);
				}
				break;
			}
			case ENTER_LONG_STRING_PACKET: {
				String value = stream.readString();
				if (value.equals("")) {
					return;
				}
				if (player.getAttribute("input_event") != null) {
					InputEvent event = player.removeAttribute("input_event");
					event.setInput(value);
					event.handleInput();
				}
				break;
			}
			case ENTER_INTEGER_PACKET: {
				if (!player.isRunning() || player.isDead()) {
					return;
				}
				int value = stream.readInt();
				if (player.getAttribute("input_event") != null) {
					InputEvent event = player.removeAttribute("input_event");
					event.setInput(value);
					event.handleInput();
					return;
				}
				break;
			}
		}
	}
}
