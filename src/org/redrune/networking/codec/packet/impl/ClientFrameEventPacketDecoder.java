package org.redrune.networking.codec.packet.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.codec.packet.IncomingPacketDecoder;
import org.redrune.networking.stream.InputStream;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public class ClientFrameEventPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(MOVE_MOUSE_PACKET, KEY_TYPED_PACKET, MOVE_CAMERA_PACKET, CLICK_PACKET, WINDOW_SWITCH_PACKET);
	}
	
	@Override
	public void decode(Player player, InputStream stream, int packetId, int packetLength) {
		switch (packetId) {
			case MOVE_MOUSE_PACKET:
				// nothing
				break;
			case KEY_TYPED_PACKET:
				break;
			case MOVE_CAMERA_PACKET:
				// not using it atm
				stream.readUnsignedShort();
				stream.readUnsignedShort();
				break;
			case CLICK_PACKET: {
				int mouseHash = stream.readShortLE128();
				int mouseButton = mouseHash >> 15;
				int time = mouseHash - (mouseButton << 15); // time
				
				int positionHash = stream.readIntV1();
				int y = positionHash >> 16; // y;
				
				int x = positionHash - (y << 16); // x
				
				@SuppressWarnings("unused") boolean clicked;
				// mass click or stupid autoclicker, lets stop lagg
				if (time <= 1 || x < 0 || x > player.getInterfaceManager().getScreenWidth() || y < 0 || y > player.getInterfaceManager().getScreenHeight()) {
					// player.getSession().getChannel().close();
					clicked = false;
					return;
				}
				clicked = true;
				break;
			}
			case WINDOW_SWITCH_PACKET:
				// true if we swap to client, false if client is in backgrond
				boolean dominant = stream.readByte() == 1;
				break;
		}
	}
}
