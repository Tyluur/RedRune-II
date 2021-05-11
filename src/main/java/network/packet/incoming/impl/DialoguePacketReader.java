package network.packet.incoming.impl;

import game.entity.actor.player.Player;
import network.packet.Packet;
import network.packet.context.PacketContext;
import network.packet.context.impl.DialogueChatPacketContext;
import network.packet.incoming.IncomingPacketReader;
import utility.functions.Misc;
import utility.game.InputEvent;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class DialoguePacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(DIALOGUE_CONTINUE_PACKET, ENTER_STRING_PACKET, ENTER_LONG_STRING_PACKET, ENTER_INTEGER_PACKET);
	}
	
	@Override
	public PacketContext read(Player player, Packet stream) {
		switch (stream.getOpcode()) {
			case DIALOGUE_CONTINUE_PACKET: {
				int interfaceHash = stream.readIntV2();
				@SuppressWarnings("unused") int junk = stream.readShortLE128();
				int interfaceId = interfaceHash >> 16;
				@SuppressWarnings("unused") int buttonId = (interfaceHash & 0xFF);
				if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
					break;
				}
				if (!player.isRunning() || !player.getInterfaceManager().containsInterface(interfaceId)) {
					break;
				}
				int componentId = interfaceHash - (interfaceId << 16);
				return new DialogueChatPacketContext(interfaceId, componentId);
			}
			case ENTER_STRING_PACKET: {
				if (!player.isRunning() || player.isDead()) {
					break;
				}
				String value = stream.readRS2String();
				if (value.equals("")) {
					break;
				}
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						if (player.getTemporaryAttribute("input_event", null) != null) {
							InputEvent event = player.removeTemporaryAttribute("input_event");
							event.setInput(value);
							event.handleInput();
							return;
						}
						if (player.getInterfaceManager().containsInterface(1108)) {
							player.getContactManager().setChatPrefix(value);
						}
					}
				};
			}
			case ENTER_LONG_STRING_PACKET: {
				String value = stream.readRS2String();
				if (value.equals("")) {
					break;
				}
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						if (player.getTemporaryAttribute("input_event") != null) {
							InputEvent event = player.removeTemporaryAttribute("input_event");
							event.setInput(value);
							event.handleInput();
						}
					}
				};
			}
			case ENTER_INTEGER_PACKET: {
				if (!player.isRunning() || player.isDead()) {
					break;
				}
				int value = stream.readInt();
				return new PacketContext() {
					@Override
					public void handle(Player player) {
						if (player.getTemporaryAttribute("input_event") != null) {
							InputEvent event = player.removeTemporaryAttribute("input_event");
							event.setInput(value);
							event.handleInput();
						}
					}
				};
			}
		}
		return null;
	}
}
