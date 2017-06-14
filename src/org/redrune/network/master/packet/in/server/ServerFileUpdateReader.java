package org.redrune.network.master.packet.in.server;

import org.jboss.netty.channel.Channel;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ServerFileUpdateReader implements MasterPacketReader {
	
	@Override
	public int getId() {
		return CLIENT_FILE_UPDATE_PACKET;
	}
	
	@Override
	public void read(Channel channel, MasterPacket packet) {
		String username = packet.readString();
		String text = packet.readString();
		
		Misc.writeTextToFile(MasterConstants.SAVE_LOCATION + Misc.formatPlayerNameForProtocol(username) + ".json", text, false);
		System.out.println("Updated file " + username + ".json");
	}
}
