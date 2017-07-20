package org.redrune.network.lobby.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public interface LoginDecoder {
	
	/**
	 * Decodes the login block
	 *
	 * @param ctx
	 * 		The channel context
	 * @param in
	 * 		The buffer
	 * @param out
	 * 		The outgoing response
	 * @return The next state
	 */
	LoginRequest decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out);
}
