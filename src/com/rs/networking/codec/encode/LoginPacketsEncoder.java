package com.rs.networking.codec.encode;

import com.rs.game.entity.actor.player.Player;
import com.rs.networking.Session;
import com.rs.networking.codec.Encoder;
import com.rs.networking.io.OutputStream;
import com.rs.utility.Misc;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

public final class LoginPacketsEncoder extends Encoder {

	public LoginPacketsEncoder(Session connection) {
		super(connection);
	}

	public final void sendStartUpPacket() {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(0);
		session.write(stream);
	}

	public final void sendClientPacket(int opcode) {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(opcode);
		ChannelFuture future = session.write(stream);
		if (future != null) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			session.getChannel().close();
		}
	}

	public final void sendLoginDetails(Player player) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(2);
		stream.writeByte(player.getDominantRight().getClientRight());
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(1);
		stream.writeByte(0);
		stream.writeShort(player.getIndex());
		stream.writeByte(1);
		stream.write24BitInteger(0);
		stream.writeByte(1);
		stream.writeString(player.getDisplayName());
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendLobbyDetails(Player player) {
		
		OutputStream responseBlock = new OutputStream();
		
		responseBlock.writePacketVarByte(2);
		responseBlock.writeByte(player.getDominantRight().getClientRight());// rights
		responseBlock.writeByte(0);// blackmarks
		responseBlock.writeByte(0);// muted? (bool)
		responseBlock.writeByte(0);// dunno (bool)
		responseBlock.writeByte(0);// dunno (bool)
		
		responseBlock.writeLong(0);// members subscription end
		responseBlock.writeByte(0);// 0x1 - if members, 0x2 - subscription
		responseBlock.writeInt(0);// recovery questions set date
		
		responseBlock.writeByte(2);
		responseBlock.writeInt(0);
		responseBlock.writeByte(0);
		responseBlock.writeInt(0);
		
		responseBlock.writeShort(2649);
		responseBlock.writeShort(0);
		// leave the cast because of json boxing.
		long lastLogin = System.currentTimeMillis(); /*((Number) player.getVariables().getAttribute(AttributeKey.LAST_LONGIN_STAMP, System.currentTimeMillis())).longValue();*/
		
		long now = System.currentTimeMillis();
		long jag = 1014753880308L;
		long since_jag = (now - jag) / 1000 / 60 / 60 / 24;
		long since_log = (now - lastLogin) / 1000 / 60 / 60 / 24;
		
		responseBlock.writeShort((int) (since_jag - since_log)); // last logged in date
		String lastIp = player.getLastIP();
		if (lastIp == null) {
			lastIp = Misc.getIpAddress(player.getSession().getChannel());
		}
		responseBlock.writeInt(Misc.IPAddressToNumber(lastIp)); // last login ip
		
		responseBlock.writeByte(3);
		responseBlock.writeShort(0);
		responseBlock.writeShort(0);
		responseBlock.writeByte(1);
		responseBlock.writeGJString(player.getUsername());
		responseBlock.writeByte((byte) 0);
		responseBlock.writeInt(1);
		responseBlock.writeByte(1);
		responseBlock.writeShort(1);
		responseBlock.writeGJString("127.0.0.1");
		responseBlock.endPacketVarByte();
		
		/*
		builder.writeByte(player.getDetails().getDominantRight().getClientRight());
		builder.writeByte(0); // blackmarks
		builder.writeByte(0); // muted? (bool)
		builder.writeByte(0); // dunno (bool)
		builder.writeByte(0);// dunno (bool)
		final long nowDate = new Date().getTime();
		
		builder.writeLong((nowDate - new Date(1970, 1, 1).getTime()));
		builder.writeByte5((6 * (60000)) + 20000);
		builder.writeByte(player.getDetails().isDonator() ? 0x2 : 0);
		builder.writeInt(0);// unused in client
		builder.writeByte(0); // unused in client (bool)
		builder.writeInt(0); // unused in client
		builder.writeShort(1); // recovery questions, 0 - not set,
		// otherwise goes bitencoded date.
		builder.writeShort(player.getManager().getWebManager().getUnreadMessages()); // unread messages
		
		// leave the cast because of json boxing.
		long lastLogin = ((Number) player.getVariables().getAttribute(AttributeKey.LAST_LONGIN_STAMP, System.currentTimeMillis())).longValue();
		
		long now = System.currentTimeMillis();
		long jag = 1014753880308L;
		long since_jag = (now - jag) / 1000 / 60 / 60 / 24;
		long since_log = (now - lastLogin) / 1000 / 60 / 60 / 24;
		
		builder.writeShort((int) (since_jag - since_log)); // last logged in date
		String lastIp = player.getDetails().getLastIp();
		if (lastIp == null) {
			lastIp = Misc.getIpAddress(player.getSession().getChannel());
		}
		builder.writeInt(Misc.IPAddressToNumber(lastIp)); // last login ip
		builder.writeByte(player.getManager().getWebManager().isEmailVerified() ? 3 : 0);
		// 0 - no email
		// 1 - pending parental confirmation
		// 2 - pending confirmation.
		// 3 - registered.
		// 4 - no longer registered.
		builder.writeShort(0); // credit card expiration time
		builder.writeShort(0); // credit card loyality expiration time
		builder.writeByte(0); // unused in client
		builder.writeGJString2(player.getDetails().getUsername());
		builder.writeByte(0); // unused in client
		builder.writeInt(player.getDetails().getUsername().startsWith("#") ? 0 : 1);
		builder.writeByte(1); // dunno (bool)
		builder.writeShort(GameFlags.worldId); // current world id
		builder.writeGJString2("127.0.0.1");
		 */
		
		player.getSession().write(responseBlock);
	}

}
