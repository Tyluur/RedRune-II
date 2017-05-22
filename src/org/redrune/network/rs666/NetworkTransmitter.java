package org.redrune.network.rs666;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.out.*;
import org.redrune.rs2.node.entity.player.Player;

/**
 * This class handles the transmission of all important packets directly to the client.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class NetworkTransmitter {
	
	/**
	 * The player
	 */
	private Player player;
	
	public NetworkTransmitter(Player player) {
		this.player = player;
	}
	
	/**
	 * Sends all the components necessary for a successful login
	 *
	 * @return A {@code NetworkTransmitter} {@code Object}
	 */
	public NetworkTransmitter sendLoginComponents() {
		send(new LoginCredentialsBuilder().build(player));
		send(new MapRegionBuilder(true).build(player));
		sendMainInterfaces();
		if (player.getNetworkSession().getViewComponents().getScreenSizeMode() > 1) {
			sendFullScreenAMasks();
		} else {
			sendFixedAMasks();
		}
		return this;
	}
	
	/**
	 * Sends the outgoing packet
	 *
	 * @param packet
	 * 		The outgoing packet
	 */
	public NetworkTransmitter send(Packet packet) {
		player.getNetworkSession().write(packet);
		return this;
	}
	
	/**
	 * Sends all interfaces to the client
	 *
	 * @return A {@code NetworkTransmitter} {@code Object}
	 */
	private NetworkTransmitter sendMainInterfaces() {
		switch (player.getNetworkSession().getViewComponents().getScreenSizeMode()) {
			case 0:
			case 1:
				send(new GameWindowBuilder(548, 0).build(player));
				send(new InterfaceDisplayBuilder(548, 67, 751, true).build(player));
				send(new InterfaceDisplayBuilder(548, 192, 752, true).build(player));
				send(new InterfaceDisplayBuilder(548, 16, 754, true).build(player));
				send(new InterfaceDisplayBuilder(548, 182, 748, true).build(player));
				send(new InterfaceDisplayBuilder(548, 184, 749, true).build(player));
				send(new InterfaceDisplayBuilder(548, 185, 750, true).build(player));
				send(new InterfaceDisplayBuilder(548, 187, 747, true).build(player));
				send(new InterfaceDisplayBuilder(548, 14, 745, true).build(player));
				send(new InterfaceDisplayBuilder(752, 9, 137, true).build(player));
				send(new InterfaceDisplayBuilder(548, 203, 884, true).build(player));
				send(new InterfaceDisplayBuilder(548, 205, 320, true).build(player));
				send(new InterfaceDisplayBuilder(548, 206, 190, true).build(player));
				send(new InterfaceDisplayBuilder(548, 204, 1056, true).build(player));
				send(new InterfaceDisplayBuilder(548, 207, 679, true).build(player));
				send(new InterfaceDisplayBuilder(548, 208, 387, true).build(player));
				send(new InterfaceDisplayBuilder(548, 209, 271, true).build(player));
				//				send(new InterfaceDisplayBuilder(548, 210, player.getSettings().getSpellBook(), true).build(player));
				send(new InterfaceDisplayBuilder(548, 204, 1056, true).build(player));
				send(new InterfaceDisplayBuilder(548, 212, 550, true).build(player));
				send(new InterfaceDisplayBuilder(548, 213, 1109, true).build(player));
				send(new InterfaceDisplayBuilder(548, 214, 1110, true).build(player));
				send(new InterfaceDisplayBuilder(548, 215, 261, true).build(player));
				send(new InterfaceDisplayBuilder(548, 216, 590, true).build(player));
				send(new InterfaceDisplayBuilder(548, 217, 187, true).build(player));
				send(new InterfaceDisplayBuilder(548, 218, 34, true).build(player));
				send(new InterfaceDisplayBuilder(548, 221, 182, true).build(player));
				send(new InterfaceDisplayBuilder(548, 203, 884, true).build(player));
				break;
			case 2:
			case 3:
				send(new GameWindowBuilder(746, 0).build(player));
				send(new InterfaceDisplayBuilder(746, 18, 751, true).build(player));
				send(new InterfaceDisplayBuilder(746, 71, 752, true).build(player));
				send(new InterfaceDisplayBuilder(746, 72, 754, true).build(player));
				send(new InterfaceDisplayBuilder(746, 176, 748, true).build(player));
				send(new InterfaceDisplayBuilder(746, 177, 749, true).build(player));
				send(new InterfaceDisplayBuilder(746, 178, 750, true).build(player));
				send(new InterfaceDisplayBuilder(746, 179, 747, true).build(player));
				send(new InterfaceDisplayBuilder(746, 14, 745, true).build(player));
				send(new InterfaceDisplayBuilder(752, 9, 137, true).build(player));
				send(new InterfaceDisplayBuilder(746, 89, 884, true).build(player));
				send(new InterfaceDisplayBuilder(746, 91, 320, true).build(player));
				send(new InterfaceDisplayBuilder(746, 92, 190, true).build(player));
				send(new InterfaceDisplayBuilder(746, 90, 1056, true).build(player));
				send(new InterfaceDisplayBuilder(746, 93, 679, true).build(player));
				send(new InterfaceDisplayBuilder(746, 94, 387, true).build(player));
				send(new InterfaceDisplayBuilder(746, 95, 271, true).build(player));
				//				send(new InterfaceDisplayBuilder(746, 96, player.getSettings().getSpellBook(), true).build(player));
				send(new InterfaceDisplayBuilder(746, 90, 1056, true).build(player));
				send(new InterfaceDisplayBuilder(746, 98, 550, true).build(player));
				send(new InterfaceDisplayBuilder(746, 99, 1109, true).build(player));
				send(new InterfaceDisplayBuilder(746, 100, 1110, true).build(player));
				send(new InterfaceDisplayBuilder(746, 101, 261, true).build(player));
				send(new InterfaceDisplayBuilder(746, 102, 590, true).build(player));
				send(new InterfaceDisplayBuilder(746, 103, 187, true).build(player));
				send(new InterfaceDisplayBuilder(746, 104, 34, true).build(player));
				send(new InterfaceDisplayBuilder(746, 107, 182, true).build(player));
				send(new InterfaceDisplayBuilder(746, 89, 884, true).build(player));
				break;
		}
		return this;
	}
	
	/**
	 * Sends the full screen access masks
	 *
	 * @return A {@code NetworkTransmitter} {@code Object}
	 */
	public NetworkTransmitter sendFullScreenAMasks() {
		send(new AccessMaskBuilder(0, 99, 137, 58, 0, 2046).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 39, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 11, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 12, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 13, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 41, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 42, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 300, 190, 18, 0, 14).build(player));
		send(new AccessMaskBuilder(0, 11, 190, 15, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 40, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 43, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 27, 679, 0, 69, 0x457d8e).build(player));
		send(new AccessMaskBuilder(28, 55, 679, 0, 32, 0).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 44, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 45, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 30, 271, 8, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 46, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 47, 0, 0).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 40, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 48, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 49, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 600, 1109, 5, 0, 1024).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 50, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 200, 1110, 11, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 600, 1110, 16, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 600, 1110, 14, 0, 1024).build(player));
		send(new AccessMaskBuilder(0, 600, 1110, 5, 0, 1024).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 51, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 52, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 93, 590, 8, 0, 6).build(player));
		send(new AccessMaskBuilder(0, 11, 590, 13, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 53, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 2033, 187, 1, 0, 26).build(player));
		send(new AccessMaskBuilder(0, 11, 187, 9, 36, 6).build(player));
		send(new AccessMaskBuilder(12, 23, 187, 9, 0, 4).build(player));
		send(new AccessMaskBuilder(24, 24, 187, 9, 32, 0).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 54, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 29, 34, 9, 40, 30).build(player));
		send(new AccessMaskBuilder(0, 0, 747, 17, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 0, 662, 74, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 746, 39, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 11, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 12, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 13, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 14, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 0, 747, 17, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 0, 662, 74, 0, 2).build(player));
		return this;
	}
	
	/**
	 * Sends the access masks for the fixed screen size
	 *
	 * @return A {@code NetworkTransmitter} {@code Object}
	 */
	public NetworkTransmitter sendFixedAMasks() {
		send(new AccessMaskBuilder(0, 99, 137, 58, 0, 2046).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 129, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 11, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 12, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 13, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 131, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 132, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 300, 190, 18, 0, 14).build(player));
		send(new AccessMaskBuilder(0, 11, 190, 15, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 130, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 133, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 27, 679, 0, 69, 0x457d8e).build(player));
		send(new AccessMaskBuilder(28, 55, 679, 0, 32, 0).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 134, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 135, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 30, 271, 8, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 136, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 99, 0, 0).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 130, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 100, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 101, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 600, 1109, 5, 0, 1024).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 102, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 200, 1110, 11, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 600, 1110, 16, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 600, 1110, 14, 0, 1024).build(player));
		send(new AccessMaskBuilder(0, 600, 1110, 5, 0, 1024).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 103, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 104, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 93, 590, 8, 0, 6).build(player));
		send(new AccessMaskBuilder(0, 11, 590, 13, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 105, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 2033, 187, 1, 0, 26).build(player));
		send(new AccessMaskBuilder(0, 11, 187, 9, 36, 6).build(player));
		send(new AccessMaskBuilder(12, 23, 187, 9, 0, 4).build(player));
		send(new AccessMaskBuilder(24, 24, 187, 9, 32, 0).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 106, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 29, 34, 9, 40, 30).build(player));
		send(new AccessMaskBuilder(0, 0, 747, 17, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 0, 662, 74, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 548, 129, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 11, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 12, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 13, 0, 2).build(player));
		send(new AccessMaskBuilder(-1, -1, 884, 14, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 0, 747, 17, 0, 2).build(player));
		send(new AccessMaskBuilder(0, 0, 662, 74, 0, 2).build(player));
		return this;
	}
	
	/**
	 * Sends an interface to the client
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public NetworkTransmitter sendInterface(int interfaceId) {
		switch (player.getNetworkSession().getViewComponents().getScreenSizeMode()) {
			case 0:
			case 1:
				send(new InterfaceDisplayBuilder(548, 18, interfaceId, false).build(player));
				return this;
			case 2:
			case 3:
				send(new InterfaceDisplayBuilder(746, 11 /* 9 */, interfaceId, false).build(player));
				return this;
		}
		return this;
	}
}
