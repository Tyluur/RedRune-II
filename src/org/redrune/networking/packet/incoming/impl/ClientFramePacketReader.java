package org.redrune.networking.packet.incoming.impl;

import org.redrune.cache.loaders.IComponentDefinitions;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.context.impl.ClientFramePacketContext;
import org.redrune.networking.packet.incoming.IncomingPacketReader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class ClientFramePacketReader implements IncomingPacketReader {
	
	@Override
	public int[] bindings() {
		return arguments(MOVE_MOUSE_PACKET, KEY_TYPED_PACKET, MOVE_CAMERA_PACKET, CLICK_PACKET, WINDOW_SWITCH_PACKET);
	}
	
	@Override
	public PacketContext read(Player player, Packet packet) {
		int packetId = packet.getOpcode();
		switch (packetId) {
			case MOVE_MOUSE_PACKET:
				// nothing
				break;
			case KEY_TYPED_PACKET: {
				int keyCode = packet.readByte();
				//				int unknown = packet.readByte();
				//				int unknown2 = packet.readUnsignedShort();
				
				switch (keyCode) {
					case 16: { // 1
						IComponentDefinitions optionComponent = player.getInterfaceManager().getDialogueInterfaceDefinitions("option1");
						if (optionComponent == null) {
							break;
						}
						player.getDialogueManager().continueDialogue(player.getInterfaceManager().getChatboxInterface(), optionComponent.getWidgetId());
						break;
					}
					case 17: { // 2
						IComponentDefinitions optionComponent = player.getInterfaceManager().getDialogueInterfaceDefinitions("option2");
						if (optionComponent == null) {
							break;
						}
						player.getDialogueManager().continueDialogue(player.getInterfaceManager().getChatboxInterface(), optionComponent.getWidgetId());
						break;
					}
					case 18: {// 3
						IComponentDefinitions optionComponent = player.getInterfaceManager().getDialogueInterfaceDefinitions("option3");
						if (optionComponent == null) {
							break;
						}
						player.getDialogueManager().continueDialogue(player.getInterfaceManager().getChatboxInterface(), optionComponent.getWidgetId());
						break;
					}
					case 19: {// 4
						IComponentDefinitions optionComponent = player.getInterfaceManager().getDialogueInterfaceDefinitions("option4");
						if (optionComponent == null) {
							break;
						}
						player.getDialogueManager().continueDialogue(player.getInterfaceManager().getChatboxInterface(), optionComponent.getWidgetId());
						break;
					}
					case 20: {// 5
						IComponentDefinitions optionComponent = player.getInterfaceManager().getDialogueInterfaceDefinitions("option5");
						if (optionComponent == null) {
							break;
						}
						player.getDialogueManager().continueDialogue(player.getInterfaceManager().getChatboxInterface(), optionComponent.getWidgetId());
						break;
					}
					case 13: // esc
						player.closeInterfaces();
						break;
					case 83: // space
						IComponentDefinitions continueComponent = player.getInterfaceManager().getDialogueInterfaceDefinitions("Click here to continue");
						if (continueComponent == null) {
							break;
						}
						player.getDialogueManager().continueDialogue(player.getInterfaceManager().getChatboxInterface(), continueComponent.getWidgetId());
						break;
				}
			}
			case MOVE_CAMERA_PACKET:
				break;
			case CLICK_PACKET: {
				int mouseHash = packet.readShortLE128();
				int mouseButton = mouseHash >> 15;
				int time = mouseHash - (mouseButton << 15); // time
				
				int positionHash = packet.readIntV1();
				int y = positionHash >> 16; // y;
				
				int x = positionHash - (y << 16); // x
				
				@SuppressWarnings("unused") boolean clicked;
				// mass click or stupid autoclicker, lets stop lagg
				if (time <= 1 || x < 0 || x > player.getInterfaceManager().getScreenWidth() || y < 0 || y > player.getInterfaceManager().getScreenHeight()) {
					// player.getSession().getChannel().close();
					clicked = false;
					break;
				}
				clicked = true;
				break;
			}
			case WINDOW_SWITCH_PACKET:
				// true if we swap to client, false if client is in backgrond
				boolean dominant = packet.readByte() == 1;
				break;
		}
		return new ClientFramePacketContext();
	}
}
