package org.redrune.network.lobby.codec;

import com.alex.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.cache.CacheFileStore;
import org.redrune.cache.crypto.ISAACCipher;
import org.redrune.network.NetworkConstants;
import org.redrune.network.master.client.MasterCommunication;
import org.redrune.network.master.client.packet.out.LoginRequestPacketOut;
import org.redrune.network.world.WorldSession;
import org.redrune.network.world.codec.io.RSPacketDecoder;
import org.redrune.utility.backend.ReturnCode;
import org.redrune.utility.rs.buffer.FixedBuffer;
import org.redrune.utility.tool.Misc;

import java.util.Arrays;
import java.util.List;

import static org.redrune.network.NetworkConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class LobbyLoginDecoder extends ByteToMessageDecoder {
	
	/**
	 * The session created of the player being in the world
	 */
	private WorldSession session;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 3) {
			return;
		}
		int opcode = in.readUnsignedByte();
		int size = in.readUnsignedShort();
		if (in.readableBytes() != size) {
			System.out.println("Bad size");
			ctx.close();
			return;
		}
		if (in.readInt() != REVISION) {
			System.out.println("Bad rev");
			session.sendLoginResponse(10);
			return;
		}
		// the data of the login
		byte[] data = new byte[size - 4];
		
		// store the data into the buffer
		in.readBytes(data);
		
		// convert the buffer into a readable object
		FixedBuffer buffer = new FixedBuffer(data);
		
		// handle the correct login case
		if (opcode == LOBBY_OPCODE) {
			setSession(ctx.channel());
			decodeLobbyLogin(ctx, buffer, out);
		} else {
			System.out.println("Unhandled login opcode:" + opcode);
			ctx.close();
		}
	}
	
	/**
	 * Decode the lobby login from buffer.
	 *
	 * @param ctx
	 * 		The context
	 * @param buffer
	 * 		The buffer
	 */
	private boolean decodeLobbyLogin(ChannelHandlerContext ctx, FixedBuffer buffer, List<Object> out) {
		int rsaSize = buffer.readUnsignedShort();
		if (rsaSize > buffer.getRemaining()) {
			System.out.println("here");
			session.sendLoginResponse(10);
			return false;
		}
		byte[] rsaData = new byte[rsaSize];
		buffer.read(rsaData);
		FixedBuffer rsaBuffer = new FixedBuffer(Utils.cryptRSA(rsaData, LOGIN_EXPONENT, LOGIN_MODULUS));
		if (rsaBuffer.readUnsignedByte() != 10) {
			System.out.println("here");
			session.sendLoginResponse(10);
			return false;
		}
		int[] isaacSeed = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			isaacSeed[i] = rsaBuffer.readInt();
		}
		if (rsaBuffer.readLong() != 0) {
			System.out.println("here");
			session.sendLoginResponse(10);
			return false;
		}
		String password = rsaBuffer.readString();
		rsaBuffer.readLong();
		rsaBuffer.readLong();
		buffer.decodeXTEA(isaacSeed, buffer.getOffset(), buffer.getLength());
		String username = Misc.formatPlayerNameForProtocol(buffer.readString());
		int gameType = buffer.readUnsignedByte();
		int language = buffer.readUnsignedByte();
		buffer.skipBefore(24);
		buffer.readString();
		buffer.readInt();
		for (int index = 0; index < 36; index++) {
			int crc = CacheFileStore.STORE.getIndexes()[index] == null ? 0 : CacheFileStore.STORE.getIndexes()[index].getCRC();
			int receivedCRC = buffer.readInt();
			if (crc != receivedCRC && index < 32) {
				System.out.println("here");
				session.sendLoginResponse(6);
				System.out.println("Invalid CRC at index: " + index + ", " + receivedCRC + ", " + crc);
				return false;
			}
		}
		if (Misc.invalidAccountName(username)) {
			System.out.println("here [" + username + "]");
			session.sendLoginResponse(ReturnCode.INVALID_CREDENTIALS.getValue());
			return false;
		}
		if (password.length() >= 30) {
			System.out.println("here");
			session.sendLoginResponse(ReturnCode.INVALID_CREDENTIALS.getValue());
			return false;
		}
		if (!MasterCommunication.isConnected()) {
			System.out.println("here");
			session.sendLoginResponse(ReturnCode.LOGIN_SERVER_OFFLINE.getValue());
			return false;
		}
		int[] inCipher = Arrays.copyOf(isaacSeed, isaacSeed.length);
		int[] outCipher = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			outCipher[i] = isaacSeed[i] + 50;
		}
		
		System.out.println("Username = " + username + ", password = " + password);
		
		session.setInLobby(true);
		session.buildCiphers(new ISAACCipher(inCipher), new ISAACCipher(outCipher));
		ctx.pipeline().replace("decoder", "decoder", new RSPacketDecoder(session));
		MasterCommunication.write(new LoginRequestPacketOut((byte) 0, true, username, password, session.getUid()));
		return true;
	}
	
	/**
	 * Sets the session
	 *
	 * @param channel
	 * 		The channel
	 */
	private void setSession(Channel channel) {
		session = new WorldSession(channel);
		channel.attr(NetworkConstants.SESSION_KEY).set(session);
	}
	
}
