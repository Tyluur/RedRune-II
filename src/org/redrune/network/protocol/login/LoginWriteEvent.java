package org.redrune.network.protocol.login;

import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.redrune.network.protocol.ProtocolResponse;
import org.redrune.network.protocol.login.msg.LoginResponseEvent;
import org.redrune.utility.backend.BufferUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * LoginWriteEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class LoginWriteEvent extends MessageToByteEncoder<LoginResponseEvent> {

	public LoginWriteEvent() {
		super(LoginResponseEvent.class);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, LoginResponseEvent event, ByteBuf out) throws Exception {
		ByteBuf stream = Unpooled.buffer(1000);
		out.writeByte(event.getResponse().getClientId());
		if (event.getResponse().equals(ProtocolResponse.SUCCESSFUL_LOGIN)) {
			switch (event.getLoginRequest()) {
			case REQUEST_LOBBY:
				stream.writeByte(event.getPlayer().getDetails().getDominantRight().getClientRight());
				stream.writeByte(0);
				stream.writeByte(0);
				stream.writeMedium(8388608);
				stream.writeByte(0);
				stream.writeByte(0);
				stream.writeByte(0);
				stream.writeLong(1386299915556L);
				stream.writeByte(29115538);
				stream.writeInt(1);
				stream.writeShort(getRecoveryQuestionsSetDays());
				stream.writeShort(1);// 0
				stream.writeShort(getLastLoggedInDays());
				stream.writeInt(0);
				stream.writeShort(1);
				stream.writeShort(0);
				stream.writeShort(4565);
				stream.writeInt(
						refreshIPAddress(ctx.channel().remoteAddress().toString().split(":")[0].replace("/", "")));
				stream.writeByte(3);
				stream.writeShort(0);
				stream.writeShort(0);
				stream.writeByte(0);
				BufferUtils.writeGJString(event.getPlayer().getDetails().getUsername(), stream);
				stream.writeByte(0);
				stream.writeInt(1);
				stream.writeByte(1);
				stream.writeShort(1);
				BufferUtils.writeGJString("127.0.0.1", stream);
				out.writeByte(stream.writerIndex());
				out.writeBytes(stream);
				break;
			case REQUEST_WORLD:
				stream.writeByte(event.getPlayer().getDetails().getDominantRight().getClientRight());
				stream.writeByte(0);
				stream.writeByte(0);
				stream.writeByte(0);
				stream.writeByte(1);
				stream.writeByte(0);
				stream.writeShort(event.getPlayer().getIndex());
				stream.writeByte(1);
				stream.writeMedium(0);
				stream.writeByte(1);
				BufferUtils.writeGJString(event.getPlayer().getDetails().getUsername(), stream);
				out.writeByte(stream.writerIndex());
				out.writeBytes(stream);
				break;
			default:
				break;
			}
		}
	}

	private int getLastLoggedInDays() {
		DateTime from = new DateTime(2002, 2, 26, 23, 0);
		DateTime to = new DateTime();
		return Days.daysBetween(from, to).getDays();
	}

	private int getRecoveryQuestionsSetDays() {
		DateTime from = new DateTime(2002, 2, 26, 23, 0);
		DateTime to = new DateTime();
		return Days.daysBetween(from, to).getDays();
	}

	private int refreshIPAddress(String address) {
		StringTokenizer string = new StringTokenizer(address, ".");
		int[] ip = new int[4];
		int index = 0;
		while (string.hasMoreTokens()) {
			ip[index++] = Integer.parseInt(string.nextToken());
		}
		return ((ip[0] << 24) | (ip[1] << 16) | (ip[2] << 8) | (ip[3]));
	}

}
