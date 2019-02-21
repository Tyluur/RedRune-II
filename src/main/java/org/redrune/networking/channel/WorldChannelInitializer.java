package org.redrune.networking.channel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.redrune.networking.NetworkSession;
import org.redrune.networking.codec.RS2PacketEncoder;
import org.redrune.networking.codec.handshake.HandshakeDecoder;
import org.redrune.utility.constants.NetworkConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
@Sharable
public class WorldChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private static final WorldChannelReader CHANNEL_READER = new WorldChannelReader();
	
	private static final WorldChannelRegistrar REGISTRAR = new WorldChannelRegistrar();
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		final ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast("encoder", new RS2PacketEncoder());
		pipeline.addLast("decoder", new HandshakeDecoder());
		pipeline.addLast("handler", CHANNEL_READER);
		pipeline.addLast("registrar", REGISTRAR);
		// sets the session
		pipeline.channel().attr(NetworkConstants.SESSION_KEY).set(new NetworkSession(channel));
	}
}
