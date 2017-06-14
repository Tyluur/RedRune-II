package org.redrune.network.rs666.codec.login;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.redrune.game.GameFlags;
import org.redrune.network.NetworkConstants;
import org.redrune.network.RS2MasterCommunication;
import org.redrune.network.master.packet.out.client.build.ClientLoginResponseBuilder;
import org.redrune.network.master.packet.out.client.context.ClientLoginResponseContext;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.network.rs666.codec.RS2GameDecoder;
import org.redrune.network.rs666.packet.outgoing.impl.LoginResponseCodeBuilder;
import org.redrune.utility.BufferUtils;
import org.redrune.utility.backend.ReturnCode;

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
			if (state == LoginState.LOBBY_ENTRANCE || state == LoginState.GAME_ENTRANCE) {
				session = new NetworkSession(channel);
				session.getChannel().getPipeline().getContext("handler").setAttachment(session);
				RS2MasterCommunication.getNetworkSessions().add(session);
			}
			if (state == LoginState.PRE_STAGE) {
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
						checkpoint(LoginState.GAME_ENTRANCE);
					} else if (loginType == 19) {
						checkpoint(LoginState.LOBBY_ENTRANCE);
					} else {
						channel.close();
						return session;
					}
				}
				
			} else if (state == LoginState.LOBBY_ENTRANCE) {
				if (buffer.readable()) {
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
					if (!RS2MasterCommunication.loginServerOnline()) {
						session.getChannel().write(new LoginResponseCodeBuilder(ReturnCode.LOGIN_SERVER_OFFLINE).build(null));
						return session;
					}
					
					session.setInLobby(true);
					ctx.getPipeline().replace("decoder", "decoder", new RS2GameDecoder(session));
					
					RS2MasterCommunication.writeMasterPacket(new ClientLoginResponseBuilder(new ClientLoginResponseContext(session.getUid(), username, password, session.isInLobby(), GameFlags.worldId)).build());
					return session;
				}
				return session;
			} else if (state == LoginState.GAME_ENTRANCE) {
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
					
					if (!RS2MasterCommunication.loginServerOnline()) {
						session.getChannel().write(new LoginResponseCodeBuilder(ReturnCode.LOGIN_SERVER_OFFLINE).build(null));
						return session;
					}
					
					ctx.getPipeline().replace("decoder", "decoder", new RS2GameDecoder(session));
					
					RS2MasterCommunication.writeMasterPacket(new ClientLoginResponseBuilder(new ClientLoginResponseContext(session.getUid(), username, password, session.isInLobby(), GameFlags.worldId)).build());
					return session;
				}
				return session;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return session;
	}
	
}
