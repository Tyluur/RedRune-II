package org.redrune.network.rs666.codec.login;

import java.util.Arrays;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.redrune.anetworking.NetworkConstants;
import org.redrune.anetworking.rs666.NetworkSession;
import org.redrune.anetworking.rs666.codec.RS2GameDecoder;
import org.redrune.anetworking.rs666.packet.structure.out.LobbyResponseBuilder;
import org.redrune.anetworking.rs666.packet.structure.out.LoginResponseCodeBuilder;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.backend.ReturnCode;
import org.redrune.utility.io.BufferUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class RS2LoginDecoder extends ReplayingDecoder<LoginState> {
	
	private NetworkSession session;
	
	public RS2LoginDecoder() {
		checkpoint(LoginState.PRE_STAGE);
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, LoginState state) throws Exception {
		try {
			if (state == LoginState.LOBBY_FINALIZATION || state == LoginState.LOGIN_FINALIZATION) {
				session = new NetworkSession(channel);
				session.getChannel().getPipeline().getContext("handler").setAttachment(session);
			}
			switch (state) {
				case PRE_STAGE:
					if (BufferUtils.readableBytes(buffer) < 3) {
						channel.close();
						return session;
					} else {
						int loginType = buffer.readByte();
						int loginPacketSize = buffer.readShort();
						if (loginPacketSize != BufferUtils.readableBytes(buffer)) {
							System.out.println("Stopped!");
							channel.close();
							return session;
						}
						int clientVersion = buffer.readInt();
						if (clientVersion != NetworkConstants.REVISION) {
							channel.close();
							return session;
						}
						if (loginType == 16 || loginType == 18) {
							checkpoint(LoginState.LOGIN_FINALIZATION);
						} else if (loginType == 19) {
							checkpoint(LoginState.LOBBY_FINALIZATION);
						} else {
							channel.close();
							return session;
						}
					}
					break;
				case LOBBY_FINALIZATION:
					if (buffer.readable()) {
						short rsaLength = buffer.readShort();
						if (rsaLength < 0 || BufferUtils.readableBytes(buffer) < rsaLength) {
							channel.close();
							return session;
						}
						byte[] rsaBuffer = new byte[rsaLength];
						for (int i = 0; i < rsaLength; i++) {
							rsaBuffer[i] = buffer.readByte();
						}
						ChannelBuffer rsaBlock = ChannelBuffers.wrappedBuffer(rsaBuffer);
						byte rsaMagic = rsaBlock.readByte();
						if (rsaMagic != 10) {
							session.write(new LoginResponseCodeBuilder(ReturnCode.BAD_SESSION_ID).build(null));
							channel.close();
							return session;
						}
						int[] isaac = new int[4];
						for (int i = 0; i < 4; i++) {
							isaac[i] = rsaBlock.readInt();
						}
						long vHash = rsaBlock.readLong();
						if (vHash != 0L) {
							session.write(new LoginResponseCodeBuilder(ReturnCode.BAD_SESSION_ID).build(null));
							channel.close();
							return session;
						}
						String password = BufferUtils.readRS2String(rsaBlock);
						long[] serverSeeds = new long[2];
						for (int i = 0; i < 2; i++) {
							serverSeeds[i] = rsaBlock.readLong();
						}
						
						// after rsaBlock there goes xteaBlock , which needs to be decrypted.
						int xteaBlockLength = BufferUtils.readableBytes(buffer);
						byte[] xteaBuffer = new byte[xteaBlockLength];
						for (int i = 0; i < xteaBlockLength; i++) {
							xteaBuffer[i] = buffer.readByte();
						}
						ChannelBuffer xteaPacket = ChannelBuffers.wrappedBuffer(BufferUtils.decrypt(isaac, xteaBuffer, 0, xteaBlockLength));
						
						String username = BufferUtils.readRS2String(xteaPacket);
						
						byte gameID = xteaPacket.readByte(); // game ID (0 for runescape)
						byte langID = xteaPacket.readByte(); // language id.
						
						byte[] userId = new byte[24];
						for (int i = 0; i < 24; i++) // that's the content of random.dat , which is generated depending on user's hardware and software.
						{
							userId[i] = xteaPacket.readByte();
						}
						
						String settings = BufferUtils.readRS2String(xteaPacket);// settings are only used when sending login block
						
						int affliateID = xteaPacket.readInt(); // this is used when showing adverts.
						
						int[] cacheCRCS = new int[36];
						for (int i = 0; i < 36; i++) {
							cacheCRCS[i] = xteaPacket.readInt();
						}
						// finished decoding
						
						session.setInLobby(true);
						session.write(new LoginResponseCodeBuilder(ReturnCode.SUCCESSFUL).build(null));
						
						Player player = new Player(username, password, session);
						player.registerTransients();
						session.write(new LobbyResponseBuilder().build(player));
						
						System.out.println(settings + ", " + Arrays.toString(userId));
						
						ctx.getPipeline().replace("decoder", "decoder", new RS2GameDecoder(session));
						return session;
					/*
						
						int rsaHeader = buffer.readByte();
						if (rsaHeader != 10) {
							channel.close();
							return session;
						}
						int[] keys = new int[4];
						for (int i = 0; i < keys.length; i++) {
							keys[i] = buffer.readInt();
						}
						buffer.readLong();
						String password = BufferUtils.readRS2String(buffer);
						buffer.readLong(); // client key
						buffer.readLong(); // other client key
						byte[] block = new byte[BufferUtils.readableBytes(buffer)];
						buffer.readBytes(block);
						ChannelBuffer decryptedPayload = ChannelBuffers.wrappedBuffer(BufferUtils.decrypt(keys, block, 0, block.length));
						String username = BufferUtils.readRS2String(decryptedPayload).toLowerCase();
						decryptedPayload.readByte(); // screen settings?
						decryptedPayload.readByte();
						for (int i = 0; i < 24; i++) {
							decryptedPayload.readByte();
						}
						BufferUtils.readRS2String(decryptedPayload); // settings
						decryptedPayload.readInt();
						for (int i = 0; i < 34; i++) {
							decryptedPayload.readInt();
						}
						
						// finished decoding
						
						session.setInLobby(true);
						session.write(new LoginResponseCodeBuilder(ReturnCode.SUCCESSFUL).build(null));
						
						Player player = new Player(username, password, session);
						player.registerTransients();
						session.write(new LobbyResponseBuilder().build(player));
						
						ctx.getPipeline().replace("decoder", "decoder", new RS2GameDecoder(session));*/
					}
					return session;
				case LOGIN_FINALIZATION:
					if (buffer.readable()) {
						buffer.readByte();
						int rsaHeader = buffer.readByte();
						if (rsaHeader != 10) {
							channel.close();
							System.err.println("SEVERE! Invalid RSA header.");
							return session;
						}
						int[] keys = new int[4];
						for (int i = 0; i < keys.length; i++) {
							keys[i] = buffer.readInt();
						}
						buffer.readLong();
						String password = BufferUtils.readRS2String(buffer);
						buffer.readLong(); // client key
						buffer.readLong(); // other client key
						byte[] block = new byte[BufferUtils.readableBytes(buffer)];
						buffer.readBytes(block);
						ChannelBuffer decryptedPayload = ChannelBuffers.wrappedBuffer(BufferUtils.decrypt(keys, block, 0, block.length));
						String username = BufferUtils.readRS2String(decryptedPayload).toLowerCase();
						decryptedPayload.readByte();
						int mode = decryptedPayload.readByte();
						int width = decryptedPayload.readShort();
						int height = decryptedPayload.readShort();
						int displayMode = decryptedPayload.readByte();
						for (int i = 0; i < 24; i++) {
							decryptedPayload.readByte();
						}
						BufferUtils.readRS2String(decryptedPayload);
						decryptedPayload.readInt();
						decryptedPayload.skipBytes(decryptedPayload.readByte() & 0xff);
						
						// finished decoding
						
						session.setInLobby(false);
						session.getViewComponents().setScreenSizeMode(mode);
						session.getViewComponents().setScreenSizeX(width);
						session.getViewComponents().setScreenSizeY(height);
						session.getViewComponents().setDisplayMode(displayMode);
						session.write(new LoginResponseCodeBuilder(ReturnCode.SUCCESSFUL).build(null));
						
						Player player = new Player(username, password, session);
						
						player.register();
						
						ctx.getPipeline().replace("decoder", "decoder", new RS2GameDecoder(session));
						return null;
					}
					return session;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return session;
	}
	
}
