package org.redrune.networking.codec.decode;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.Session;
import org.redrune.networking.codec.Decoder;
import org.redrune.networking.stream.InputStream;

import static org.redrune.utility.constants.PacketConstants.loadPacketSizes;

public final class WorldPacketsDecoder extends Decoder {
	
	static {
		loadPacketSizes();
	}
	
	private Player player;
	
	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}
	
	@Override
	public void decode(InputStream stream) {
		player.getSession().addStreamToIncomingQueue(stream);
	}
	
}
