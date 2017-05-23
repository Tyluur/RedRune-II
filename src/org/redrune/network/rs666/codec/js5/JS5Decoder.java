package org.redrune.network.rs666.codec.js5;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.redrune.cache.CacheManager;
import org.redrune.engine.EngineWorkingSet;
import org.redrune.network.rs666.packet.Packet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class JS5Decoder extends FrameDecoder {
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, final Channel channel, ChannelBuffer buffer) throws Exception {
		while (true) {
			if (!(buffer.readableBytes() >= 4)) {
				break;
			}
			final int priority = buffer.readByte() & 0xFF;
			final int container = buffer.readByte() & 0xFF;
			final int file = buffer.readShort() & 0xFFFF;
			if (priority == 1 || priority == 0) {
				EngineWorkingSet.submitJs5Work(() -> {
					if (channel.isConnected()) {
						Packet response = CacheManager.generateFile(container, file, priority);
						if (response != null) {
							channel.write(response);
						}
					}
				});
			}
		}
		return null;
	}
}
