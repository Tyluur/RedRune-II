package org.redrune.network.rs666.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.PacketBuilder;
import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.Packet.PacketType;
import org.redrune.network.rs666.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class FriendsListBuilder implements OutgoingPacketBuilder {
	
	private final String name;
	
	private final String previousName;
	
	private final int worldId;
	
	private final int clanRank;
	
	private final boolean warn;
	
	private final boolean lobby;
	
	private final boolean online;
	
	/**
	 * Constructs an empty friends list
	 */
	public FriendsListBuilder() {
		this.name = null;
		this.previousName = null;
		this.worldId = -1;
		this.clanRank = 0;
		this.warn = false;
		this.lobby = false;
		this.online = false;
	}
	
	/**
	 * Constructs a friends list update for a friend that loggged out
	 *
	 * @param name
	 * 		The name of the friend
	 */
	public FriendsListBuilder(String name) {
		this.name = name;
		this.previousName = "";
		this.worldId = -1;
		this.clanRank = 0;
		this.warn = true;
		this.lobby = false;
		this.online = false;
	}
	
	public FriendsListBuilder(String name, int worldId) {
		this.name = name;
		this.previousName = "";
		this.worldId = worldId;
		this.clanRank = 0;
		this.warn = true;
		this.lobby = worldId == 0;
		this.online = true;
	}
	
	public FriendsListBuilder(String name, String previousName, int worldId, int clanRank, boolean warn, boolean lobby, boolean online) {
		this.name = name;
		this.previousName = previousName;
		this.worldId = worldId;
		this.clanRank = clanRank;
		this.warn = warn;
		this.lobby = lobby;
		this.online = online;
	}
	
	@Override
	public Packet build(Player player) {
		if (name == null) {
			return new PacketBuilder(5, PacketType.VAR_SHORT).toPacket();
		} else {
			PacketBuilder bldr = new PacketBuilder(5, PacketType.VAR_SHORT);
			
			bldr.writeByte(warn ? 0 : 1);
			bldr.writeRS2String(name);
			bldr.writeRS2String(previousName);
			// random world id for the lobby.
			bldr.writeShort(online ? (lobby ? 1337 : worldId) : 0);
			bldr.writeByte(clanRank);
			bldr.writeByte(0);
			if (worldId >= 0) {
				bldr.writeRS2String(lobby ? "Lobby" : "World " + worldId);
				bldr.writeByte(0);
			}
			return bldr.toPacket();
		}
	}
	
}
