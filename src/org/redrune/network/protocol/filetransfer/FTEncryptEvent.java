package org.redrune.network.protocol.filetransfer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * FTEncryptEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class FTEncryptEvent extends MessageToByteEncoder<ByteBuf> {

	private int encryptedKey;

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
		while (in.isReadable()) {
			out.writeByte(in.readUnsignedByte() ^ encryptedKey);
		}
	}

	public int getEncryptedKey() {
		return encryptedKey;
	}

	public void setEncryptedKey(int encryptedKey) {
		this.encryptedKey = encryptedKey;
	}

}
