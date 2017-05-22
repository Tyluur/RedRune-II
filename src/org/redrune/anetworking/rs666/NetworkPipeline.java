package org.redrune.network.rs666;

import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.redrune.anetworking.rs666.codec.RS2GameEncoder;
import org.redrune.anetworking.rs666.codec.handshake.HandshakeDecoder;

import io.netty.channel.ChannelPipeline;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class NetworkPipeline implements ChannelPipelineFactory {
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = new DefaultChannelPipeline();
		pipeline.addLast("encoder", new RS2GameEncoder());
		pipeline.addLast("decoder", new HandshakeDecoder());
		pipeline.addLast("handler", new NetworkHandler());
		return pipeline;
	}
	
}
