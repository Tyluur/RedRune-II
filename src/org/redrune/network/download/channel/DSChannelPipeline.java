package org.redrune.network.download.channel;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.redrune.network.NetworkConstants;
import org.redrune.network.NetworkSession;
import org.redrune.network.download.codec.VersionCheckDecoder;
import org.redrune.network.world.codec.io.RSPacketEncoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class DSChannelPipeline extends ChannelInitializer<SocketChannel> {
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		// localized pipeline
		final ChannelPipeline pipeline = channel.pipeline();
		
		// builds the pipeline
		pipeline.addLast("encoder", new RSPacketEncoder());
		pipeline.addLast("decoder", new VersionCheckDecoder());
		pipeline.addLast("handler", new DSChannelReader());
		pipeline.addLast("registrar", new DSChannelRegistrar());
		
		// sets the session
		channel.attr(NetworkConstants.SESSION_KEY).setIfAbsent(new NetworkSession(channel));
	}
}
