package org.redrune.network.master.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.redrune.network.master.codec.MasterPacketDecoder;
import org.redrune.network.master.codec.MasterPacketEncoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public class MasterServerPipeline implements ChannelPipelineFactory {
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = new DefaultChannelPipeline();
		pipeline.addLast("encoder", new MasterPacketEncoder());
		pipeline.addLast("decoder", new MasterPacketDecoder());
		pipeline.addLast("handler", new MasterServerHandler());
		return pipeline;
	}
	
}