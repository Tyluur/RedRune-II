package org.redrune.networking.channel;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.NetworkSession;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.incoming.IncomingPacketRepository;
import org.redrune.utility.constants.NetworkConstants;
import org.redrune.utility.functions.Misc;

import static org.redrune.utility.constants.NetworkConstants.IGNORED_EXCEPTIONS;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
@Sharable
public class WorldChannelReader extends SimpleChannelInboundHandler<Packet> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
		try {
			NetworkSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
			// makes sure we have a session
			Preconditions.checkArgument(session != null, "No session set for channel.");
			// the player of the session
			final Player player = session.getPlayer();
			// make sure we have a player
			if (player == null) {
				return;
			}
			player.getAttributes().setPacketsDecoderPing(Misc.currentTimeMillis());
			IncomingPacketRepository.handlePacket(player, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		System.out.println("ctx = [" + ctx + "], e = [" + e + "]");
		if (IGNORED_EXCEPTIONS.stream().noneMatch($it -> Objects.equal($it, e.getMessage()))) {
			e.printStackTrace();
		}
		ctx.channel().close();
	}
}
