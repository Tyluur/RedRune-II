package org.redrune.network.packet.read.impl;

import org.redrune.network.packet.read.PacketReadEvent;
import org.redrune.network.stream.IoReadEvent;
import org.redrune.rs2.node.entity.player.Player;

public class OpenWebsiteReadEvent implements PacketReadEvent {

	@Override
	public void decodePacket(Player player, IoReadEvent packet) {
		packet.readShort();
		packet.readRS2String();
		String address = packet.readRS2String();
		System.out.println(address);
		switch (address) {
		case "account_settings.ws?mod=email":
			player.getPacketSender().sendWebsite("http://www.google.com/");
			break;
		case "account_settings.ws?mod=recoveries":
			player.getPacketSender().sendWebsite("http://www.google.com/");
			break;
		case "account_settings.ws?mod=messages":
			player.getPacketSender().sendWebsite("http://www.google.com/");
			break;
		case "userdetails.ws":
			player.getPacketSender().sendWebsite("http://www.google.com/");
			break;
		case "account_settings.ws?mod=uidPassport":
			player.getPacketSender().sendWebsite("http://www.google.com/");
			break;
		case "index.ws":
			player.getPacketSender().sendWebsite("http://www.google.com/");
			break;
		case "en/Customer_Support":
			player.getPacketSender().sendWebsite("http://www.google.com/");
			break;
		case "forums.ws":
			player.getPacketSender().sendWebsite("http://www.google.com/");
			break;
		case "purchasepopup.ws?externalName=rs":
			player.getPacketSender().sendWebsite("http://www.google.com/");
			break;
		}
	}

}
