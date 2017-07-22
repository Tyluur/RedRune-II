package org.redrune.network.world.codec;

import com.alex.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.cache.CacheFileStore;
import org.redrune.cache.crypto.ISAACCipher;
import org.redrune.game.GameFlags;
import org.redrune.network.NetworkConstants;
import org.redrune.network.master.client.MasterCommunication;
import org.redrune.network.master.client.packet.out.LoginRequestPacketOut;
import org.redrune.network.world.WorldSession;
import org.redrune.network.world.codec.io.RSPacketDecoder;
import org.redrune.utility.rs.buffer.FixedBuffer;

import java.util.Arrays;
import java.util.List;

import static org.redrune.network.NetworkConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class WorldLoginDecoder extends ByteToMessageDecoder {
	
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
			ctx.close();
			return;
		}
		if (in.readInt() != REVISION) {
			session.sendLoginResponse(10);
			return;
		}
		byte[] data = new byte[size - 4];
		// store the data into the buffer
		in.readBytes(data);
		// convert the buffer into a readable object
		FixedBuffer buffer = new FixedBuffer(data);
		
		// handle the correct login case
		switch (opcode) {
			case WORLD_OPCODE:
				setSession(ctx.channel());
				decodeWorldLogin(ctx, buffer, out);
				break;
			default:
				System.out.println("Unhandled login opcode:" + opcode);
				ctx.close();
				break;
		}
		
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
	
	/**
	 * Decode the world login from buffer.
	 *
	 * @param ctx
	 * 		the channel context.
	 * @param buffer
	 * 		the buffer to read from.
	 */
	private boolean decodeWorldLogin(ChannelHandlerContext ctx, FixedBuffer buffer, List<Object> out) {
		boolean reconnecting = buffer.readBoolean();
		int rsaSize = buffer.readUnsignedShort();
		if (rsaSize > buffer.getRemaining()) {
			session.sendLoginResponse(10);
			return false;
		}
		byte[] rsaData = new byte[rsaSize];
		buffer.read(rsaData);
		FixedBuffer rsaBuffer = new FixedBuffer(Utils.cryptRSA(rsaData, LOGIN_EXPONENT, LOGIN_MODULUS));
		if (rsaBuffer.readUnsignedByte() != 10) {
			session.sendLoginResponse(10);
			return false;
		}
		int[] isaacSeed = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			isaacSeed[i] = rsaBuffer.readInt();
		}
		if (rsaBuffer.readLong() != 0) {
			session.sendLoginResponse(10);
			return false;
		}
		String password = rsaBuffer.readString();
		rsaBuffer.readLong();
		rsaBuffer.readLong();
		buffer.decodeXTEA(isaacSeed, buffer.getOffset(), buffer.getLength());
		String username = buffer.readString();
		buffer.readByte();
		int mode = buffer.readByte();
		int width = buffer.readShort();
		int height = buffer.readShort();
		int displayMode = buffer.readByte();
		buffer.skipAfter(24);
		buffer.readString();
		buffer.readInt();
		for (int index = 0; index < 36; index++) {
			int crc = CacheFileStore.STORE.getIndexes()[index] == null ? 0 : CacheFileStore.STORE.getIndexes()[index].getCRC();
			int receivedCRC = buffer.readInt();
			if (crc != receivedCRC && index < 32) {
				// TODO: crc check
				//session.sendLoginResponse(6);
				//System.out.println("Invalid CRC at index: " + index + ", " + receivedCRC + ", " + crc);
				//return false;
			}
		}
		// build the isaac ciphers
		int[] inCipher = Arrays.copyOf(isaacSeed, isaacSeed.length);
		int[] outCipher = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			outCipher[i] = isaacSeed[i] + 50;
		}
		
		// finished decoding now we can build the session
		session.setInLobby(false);
		session.getViewComponents().setScreenSizeMode(mode);
		session.getViewComponents().setScreenSizeX(width);
		session.getViewComponents().setScreenSizeY(height);
		session.getViewComponents().setDisplayMode(displayMode);
		session.buildCiphers(new ISAACCipher(inCipher), new ISAACCipher(outCipher));
		
		// change the decoders now
		ctx.pipeline().replace("decoder", "decoder", new RSPacketDecoder(session));
		
		// tell the master server we this session to log in
		MasterCommunication.write(new LoginRequestPacketOut(GameFlags.worldId, false, username, password, session.getUid()));
		return true;
	}
}
