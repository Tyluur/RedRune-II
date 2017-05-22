package org.redrune.network.rs666.packet.structure.in;

import org.redrune.anetworking.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class ClientDisplayPacketStructure implements IncomingPacketStructure {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(34);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int screenSizeMode = packet.readByte();
		int screenSizeX = packet.readShort();
		int screenSizeY = packet.readShort();
		int displayMode = packet.readByte();
		if (screenSizeMode < 0 || screenSizeMode > 3) {
			return;
		}
		boolean send = false;
		if (screenSizeMode != player.getNetworkSession().getViewComponents().getScreenSizeMode()) {
			send = true;
		}
		player.getNetworkSession().getViewComponents().setScreenSizeMode(screenSizeMode);
		player.getNetworkSession().getViewComponents().setScreenSizeX(screenSizeX);
		player.getNetworkSession().getViewComponents().setScreenSizeY(screenSizeY);
		player.getNetworkSession().getViewComponents().setDisplayMode(displayMode);
		if (send) {
			player.getTransmitter().sendLoginComponents();
			player.getTransmitter().sendInterface(742);
			if (screenSizeMode < 2) {
				player.getTransmitter().sendFixedAMasks();
			} else {
				player.getTransmitter().sendFullScreenAMasks();
			}
		}
	}
}
