package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.list.WorldList;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class WorldListBuilder implements OutgoingPacketBuilder {
	
	private final boolean worldConfiguration;
	
	private final boolean worldStatus;
	
	public WorldListBuilder(boolean worldConfiguration, boolean worldStatus) {
		this.worldConfiguration = worldConfiguration;
		this.worldStatus = worldStatus;
	}
	
	@Override
	public Packet build(Player player) {
		return WorldList.getData(worldConfiguration, worldStatus).toPacket();
	}
}
