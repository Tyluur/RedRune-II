package org.redrune.network.rs666;

import org.redrune.game.GameConstants;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.input.InputResponse;
import org.redrune.network.rs666.packet.input.InputType;
import org.redrune.network.rs666.packet.structure.out.*;
import org.redrune.utility.rs.constant.InterfaceConstants;

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
		
		player.loadMapRegions();
		player.getManager().getInterfaces().sendLogin();
		player.sendSettings();
		sendDefaultConfigs();
		sendMessage("Welcome to " + GameConstants.SERVER_NAME + ".", false);
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
	 * Sends the default game configs
	 */
	public NetworkTransmitter sendDefaultConfigs() {
		send(new InterfaceChangeBuilder(34, 13, false).build(player));
		send(new InterfaceChangeBuilder(34, 3, false).build(player));
		send(new VarpPacketBuilder(281, 1000).build(player));// Tutorial-completed-config
		send(new CS2ConfigBuilder(168, 4).build(player));
		send(new CS2ConfigBuilder(1273, 1).build(player));
		send(new CS2ConfigBuilder(1000, 1).build(player));
		send(new CS2ConfigBuilder(232, 0).build(player));
		send(new CS2ConfigBuilder(233, 0).build(player));
		send(new CS2ConfigBuilder(234, 0).build(player));
		send(new CS2ConfigBuilder(1423, 44).build(player));// tasklist-total
		send(new CS2ConfigBuilder(1424, 8).build(player));// tasklist-
		send(new CS2ConfigBuilder(822, 0).build(player));
		send(new CS2ConfigBuilder(181, 0).build(player));
		send(new CS2ConfigBuilder(823, 0).build(player));
		send(new CS2ConfigBuilder(1027, 1).build(player));
		send(new CS2ConfigBuilder(1034, 2).build(player));
		send(new CS2ConfigBuilder(245, 0).build(player));
		send(new CS2ConfigBuilder(1000, 66).build(player));
		send(new CS2ConfigBuilder(1428, 0).build(player));
		send(new CS2ConfigBuilder(629, -1).build(player));
		send(new CS2ConfigBuilder(630, -1).build(player));
		send(new CS2ConfigBuilder(627, -1).build(player));
		send(new CS2ConfigBuilder(628, -1).build(player));
		send(new CS2ConfigBuilder(1416, 0).build(player));
		send(new CS2ConfigBuilder(1469, 51).build(player));
		send(new CS2ConfigBuilder(1470, 50).build(player));
		send(new CS2ConfigBuilder(1471, 50).build(player));
		send(new CS2ConfigBuilder(1472, 20).build(player));
		send(new CS2ConfigBuilder(1473, 56).build(player));
		send(new CS2ConfigBuilder(1474, 60).build(player));
		send(new CS2ConfigBuilder(1475, 49).build(player));
		send(new CS2ConfigBuilder(1476, 24).build(player));
		send(new CS2ConfigBuilder(1477, 22).build(player));
		send(new CS2ConfigBuilder(1478, 32).build(player));
		send(new CS2ConfigBuilder(1479, 50).build(player));
		send(new CS2ConfigBuilder(1480, 14).build(player));
		send(new CS2ConfigBuilder(1481, 37).build(player));
		send(new CS2ConfigBuilder(1482, 30).build(player));
		send(new CS2ConfigBuilder(1483, 44).build(player));
		send(new CS2ConfigBuilder(1484, 31).build(player));
		send(new CS2ConfigBuilder(1485, 4).build(player));
		send(new CS2ConfigBuilder(1486, 64).build(player));
		send(new CS2ConfigBuilder(1487, 18).build(player));
		send(new CS2ConfigBuilder(1488, 8).build(player));
		send(new CS2ConfigBuilder(1489, 10).build(player));
		send(new CS2ConfigBuilder(1490, 3).build(player));
		send(new CS2ConfigBuilder(1491, 15).build(player));
		send(new CS2ConfigBuilder(1492, 18).build(player));
		send(new CS2ConfigBuilder(1493, 6).build(player));
		send(new CS2ConfigBuilder(1000, 66).build(player));
		send(new CS2ConfigBuilder(1413, 1).build(player));
		send(new CS2ConfigBuilder(1416, 0).build(player));
		send(new CS2ConfigBuilder(695, 0).build(player));
		send(new CS2ConfigBuilder(695, 0).build(player));
		return this;
	}
	
	/**
	 * Sends a message
	 *
	 * @param text
	 * 		The text of the message
	 * @param filterable
	 * 		If the message should be filterable.
	 */
	public NetworkTransmitter sendMessage(String text, boolean... filterable) {
		// messages should only be filtered if this is sent as NetworkTransmitter#sendMessage("hi", true);
		// otherwise the parameter is unneeded...
		
		boolean shouldFilter = filterable.length != 0 && filterable[0];
		send(new MessageBuilder(shouldFilter ? 109 : 0, text).build(player));
		return this;
	}
	
	/**
	 * Sends the full screen access masks
	 *
	 * @return A {@code NetworkTransmitter} {@code Object}
	 */
	public NetworkTransmitter sendFullScreenAMasks() {
		send(new AccessMaskBuilder(137, 58, 0, 2046, 0, 99).build(player));
		send(new AccessMaskBuilder(746, 39, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 11, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 12, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 13, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(746, 41, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(746, 42, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(190, 18, 0, 14, 0, 300).build(player));
		send(new AccessMaskBuilder(190, 15, 0, 2, 0, 11).build(player));
		send(new AccessMaskBuilder(746, 40, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(746, 43, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(InterfaceConstants.INVENTORY_INTERFACE_ID, 0, 69, 0x457d8e, 0, 27).build(player));
		send(new AccessMaskBuilder(InterfaceConstants.INVENTORY_INTERFACE_ID, 0, 32, 0, 28, 55).build(player));
		send(new AccessMaskBuilder(746, 44, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(746, 45, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(271, 8, 0, 2, 0, 30).build(player));
		send(new AccessMaskBuilder(746, 46, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(746, 47, 0, 0, -1, -1).build(player));
		send(new AccessMaskBuilder(746, 40, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(746, 48, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(746, 49, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(1109, 5, 0, 1024, 0, 600).build(player));
		send(new AccessMaskBuilder(746, 50, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(1110, 11, 0, 2, 0, 200).build(player));
		send(new AccessMaskBuilder(1110, 16, 0, 2, 0, 600).build(player));
		send(new AccessMaskBuilder(1110, 14, 0, 1024, 0, 600).build(player));
		send(new AccessMaskBuilder(1110, 5, 0, 1024, 0, 600).build(player));
		send(new AccessMaskBuilder(746, 51, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(746, 52, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(590, 8, 0, 6, 0, 93).build(player));
		send(new AccessMaskBuilder(590, 13, 0, 2, 0, 11).build(player));
		send(new AccessMaskBuilder(746, 53, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(187, 1, 0, 26, 0, 2033).build(player));
		send(new AccessMaskBuilder(187, 9, 36, 6, 0, 11).build(player));
		send(new AccessMaskBuilder(187, 9, 0, 4, 12, 23).build(player));
		send(new AccessMaskBuilder(187, 9, 32, 0, 24, 24).build(player));
		send(new AccessMaskBuilder(746, 54, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(34, 9, 40, 30, 0, 29).build(player));
		send(new AccessMaskBuilder(747, 17, 0, 2, 0, 0).build(player));
		send(new AccessMaskBuilder(662, 74, 0, 2, 0, 0).build(player));
		send(new AccessMaskBuilder(746, 39, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 11, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 12, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 13, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 14, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(747, 17, 0, 2, 0, 0).build(player));
		send(new AccessMaskBuilder(662, 74, 0, 2, 0, 0).build(player));
		return this;
	}
	
	/**
	 * Sends the access masks for the fixed screen size
	 *
	 * @return A {@code NetworkTransmitter} {@code Object}
	 */
	public NetworkTransmitter sendFixedAMasks() {
		send(new AccessMaskBuilder(137, 58, 0, 2046, 0, 99).build(player));
		send(new AccessMaskBuilder(548, 129, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 11, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 12, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 13, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(548, 131, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(548, 132, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(190, 18, 0, 14, 0, 300).build(player));
		send(new AccessMaskBuilder(190, 15, 0, 2, 0, 11).build(player));
		send(new AccessMaskBuilder(548, 130, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(548, 133, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(InterfaceConstants.INVENTORY_INTERFACE_ID, 0, 69, 0x457d8e, 0, 27).build(player));
		send(new AccessMaskBuilder(InterfaceConstants.INVENTORY_INTERFACE_ID, 0, 32, 0, 28, 55).build(player));
		send(new AccessMaskBuilder(548, 134, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(548, 135, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(271, 8, 0, 2, 0, 30).build(player));
		send(new AccessMaskBuilder(548, 136, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(548, 99, 0, 0, -1, -1).build(player));
		send(new AccessMaskBuilder(548, 130, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(548, 100, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(548, 101, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(1109, 5, 0, 1024, 0, 600).build(player));
		send(new AccessMaskBuilder(548, 102, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(1110, 11, 0, 2, 0, 200).build(player));
		send(new AccessMaskBuilder(1110, 16, 0, 2, 0, 600).build(player));
		send(new AccessMaskBuilder(1110, 14, 0, 1024, 0, 600).build(player));
		send(new AccessMaskBuilder(1110, 5, 0, 1024, 0, 600).build(player));
		send(new AccessMaskBuilder(548, 103, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(548, 104, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(590, 8, 0, 6, 0, 93).build(player));
		send(new AccessMaskBuilder(590, 13, 0, 2, 0, 11).build(player));
		send(new AccessMaskBuilder(548, 105, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(187, 1, 0, 26, 0, 2033).build(player));
		send(new AccessMaskBuilder(187, 9, 36, 6, 0, 11).build(player));
		send(new AccessMaskBuilder(187, 9, 0, 4, 12, 23).build(player));
		send(new AccessMaskBuilder(187, 9, 32, 0, 24, 24).build(player));
		send(new AccessMaskBuilder(548, 106, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(34, 9, 40, 30, 0, 29).build(player));
		send(new AccessMaskBuilder(747, 17, 0, 2, 0, 0).build(player));
		send(new AccessMaskBuilder(662, 74, 0, 2, 0, 0).build(player));
		send(new AccessMaskBuilder(548, 129, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 11, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 12, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 13, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(884, 14, 0, 2, -1, -1).build(player));
		send(new AccessMaskBuilder(747, 17, 0, 2, 0, 0).build(player));
		send(new AccessMaskBuilder(662, 74, 0, 2, 0, 0).build(player));
		return this;
	}
	
	/**
	 * Sends the packet to reset the minimap flag location
	 */
	public NetworkTransmitter sendMinimapFlagReset() {
		send(new MinimapFlagBuilder(255, 255).build(player));
		return this;
	}
	
	/**
	 * Closes the input box that is opened from {@link #requestInput(InputResponse, InputType, String)}
	 */
	public NetworkTransmitter closeInputBox() {
		send(new CS2ScriptBuilder(1548, 0).build(player));
		return this;
	}
	
	/**
	 * This requests input from the client
	 *
	 * @param response
	 * 		The response
	 * @param type
	 * 		The type of input to expect
	 * @param title
	 * 		The title to send on the input text
	 */
	public NetworkTransmitter requestInput(InputResponse response, InputType type, String title) {
		player.putAttribute(type.getName(), response);
		send(new CS2ScriptBuilder(type.getScriptId(), "s", title).build(player));
		return this;
	}
}