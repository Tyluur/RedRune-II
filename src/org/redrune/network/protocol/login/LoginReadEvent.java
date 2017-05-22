package org.redrune.network.protocol.login;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.redrune.network.protocol.ProtocolThrottle.Protocol;
import org.redrune.network.protocol.ProtocolThrottle.ProtocolRequest;
import org.redrune.network.protocol.login.msg.LoginRequestEvent;
import org.redrune.rs2.node.entity.player.components.managers.InterfaceManager.DisplayMode;
import org.redrune.utility.Misc;
import org.redrune.utility.base37.Base37Utils;
import org.redrune.utility.io.BufferUtils;
import org.redrune.utility.isaac.IsaacRandom;
import org.redrune.utility.isaac.IsaacRandomPair;
import org.redrune.utility.xtea.XTEACryption;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * LoginReadEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
@ProtocolRequest(request = Protocol.REQUEST_LOGIN)
public class LoginReadEvent extends ByteToMessageDecoder {

	public enum LoginRequest {
		REQUEST_LOBBY, REQUEST_WORLD;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.isReadable()) {

			int opcode = in.readUnsignedByte();
			LoginRequest request = Optional.of(opcode == 19 ? LoginRequest.REQUEST_LOBBY : LoginRequest.REQUEST_WORLD)
					.get();

			if (request == null) {
				return;
			}

			int size = in.readShort();
			if (in.readableBytes() != size) {
				ctx.channel().disconnect();
				return;
			}

			int major = in.readInt();

			int minor = in.readInt();
			switch (request) {
			case REQUEST_LOBBY:
				processLobbyRequest(major, minor, ctx, in, out);
				break;
			case REQUEST_WORLD:
				processWorldRequest(major, minor, ctx, in, out);
				break;
			default:
				break;
			}
		}
	}

	private void processLobbyRequest(int major, int minor, ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
			throws InterruptedException {

		byte[] rsa = new byte[in.readUnsignedShort()];
		in.readBytes(rsa);

		ByteBuf block = Unpooled.wrappedBuffer(new BigInteger(rsa).modPow(org.redrune.network.protocol.Protocol.LOGIN_PRIVATE_KEY, org.redrune.network.protocol.Protocol.LOGIN_MODULUS).toByteArray());

		int blockId = block.readUnsignedByte();

		if (blockId != 10) {
			ctx.channel().disconnect().sync();
			return;
		}

		int[] isaacSeed = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			isaacSeed[i] = block.readInt();
		}

		long key = block.readLong();

		if (key != 0) {
			ctx.channel().disconnect();
			return;
		}

		String password = BufferUtils.readString(block);

		long[] loginSeeds = new long[2];
		for (int i = 0; i < loginSeeds.length; i++) {
			loginSeeds[i] = block.readLong();
		}

		byte[] xtea = new byte[in.readableBytes()];
		in.readBytes(xtea);
		XTEACryption.set(isaacSeed).decrypt(xtea, 0, xtea.length);

		block = Unpooled.wrappedBuffer(xtea);

		boolean useString = block.readByte() == 1;
		String username = useString ? BufferUtils.readString(block) : Base37Utils.decodeBase37(block.readLong());
		username = Misc.formatPlayerNameForDisplay(username);

		block.readUnsignedByte();
		block.readUnsignedByte();

		for (int i = 0; i < 24; i++) {
			block.readByte();
		}

		String loginToken = BufferUtils.readString(block);

		if (!loginToken.equals(org.redrune.network.protocol.Protocol.LOGIN_TOKEN)) {
			ctx.channel().disconnect().sync();
			return;
		}

		IsaacRandom input = new IsaacRandom(isaacSeed);
		for (int i = 0; i < 4; i++) {
			isaacSeed[i] += 50;
		}
		IsaacRandom output = new IsaacRandom(isaacSeed);
		IsaacRandomPair pair = new IsaacRandomPair(input, output);
		out.add(new LoginRequestEvent(LoginRequest.REQUEST_LOBBY, major, minor, username, password, pair, null));
	}

	private void processWorldRequest(int major, int minor, ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
			throws InterruptedException {
		in.readUnsignedByte();

		byte[] rsa = new byte[in.readUnsignedShort()];
		in.readBytes(rsa);

		ByteBuf block = Unpooled.wrappedBuffer(new BigInteger(rsa).modPow(org.redrune.network.protocol.Protocol.WORLD_PRIVATE_KEY, org.redrune.network.protocol.Protocol.WORLD_MODULUS).toByteArray());

		int blockId = block.readUnsignedByte();

		if (blockId != 10) {
			ctx.channel().disconnect().sync();
			return;
		}

		int[] isaacSeed = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			isaacSeed[i] = block.readInt();
		}

		long key = block.readLong();

		if (key != 0) {
			ctx.channel().disconnect();
			return;
		}

		String password = BufferUtils.readString(block);

		long[] loginSeeds = new long[2];
		for (int i = 0; i < loginSeeds.length; i++) {
			loginSeeds[i] = block.readLong();
		}

		byte[] xtea = new byte[in.readableBytes()];
		in.readBytes(xtea);
		XTEACryption.set(isaacSeed).decrypt(xtea, 0, xtea.length);

		block = Unpooled.wrappedBuffer(xtea);

		boolean useString = block.readByte() == 1;
		String username = useString ? BufferUtils.readString(block) : Base37Utils.decodeBase37(block.readLong());
		username = Misc.formatPlayerNameForDisplay(username);

		int displayId = block.readUnsignedByte();// mode
		block.readUnsignedShort();// width
		block.readUnsignedShort();// height
		block.readUnsignedByte();

		for (int i = 0; i < 24; i++) {
			block.readByte();
		}

		String loginToken = BufferUtils.readString(block);

		if (!loginToken.equals(org.redrune.network.protocol.Protocol.LOGIN_TOKEN)) {
			ctx.channel().disconnect().sync();
			return;
		}

		IsaacRandom input = new IsaacRandom(isaacSeed);
		for (int i = 0; i < 4; i++) {
			isaacSeed[i] += 50;
		}
		IsaacRandom output = new IsaacRandom(isaacSeed);
		IsaacRandomPair pair = new IsaacRandomPair(input, output);
		out.add(new LoginRequestEvent(LoginRequest.REQUEST_WORLD, major, minor, username, password, pair,
				Optional.of(displayId == 1 ? DisplayMode.FIXED_DISPLAY : DisplayMode.RESIZEABLE_DISPLAY).get()));
	}

}
