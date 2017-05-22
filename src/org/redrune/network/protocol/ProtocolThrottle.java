package org.redrune.network.protocol;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Optional;

import org.redrune.network.ChannelListener;
import org.redrune.network.protocol.filetransfer.FTReadEvent;
import org.redrune.network.protocol.filetransfer.FTWriteEvent;
import org.redrune.network.protocol.handshake.HSReadEvent;
import org.redrune.network.protocol.handshake.HSWriteEvent;
import org.redrune.network.protocol.login.LoginReadEvent;
import org.redrune.network.protocol.login.LoginWriteEvent;
import org.redrune.network.session.impl.FileTransferSession;
import org.redrune.network.session.impl.HandshakeSession;
import org.redrune.network.session.impl.LoginSession;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * ProtocolThrottle.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class ProtocolThrottle extends ByteToMessageDecoder {

	/**
	 * Protocol
	 * @author Chryonic
	 * May 22, 2017 | RedRune
	 */
	public enum Protocol {
		REQUEST_HANDSHAKE, REQUEST_FILE_TRANSFER, REQUEST_LOGIN, REQUEST_GAME;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.isReadable()) {

			int opcode = in.readUnsignedByte();

			Protocol request = Optional.of(opcode == 15 ? Protocol.REQUEST_HANDSHAKE : Protocol.REQUEST_LOGIN).get();
			if (request == null) {
				return;
			}
			switch (request) {
			case REQUEST_HANDSHAKE:
				ctx.pipeline().addAfter("p.handler", "hs.write", new HSWriteEvent());
				ctx.pipeline().replace("p.handler", "hs.read", setSession(ctx.channel(), new HSReadEvent()));

				ctx.pipeline().addAfter("hs.read", "ft.write", new FTWriteEvent());
				ctx.pipeline().replace("hs.read", "ft.read", setSession(ctx.channel(), new FTReadEvent()));
				break;
			case REQUEST_LOGIN:

				ctx.channel().writeAndFlush(
						Unpooled.buffer(1).writeByte(ProtocolResponse.SUCCESSFUL_CONNECTION.getClientId()));
				ctx.pipeline().addAfter("p.handler", "login.write", new LoginWriteEvent());
				ctx.pipeline().replace("p.handler", "login.read", setSession(ctx.channel(), new LoginReadEvent()));

				break;
			default:
				break;
			}
		}
	}

	public static ByteToMessageDecoder setSession(Channel channel, ByteToMessageDecoder decoder) {
		ProtocolRequest connection = decoder.getClass().getAnnotation(ProtocolRequest.class);
		if (connection == null) {
			return null;
		}
		switch (connection.request()) {
		case REQUEST_HANDSHAKE:
			channel.attr(ChannelListener.CURRENT_SESSION).set(new HandshakeSession(channel));
			break;
		case REQUEST_FILE_TRANSFER:
			channel.attr(ChannelListener.CURRENT_SESSION).set(new FileTransferSession(channel));
			break;
		case REQUEST_LOGIN:
			channel.attr(ChannelListener.CURRENT_SESSION).set(new LoginSession(channel));
			break;
		case REQUEST_GAME:
			break;
		default:
			break;
		}
		return decoder;
	}

	@Retention(value = RetentionPolicy.RUNTIME)
	public abstract @interface ProtocolRequest {

		public abstract Protocol request();

	}

}
