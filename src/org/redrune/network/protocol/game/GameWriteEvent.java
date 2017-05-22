package org.redrune.network.protocol.game;

import org.redrune.network.protocol.game.msg.GameResponseEvent;
import org.redrune.network.stream.IoWriteEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * GameWriteEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class GameWriteEvent extends MessageToByteEncoder<GameResponseEvent> {

	public GameWriteEvent() {
		super(GameResponseEvent.class);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, GameResponseEvent msg, ByteBuf out) throws Exception {
		out.writeBytes(IoWriteEvent.serialize(msg.getIsaacPair(), msg.getClazz(), msg.getIOWriteEvent()));
	}

}
